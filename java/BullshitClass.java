
package com.bullshit;

import org.apache.commons.lang3.StringUtils;


public final class BullishitClass {

    /**
    * 示例一：  获取当前登录用户ID，如果不存在抛出异常  「大坑：前端传过来 0也是登录成功」
     *
     * @return 当前登录用户ID
     */
    public static String demo1() {
        String userId = "xxx"; // 获取前端传过来userId ，已经经过验证

        if (StringUtils.isEmpty(userId)) {
            throw new InvalidException("用户ID不能为空");
        }

        return userId;
    }
}