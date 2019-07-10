  $del_count = "select count(*) from " . $receive['com_tb'] . " where type = 0";
   if ($old_res !== false && $new_res !== false && $del_res !== false) {
        if($del_count > 0){
            return ['status' => 1, 'msg' => '对比成功,检测到有死链接,即将生成死链接文件,请到根目录查看！'];}
         else {
                return ['status' => 1, 'msg' => '对比成功'];
            }
     }

     来来来，$del_count 坐上来，自己动。
     sql语句：动你麻痹，劳资的execute呢？