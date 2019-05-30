<?php
/**
 * 怎么偷懒哈哈哈.
 * User: l
 * Date: 2019/5/30
 * Time: 11:05
 */

class Lazy extends Controller {
    public function ClearCache () {
        // 清理缓存中
        sleep(3);
        // 返回缓存清理成功
        return ['code' => 1,'msg' => '清理缓存成功~~~!!!'];
    }
}
