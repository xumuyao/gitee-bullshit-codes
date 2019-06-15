import java.util.Random;

/**
 * @author pantao
 * @since 2019/6/15
 */
public class NoVariable {

    public static void main(String[] args) {
        NoVariable nv = new NoVariable();
        char source = 'a';
        // 干掉中间变量，这里只是示例，简化了函数
        String target = nv.foo(nv.bar(nv.fun(nv.yea(source))));
        System.out.println(target);
    }

    public boolean yea(char var) {
        return Character.isLetterOrDigit(var);
    }

    public int fun(boolean var) {
        return (int) (System.currentTimeMillis() & (var ? Integer.MIN_VALUE : Integer.MAX_VALUE));
    }

    public long bar(int var) {
        return ((long) var) << (new Random().nextInt(8));
    }

    public String foo(long var) {
        return String.valueOf(var);
    }
}
