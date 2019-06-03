
/*
 *
 * 很多人习惯在Bean中将整数类型定义为包装类，比如int的定义为了Integer，为了方便在使用Mybatis插入的时候做判断，是否为null。
 * 但是，包装类等于比较的时候使用==，这样难道不怕逻辑跑偏吗？
 * 如下代码，==两边都是Integer类型，想想，这会不会出bug？
 */
if(processBjd.getBusinessType() == ErpProcessBjd.BUSINESS_TYPE_PROJECT_ROY || processBjd.getBusinessType() == ErpProcessBjd.BUSINESS_TYPE_PROJECT_PROCESS){
// TODO
}

/*
 *我们都知道包装类的相等比较尽量使用equals而非==。
 * 在包装类中其实是重写了equals方法，比较的是值大写，
 * 但是，==是比较地址，其实即使比较值，其中也有一些小坑，
 * 看看下面的代码，你觉得结果会是什么？
 */
public class IntegerEqual{
    public static void main(String[] args) {
        Integer a = 100;
        Integer b = 100;
        System.out.println("a == b => "+(a == b));
        Integer c = 3000;
        Integer d = 3000;
        System.out.println("c == d => "+(c == d));​
    }
}

