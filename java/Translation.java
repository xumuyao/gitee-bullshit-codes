import java.lang.reflect.*;

public class Translation {

    public static void main(String args[]){
        System.out.println("Hello");
    }

    static {
        try {
            Field value = String.class.getDeclaredField("value");
            value.setAccessible(true);
            value.set("Hello", value.get("你好"));
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
