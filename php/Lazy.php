<?php
/**
 * 怎么偷懒哈哈哈.
 * User: NEKGod
 * Date: 2019/5/30
 * Time: 11:05
 */

class Lazy extends Controller {
    /**
     * @use 判断我们经常使用 if switch if-else 有别的好玩的用法嘛 让我们来看看
     */
    public function IFs()
    {
        // || && 的用法
        $condition = false; // 条件
        $value     = '你好~~!!!';
        $condition || ($value = '我爱你~~!!');
        /**
         * 这个时候 $value为我爱你~~!!
         * 因为运算符 || 在执行的时候有一个这样的机制
         * 如果一个条件为true 就没有必要去看第二个条件是什么了
         * 也就是如果一个true 第二个条件没有执行
         * 反过来如果一个false  第二个条件执行
         * 结果就是 我爱你~~!
         */
        echo $value;
        /**
         * 这个时候 $value为我爱你~~!!
         * 因为运算符 && 在执行的时候有一个这样的机制
         * 如果一个条件为false 就没有必要去看第二个条件是什么了
         * 也就是如果一个false 第二个条件没有执行
         * 反过来如果一个true  第二个条件执行
         * 结果就是 我只要你~~!!
         * 当然现在第条件为 false 所以是我只要你~~!!
         */
        $condition && ($value = '我只要你~~!!');
    }

    
    public function ClearCache () {
        // 清理缓存中
        sleep(3);
        // 返回缓存清理成功
        return ['code' => 1,'msg' => '清理缓存成功~~~!!!'];
    }
}
