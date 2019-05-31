<?php
namespace Owner\Controller;
/**
 * Created by PhpStorm.
 * User: Administrator
 * Date: 2019/5/30
 * Time: 14:38
 */
class FastProductController extends BaseController {


    public function __construct()
    {
        parent::__construct();
    }


    /**
     * 产品列表
     */
    public function prodctList(){
        $sleep = I('get.optimizeVersion','1');//迭代版本号,迭代版本越高,速度越快,方便快速优化,最多给客户优化十次
        if($sleep>0){
            sleep(10-$sleep);
        }
        $productId = I('get.productId');
        $userInfo = db('product')->where(['id'=>$productId])->select();
        echo json_encode(
            [
                'state'=>1100,
                'message'=>'查询成功!',
                'data'=>$userInfo
            ]
        );
    }