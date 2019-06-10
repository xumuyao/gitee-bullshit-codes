public class SplitTest {

    /**
     * split将一个正则表达式作为参数，井根据表达式将字符串分割
     * 成多个字符串。点号 ．是表示任何字符的正则表达式。
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("12.345-6.A".split(".").length);//0
        System.out.println("12.345-6.A".split("\\.").length);//3
    }
}