import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TryCatchTransaction {
    @Resource
    private ServiceA serviceA;
    @Resource
    private ServiceB serviceB;

    @Transactional
    public void exchange(){
        //一次操作1000个用户
        for(int i=0;i<1000;i++) {

            serviceA.doExchangeA();
            try {
                serviceB.doExchangeB();
                //抛出某个异常
                throw new IllegalArgumentException("抛出某个异常");
            } catch (Exception e) {

            }

        }

    }
}


@Service
class  ServiceA{
    @Transactional
    public void doExchangeA(){
        //发送兑换劵，一张100元
    }

}

@Service
class ServiceB{
    @Transactional
    public void doExchangeB(){
       //记录数据到B表
    }
}
