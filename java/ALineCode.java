package spring;


public class B {


    public static void main(String[] args)  {
        System.out.println(isTwoPower(16));
    }

    /**
     * 看不懂的代码
     * @param n
     * @return
     */
    public static boolean isTwoPower(int n){
        return (n & n-1) == 0;
    }

}
