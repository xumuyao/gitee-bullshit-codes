<?php

class  bullshitClass
{
    use Db;

    /**
     * 示例一：微信支付异步通知的处理
     * @param $notifyData  收到微信异步通知的数组
     */
    public function demo1(array $notifyData)
    {
        // 根据订单号查找订单
        $order = Db::name('pay_bill')->where(['out_trade_no' => $notifyData['out_trade_no']])->find();

        if (!$order) {// 订单不存在则不处理
            echo 'SUCCESS';
            die;
        }

        if ($notifyData['return_code'] === 'SUCCESS') { // 支付成功了
            // 1.将订单状态更新为已完成
            // 2.给用户的账户加钱
        }
    }
}