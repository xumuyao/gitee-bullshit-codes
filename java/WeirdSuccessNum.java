
public class WeirdSuccessNum {
    
    public void SomeBusinessHandler(Map<String, List<Object>> businessMap) {
        
        int successedNum = 0;
        for(Iterator<Entry<String, List<Object>>> it = businessMap.entrySet().iterator(); it.hasNext();){
            Entry<String, List<Object>> entry = it.next();
			
            // 遍历业务数据
            for(Object obj : entry.getValue()){
				
                // 业务处理成功
                if(业务处理成功){
                    successedNum++;
                }
            }
        }
        
        if(successedNum != businessMap.values().size()){
            // 有部分未成功
        }else{
            // 业务全部处理成功啦
        }
        
        /*
         * 赶紧跑去问大神为啥业务处理结果不稳定
         * 大神看了代码表情后-_-
         * /
    }

}
