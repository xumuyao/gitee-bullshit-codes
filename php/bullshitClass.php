<?php

/**
 * 多看看坑爹代码也是一种成长
 * Class bullshitClass
 */

class  bullshitClass
{

    /**
     * 示例一：微信支付异步通知的处理 「大坑：处理异步通知时切记只能处理一次，否则会造成用户充值1次却多次入账的情况出现」
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

    /**
     * 示例二：事务的处理 「大坑：return 之后不要有其它代码」
     *
     */
    public function demo2()
    {
        Db::startTrans();
        try {
            // 逻辑1
            // 逻辑2
            // ...
            return true;
            Db::commit();
        } catch (Exception $e) {
            return false;
            Db::rollback();

        }
    }

    /**
     * 示例三：下单api 「大坑：商品实际价格应在后端数据库来获取，不能以前端提交的价格来作为商品价格」
     * 已经过身份认证的用户才可访问该方法
     * @param $productData
     * @return array
     */
    public function demo3($productData)
    {
        $product = Db::name('product')->find($productData['product_id']);
        if (!$product || ($product->is_on_sell === 'F')) return ['status' => 'error', 'msg' => '商品不存在或已下架'];

        // 判断库存是否充足 TODO

        $orderData = [
            'user_id' => 'xx',// 当前登录用户 id
            'product_id' => $productData['product_id'],
            'order_sn' => generateOrderSn(), // 生成唯一订单号
            'product_price' => $productData['product_price'] * $productData['product_num'],
            'status' => 0,// 新订单
            'create_time' => time()
        ];
        // 拉取微信支付数据
        $result = $this->wxpay->order->unify([
            'body' => '博彩投注',
            'out_trade_no' => $orderData['order_sn'],
            'total_fee' => $orderData['product_price'] * 100,
            'spbill_create_ip' => $_SERVER["REMOTE_ADDR"], // 可选，如不传该参数，SDK 将会自动获取相应 IP 地址
            'notify_url' => 'http://xxxxxx', // 支付结果通知网址，如果不设置则会使用配置里的默认地址
            'trade_type' => 'APP',
        ]);

        if ($result['return_code'] == 'FAIL') {
            return ['status' => 'error', 'msg' => $result['return_msg']];
        }

        // 生成预支付订单
        Db::name('wx_order')->insert($orderData);
        return ['status' => 'success', 'msg' => $result];
    }
}