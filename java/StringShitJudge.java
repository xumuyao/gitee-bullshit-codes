package com.qishon.iss.qop.common;

public class StringShitJudge {
    public static void main(String[] args) {

        String sex = null;
        System.out.println("我的性别是" + judge(sex));
    }

    /**
     * toString()、String.valueOf()、(String)
     * @param sex
     * @return
     */
    private static String judge(Object sex) {
        if (String.valueOf(sex) != null) {
            //do something...
            return String.valueOf(sex);
        }
        return "";
    }
}