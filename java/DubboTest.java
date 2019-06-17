package demo;

import javax.annotation.Resource;

/**
 * @author liujian
 */

class DemoService {

    private static final String VERSION = "1.1.18";

    public String getVersion() {
        return VERSION;
    }

}

public class DubboTest {

    @Resource
    private DemoService demoService;

    public String testConnection() {
        return demoService.getVersion();
    }

    public void init() {
        //入职时写个dubbo服务，在初始化类中测试配置的dubbo服务是否能成功调用
        //由于刚从c++转到java，还没有什么经验，使用了new，而不是springutil.getbean，导致一直报空指针错误
		//搞了好久好久，导师还特地写了个dubbo demo来验证我是不是哪里配置有问题
		//半天后突然顿悟，那种感觉，就好像冷冷的冰雨在脸上胡乱地拍
        new DubboTest().testConnection();
    }

}
