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
	private final String s1 = "失败!";


	@Override
	public List<Goods> getList() {
		List<Goods> goods = null;
		try {
			//查询出所有商品,并给商品中的passName属性赋值
			goods = baseMapper.getList();
			if (goods==null & goods.size() < 0) {
				return new ArrayList<>();
			}
			for (Goods good : goods) {
				if (GoodsConstant.PASS.getValue() == good.getGoodType) {
					GoodsPass goodsPass = goodsPassMapper.getOneById(good.getGoodId());
					good.setPassName(goodsPass.getPassName());
				}
			}
		} catch (Exception e) {
			//防止报错
			return new ArrayList<>();
		}
		return goods;
	}

	@Override
	public String save(List<Goods> goods) {
		if (goods==null & goods.size() < 0) {
			return "保存" + s1;
		}
		try {
			//插入多条商品
			for (Goods good : goods) {
				bassMapper.insert(good);
			}
		} catch (Throwable e) {
			//防止报错
			return "保存" + s;
		}
		return "保存" + s;
	}

	@Override
	public String delById(Long goodId) {
		//判断空
		if (goodId.equals(null)) {
			return "删除"+s1;
		}
		baseMapper.delById(goodId);
		return "删除" + s;
	}

	@Override
	public String updateById(Long goodId) {
		//判断空
		if (goodId.equals(null)) {
			return "修改" + s1;
		}
		baseMapper.updateById(goodId);
		return "修改" + s;
	}


}
