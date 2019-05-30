/**
 * 前端格式转换 我总觉得有问题，但是又没毛病 -_-!!!
 */
public class NullSerializer {
    public static void main(String[] args) {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializeFilters( new ValueFilter() {
            @Override
            public Object process(Object object, String s, Object value) {
                //数组为空 给 "" 应该也是可以的的吧
                if(value==null && !s.equals("data")){
                    return "";
                }
                return value;
            }
        });
        fastJsonHttpMessageConverter.setFastJsonConfig(config);
    }
}