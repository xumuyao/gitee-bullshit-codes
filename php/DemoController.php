<?php 
namespace app\shop\controller;
use think\Db;

class DemoController extends AdminBaseController{

    //哥们说他写了一个通用的删除函数
    public function delete()
    {
        $name = $this->request->param('name');
        $id = $this->request->param('id');

        Db::name($name)->where('id',$id)->delete();
        $this->success('删除成功');
    }

}
