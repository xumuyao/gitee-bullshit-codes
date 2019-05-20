# 本方法的目标是爬取某站点的资源到本地, 
# 然后上传至七牛云或者通过邮件附件发送到指定的email, 最后删除本地的文件
# 由于文件名是未知的, 是可以任意构造的, 所以最后删除本地文件的代码是致命的
# 解决方案:
# a. 将下载文件放置一个文件夹 temp 下, 然后 rm -f temp/*
# b. 将文件名使用base64等进行编码, 然后 rm -f encrypt_base64(filename)

def download(self, url, email):
        # 解析下载链接
        download_url = self.download_parser(url)
        # 获取cookie_jar
        jar = self.get_cookie_jar()
        headers = {
            'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) '
                          'AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36'
        }
        # 请求资源内容
        r = requests.get(download_url, cookies=jar, headers=headers)

        if r.status_code == 200:
            # 获取文件名
            filename = str(r.headers['Content-Disposition'].split('"')[1].encode('ISO-8859-1'), encoding='utf-8')
            # 写入文件
            with open(filename, 'wb') as f:
                f.write(r.content)

            file_size = helper.get_file_size(filename)
            if file_size is not None:
                # 文件如果大于50MB，使用七牛云存储
                if file_size > 50 * 1024 * 1024:
                    print('使用七牛云')
                    # 上传到七牛云
                    helper.upload_to_qiniu(filename)
                    helper.send_email(email, self.qiniu_domain + filename)
                else:
                    print('使用附件')
                    helper.send_email_with_file(email, filename)

            # 删除本地文件
            cmd = 'rm -f ' + filename
            os.system(cmd)
