import java.util.Scanner;
import org.junit.Test;

/***
 * 
 * 一个工作好几年的代码
 * 
 */

public class CarInfo {
	public CarInfoOutPut queryAllCatMonitor(Long orderId) throws ParseException {
		/**获取运单基本信息**/
		CarMonitorOutPut car = dao.getCar(orderId);
		CarTankRecent carTankRecent;
		CarInfoOutPut carInfoOutPut = new CarInfoOutPut();
		if(null!=car){
		   carTankRecent = carService.getCarTankRecent(car.getTractorPlate());
		    /**车速**/
		    if(carTankRecent!=null){
		       car.setSpeed(carTankRecent.getMph());
		    }else{
		       car.setSpeed(0D);
		    }
		
		List<TaskInformationOutPut> list=new ArrayList<TaskInformationOutPut>();
		List<LoadTaskInformationOutPut> queryALLTask = dao.queryALLTask(orderId);
		for (LoadTaskInformationOutPut loadTaskInformationOutPut : queryALLTask) {
				if(loadTaskInformationOutPut.getType()==0){
					TaskInformationOutPut one = new TaskInformationOutPut();
					TaskInformationOutPut two = new TaskInformationOutPut();
					TaskInformationOutPut three = new TaskInformationOutPut();
					TaskInformationOutPut four = new TaskInformationOutPut();
					loadTaskInformationOutPut.getArriveTime();
					if(null!=loadTaskInformationOutPut.getArriveTime()){
		    		   one.setTaskInfo("已经到达");
		    		   one.setTaskType("装液");
		    		   one.setSignType("到达");
		    		   one.setTagName("装液信息");
		    		   one.setTime(loadTaskInformationOutPut.getArriveTime());
		    		   one.setCustomerName(loadTaskInformationOutPut.getCustomerName());
		    		   one.setLng(loadTaskInformationOutPut.getLng());
		    		   one.setLat(loadTaskInformationOutPut.getLat());
		    		   car.setState("已经到达,前往下一个任务点");
		    		   list.add(one);
					}
					if(loadTaskInformationOutPut.getLoadTime()!=null){
						two.setTaskInfo("开始装液");
						two.setTaskType("装液");
						two.setSignType("装液");
						two.setTagName("装液信息");
						two.setTime(loadTaskInformationOutPut.getLoadTime());
						two.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						two.setLng(loadTaskInformationOutPut.getLng());
						two.setLat(loadTaskInformationOutPut.getLat());
			    		car.setState("开始装液,前往下一个任务点");
			    		list.add(two);
					}
					if(loadTaskInformationOutPut.getLeaveTime()!=null){
						three.setTaskInfo("完成装液");
						three.setTaskType("装液");
						three.setSignType("离开");
						three.setTagName("装液信息");
						three.setTime(loadTaskInformationOutPut.getLeaveTime());
						three.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						three.setLng(loadTaskInformationOutPut.getLng());
						three.setLat(loadTaskInformationOutPut.getLat());
			    		car.setState("完成装液,前往下一个任务点");
			    		list.add(three);
					}
					if(loadTaskInformationOutPut.getReceiptTime()!=null&&loadTaskInformationOutPut.getPumpUrl()!=null){
						four.setTaskInfo("装液回单");
						four.setTaskType("装液");
						four.setSignType("装液回单");
						four.setTagName("回单信息");
						four.setTime(loadTaskInformationOutPut.getReceiptTime());
						four.setPumpUrl(loadTaskInformationOutPut.getPumpUrl());
						four.setLoadWeigth(loadTaskInformationOutPut.getLoadWeigth());
						four.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						four.setLng(loadTaskInformationOutPut.getLng());
						four.setLat(loadTaskInformationOutPut.getLat());
			    		car.setState("装液回单,前往下一个任务点");
			    		list.add(four);
					}
					
				}
				if(loadTaskInformationOutPut.getType()==1){
					TaskInformationOutPut one = new TaskInformationOutPut();
					TaskInformationOutPut two = new TaskInformationOutPut();
					TaskInformationOutPut three = new TaskInformationOutPut();
					TaskInformationOutPut four = new TaskInformationOutPut();
					if(loadTaskInformationOutPut.getArriveTime()!=null){
					one.setTaskInfo("已经到达");
					one.setTaskType("卸液");
					one.setSignType("到达");
					one.setTagName("卸液信息");
					one.setTime(loadTaskInformationOutPut.getArriveTime());
					one.setCustomerName(loadTaskInformationOutPut.getCustomerName());
					one.setLng(loadTaskInformationOutPut.getLng());
					one.setLat(loadTaskInformationOutPut.getLat());
					car.setState("到达卸液地点,前往下一个任务点");
					list.add(one);
					}
					if(loadTaskInformationOutPut.getLoadTime()!=null){
			    		two.setTaskInfo("开始卸液");
			    		two.setTaskType("卸液");
			    		two.setSignType("卸液");
			    		two.setTagName("卸液信息");
						two.setTime(loadTaskInformationOutPut.getLoadTime());
						two.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						two.setLng(loadTaskInformationOutPut.getLng());
						two.setLat(loadTaskInformationOutPut.getLat());
						list.add(two);
						car.setState("开始卸液,前往下一个任务点");
					}
					if(loadTaskInformationOutPut.getLeaveTime()!=null){
			    		three.setTaskInfo("完成卸液");
			    		three.setTaskType("卸液");
			    		three.setSignType("离开");
			    		three.setTagName("卸液信息");
			    		three.setTime(loadTaskInformationOutPut.getLeaveTime());
			    		three.setCustomerName(loadTaskInformationOutPut.getCustomerName());
			    		three.setLng(loadTaskInformationOutPut.getLng());
			    		three.setLat(loadTaskInformationOutPut.getLat());
			    		list.add(three);
			    		car.setState("完成卸液,前往下一个任务点");
					}
					if(loadTaskInformationOutPut.getReceiptTime()!=null&&loadTaskInformationOutPut.getPumpUrl()!=null){
						four.setTaskInfo("卸液回单");
						four.setTaskType("卸液");
						four.setSignType("卸液回单");
						four.setTagName("回单信息");
						four.setPumpUrl(loadTaskInformationOutPut.getPumpUrl());
						four.setLoadWeigth(loadTaskInformationOutPut.getLoadWeigth());
						four.setTime(loadTaskInformationOutPut.getReceiptTime());
						four.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						four.setLng(loadTaskInformationOutPut.getLng());
						four.setLat(loadTaskInformationOutPut.getLat());
						list.add(four);
						car.setState("完成卸液,前往下一个任务点");
					}
				}
				if(loadTaskInformationOutPut.getType()==2){
					TaskInformationOutPut one = new TaskInformationOutPut();
					TaskInformationOutPut two = new TaskInformationOutPut();
					TaskInformationOutPut three = new TaskInformationOutPut();
					TaskInformationOutPut four = new TaskInformationOutPut();
					if(loadTaskInformationOutPut.getLoadTime()!=null){
					one.setTaskInfo("已经到达");
					one.setTaskType("倒液");
					one.setSignType("到达");
					one.setTagName("倒液信息");
					one.setTime(loadTaskInformationOutPut.getArriveTime());
					one.setCustomerName(loadTaskInformationOutPut.getCustomerName());
					one.setLng(loadTaskInformationOutPut.getLng());
					one.setLat(loadTaskInformationOutPut.getLat());
					list.add(one);
					car.setState("到达倒液地点,前往下一个任务点");
					}
					if(loadTaskInformationOutPut.getLoadTime()!=null){
			    		two.setTaskInfo("开始倒液");
			    		two.setTaskType("倒液");
			    		two.setSignType("倒液");
			    		two.setTagName("倒液信息");
						two.setTime(loadTaskInformationOutPut.getLoadTime());
						two.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						two.setLng(loadTaskInformationOutPut.getLng());
						two.setLat(loadTaskInformationOutPut.getLat());
						list.add(two);
						car.setState("开始倒液,前往下一个任务点");
					}
					if(loadTaskInformationOutPut.getLeaveTime()!=null){
			    		three.setTaskInfo("完成倒液");
			    		three.setTaskType("倒液");
			    		three.setSignType("离开");
			    		three.setTagName("倒液信息");
			    		three.setTime(loadTaskInformationOutPut.getLeaveTime());
			    		three.setCustomerName(loadTaskInformationOutPut.getCustomerName());
			    		three.setLng(loadTaskInformationOutPut.getLng());
			    		three.setLat(loadTaskInformationOutPut.getLat());
			    		list.add(three);
			    		car.setState("完成倒液,前往下一个任务点");
			    		
					}
					if(loadTaskInformationOutPut.getReceiptTime()!=null&&loadTaskInformationOutPut.getPumpUrl()!=null){
						four.setTaskInfo("倒液回单");
						four.setTaskType("倒液");
						four.setSignType("倒液回单");
						four.setTagName("回单信息");
						four.setPumpUrl(loadTaskInformationOutPut.getPumpUrl());
						four.setLoadWeigth(loadTaskInformationOutPut.getLoadWeigth());
						four.setTime(loadTaskInformationOutPut.getReceiptTime());
						four.setCustomerName(loadTaskInformationOutPut.getCustomerName());
						four.setLng(loadTaskInformationOutPut.getLng());
						four.setLat(loadTaskInformationOutPut.getLat());
						list.add(four);
						car.setState("倒液回单,准备返程");
					}
			}
		}
		carInfoOutPut.setCar(car);
		carInfoOutPut.setTask(list);
	}
		return carInfoOutPut;
	}
	

}
