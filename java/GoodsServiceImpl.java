package com.xxx.sys.goods.service.impl;

import com.xxx.sys.goods.mapper.GoodsPassMapper;
import com.xxx.sys.goods.mapper.GoodsMapper;
import com.xxx.sys.goods.Model.Goods;
import com.xxx.sys.goods.Model.GoodsPass;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GoodsServiceImpl implements GoodsService<GoodsMapper> {

	private final GoodsPassMapper goodsPassMapper;
	private final String s = "数据成功!";


	@Override
	public List<Goods> getList() {
		List<Goods> goods = null;
		try {
			goods = baseMapper.getList();
			for (Goods good : goods) {
				if (GoodsConstant.PASS.getValue() == good.getGoodType) {
					GoodsPass goodsPass = goodsPassMapper.getOneById(good.getGoodId());
					good.setPassName(goodsPass.getPassName());
				}
			}
		} catch (Exception e) {
			return new ArrayList<>();
		}
		return goods;
	}

	@Override
	public String save(List<Goods> goods){
		try {
			for (Goods good : goods) {
				bassMapper.insert(good);
			}
		}catch (Throwable e){
			return "保存" +s;
		}
		return "保存" + s;
	}

	@Override
	public String delById(Long goodId){
		baseMapper.delById(goodId);
		return "删除"+s;
	}

	@Override
	public String updateById(Long goodId){
		baseMapper.updateById(goodId);
		return "修改"+s;
	}



}
