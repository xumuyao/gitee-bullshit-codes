
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
    /**
    * 示例二：获取车辆列表「大坑：第一条 sql 只是为了计算个总数据量。本来查询一次就够了百要查询两次」
     *
     * @return 分页数据
     */
    @Override
    public Map<String, Object> demo2(JSONObject param) {

        // 查询获取总数
        List list1 = dao.queryForList("rentCar.getUserCars", param);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", list1.size());
        result.put("content", dao.queryForList("rentCar.getUserCarsJoinBrand", param));
        return  result;
    }

    /**
     * 示例三：数据入库「大坑：不使用批量插入，使用for 循环一条条处理」
     *
     */
    @Override
    public void demo3(JSONObject param) {

        List<Map> priceList = (List<Map>) param.get("priceList");
        for (Map map : priceList) {
            map.putAll(param);
            checkAddPriceRule(map);    // 调用数据校验方法

            map.put("business_type",business_type);
            map.put("description",description);
            insertPriceRule(map);   // 调用插入数据库方法
        }
    }

    /**
     * 示例三：获取明天的日期「大坑：我刚刚就被前端坑过，他传了个 2019-06-31 过来」
     *
     */
    @Override
    public void demo4(JSONObject param) {
        String date = getSysStrDate();
        List<String> dateArray = Arrays.asList(date.split("-"));
        String year = dateArray.get(0);
        String month = dateArray.get(1);
        Integer day = Integer.valueOf(dateArray.get(2)) + 1;
        String tomorrow = year + "-" + month + "-"+day;
    }
}