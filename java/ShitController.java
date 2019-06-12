package com.hello.xxx.modules.order.web.front;

@Controller
@RequestMapping("/f/api/order/")
public class OrderController {

    @RequestMapping("createOrder")
    public  String createOrder(String productId, String num) throws CommonExchellotion {
        /**
         * 这是一个创建订单的方法，大概有如下三步：
         * 1.减少商品数量
         * 2.创建订单
         * 3.创建订单项
         * 问题：坑爹的上一任把所有逻辑写在controller里面，将近200-300行代码，整个controller可读性真的是臭的要死；并且在
         * 这个创建的操作里面，涉及到了商品表，订单表，订单项表的操作，上一个人完全没考虑到事务的问题，每个操作都是用service完
         * 成的！独立开的事务！
         */
        return "success";
    }

}
