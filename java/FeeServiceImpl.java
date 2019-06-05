package com.demo.service.impl;

import java.util.List;

import javax.sound.sampled.Line;

import com.demo.service.IFeeService;

/**
 * 两年多工作经验的妹子写的代码 需求:物流公司会找各收货点，然后各收货点有不同线路的车辆，每个线路每天发多个班次的车，
 * 每个班次会带几件货物，每件货物都有服务费，查询某一天一共需要支付多少服务费
 */
public class FeeServiceImpl implements IFeeService {

	/**
	 * 方法名和变量名类名都是我乱命名的，英文不行，当时用的是mybatis, 当时钱就是用float类型
	 */
	@Override
	public Float calcServiceFee() {
		float fee = 0;
		// 操作数据库，查询所有收货站点，不多，20个左右
		List<Station> stations = stationDao.findAll();
		// 遍历所有收货点
		for (Station station : stations) {
			// 操作数据库 查询出每个收货站点有多少条路线,平均80条路线
			List<Line> lines = lineDao.findByStation(station);
			// 遍历所有路线
			for (Line line : lines) {
				// 操作数据库，查询出每条线路当天有几个班次,多的情况是30分钟一个班次，少的情况一天1至2个班次，算上加班车，平均一天13个班次
				List<Vehicle> vehicles = vehicleDao.findByLineAndTime();
				// 遍历所有班次
				for (Vehicle vehicle : vehicles) {
					// 操作数据库，查询每个班次带了几件货物，每件货物都是一条记录，平均每个班次带30件左右
					List<Goods> goodsList = goodsDao.findByVehicle();
					// 遍历所有货物记录
					for (Goods goods : goodsList) {
						fee += goods.fee;
					}
				}
			}
		}
		return fee;
	}

}
