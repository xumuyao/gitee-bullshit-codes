<?php
/**
 * 某次使用阿里云的函数计算时，他们的函数计算有问题导致压力一大系统就挂掉。
 * 然后给阿里云提交了工单后，阿里的客服给的解决方案是：
 * 捕获到这个异常之后OOM自爆!
 * 从代码逻辑上或许这没问题。。。。。但是这种解决之道，我相信也只有阿里云这种不靠谱的才会提出来吧。
 * 
 *                  ㄟ( ▔, ▔ )ㄏ
 * 
 */

$str = str_repeat('a', 4*1024 * 1024 * 1024); 

?>