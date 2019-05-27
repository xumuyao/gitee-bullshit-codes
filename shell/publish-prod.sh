#下面是发布到生产环境的shell,截取的代码片段
#要发布的是prod分支,😄

#首先切换到项目目录,往下你会发现这步好像并没有没什么用
cd /home/app

#干掉xx-vue目录,手法很暴力,突然想到了rm -rf /
rm -rf /home/app/xx-java
#除了速度慢点好像没啥问题...
git clone git@gitee.com:xx/xx-java.git --depth=10

#切换到项目目录，这步好像也没什么用
cd /home/app/xx-java

#这里他想切换到prod分支,但实际上git帮他创建了一个msater副本
git checkout -B prod

# 启动项目 然后发布的一直都是master的代码,😂(妈耶,坑爹啊)
sh /home/app/start-prod.sh