<?php
declare(strict_types = 1 );
namespace app\admin\controller;

use think\Db;

class ForMysql
{
    public function getOrder()
    {
        // 一个列多个条件
        $res = $this->request->only('XXXXXXXXXX');

        foreach ($res['XXXXXXXXXX'] as $k => $v) {
            Db::name('order')->where('XXXXXXXXXX',$v)->select();
        }
    }

    /**
     * 这道我这一辈子都不能忘记的写法
     * @author yansong
     */
    public function ifTransaction()
    {
        $XXX = $this->request->only('XXX');
        Db::startTrans();
        $a = Db::name('user')->where('id', $XXX)->update(['is_activity' => 2]);
        if ($a) {
            Db::commit();
        } else {
            Db::rollback();
        }
    }
}
