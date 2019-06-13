public class SpinAndFetchSomeData {

    /**
     * 下单后通过MQ触发的通知操作，根据订单信息通知用户和其他子系统进行进一步的处理。但是从MQ收到该通知时，订单信息还未入库，而且不知道入库操作和通知操作的时序关系（全是异步操作，通过MQ和回调来触发操作，分析代码无法知道调用顺序）
     */
    public void notify(String modelId) {
        SomeModel model = this.selectSomeModel(modelId);

        // ...处理逻辑
    }
    
    /**
     * 线程自旋等待
     */
    private SomeModel selectSomeModel(String id) {
        SomeModel model;
        // to prevent that getting the response is to fast
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            model = mapper.selectById(id);
            if (null != model)
                return model;
        }
        return new SomeModel();
    }
}