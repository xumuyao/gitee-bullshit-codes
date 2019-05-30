<?php 
namespace app\game\controller;
use think\Db;

class GameController extends AdminBaseController{

    /* 又想起同事一个神操作
     * 大转盘小游戏，开始玩一次消耗5金币，转到几个金币用户余额增加几金币
     */
    public function gamestart()
    {
        $id = cmf_get_user_id();
        Db::name('user')->where('id',$id)->setDec('coin',5);
    }

    /* 抓取概率在前端，游戏结束点击领奖
     */
    public function gameover()
    {
        $id = cmf_get_user_id();
        $coin = $this->request->param('coin');

        if ($coin != 0){
            Db::name('user')->where('id',$id)->setInc('coin', $coin);
        }
        //给他当面演示注入coin=500以后，改成以下条件
        if ($coin != 0 && $coin <= 100){    //解释说100金币是能转到最大额的 T T
            Db::name('user')->where('id',$id)->setInc('coin', $coin);
        }
    }


    /* 说到底是年纪小（当时不满21）
     * 给他讲不要相信接收的数据、给他讲开始即结束，奈何心里只有+15
     */

}

