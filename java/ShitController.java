package com.hello.xxx.modules.order.web.front;

import com.hello.xxx.common.config.*;
import com.hello.xxx.common.exchellotion.CommonExchellotion;
import com.hello.xxx.common.utils.StringUtils;
import com.hello.xxx.common.utils.ZxingHandler;
import com.hello.xxx.common.utils.tencent.wechat.CommonUtil;
import com.hello.xxx.modules.attribute.entity.xxxAttribute;
import com.hello.xxx.modules.attribute.service.xxxAttributeService;
import com.hello.xxx.modules.xxxminiconfig.dao.xxxMiniConfigDao;
import com.hello.xxx.modules.order.dao.xxxOrderDao;
import com.hello.xxx.modules.order.entity.xxxOrder;
import com.hello.xxx.modules.orderitem.entity.xxxOrderItem;
import com.hello.xxx.modules.orderitem.service.xxxOrderItemService;
import com.hello.xxx.modules.product.dao.xxxProductDao;
import com.hello.xxx.modules.product.entity.xxxProduct;
import com.hello.xxx.modules.receive.dao.xxxReceiveDao;
import com.hello.xxx.modules.user.entity.xxxUser;
import com.hello.xxx.modules.useropenid.entity.xxxUserOpenid;
import com.hello.xxx.modules.useropenid.service.xxxUserOpenidService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

@Api(value = "order",description = "订单相关api")
@Controller
@RequestMapping("/f/api/order/")
public class OrderController {

    @Autowired
    private xxxOrderDao orderDao;

    @Autowired
    private xxxAttributeService xxxAttributeService;

    @Autowired
    private xxxProductDao productDao;

    @Autowired
    private xxxOrderItemService orderItemService;

    @Autowired
    private xxxUserOpenidService xxxUserOpenidService;

    @ApiOperation(value = "创建普通订单接口",produces = MediaType.APPLICATION_JSON_VALUE,httpMethod = "POST")
    @RequestMapping("createOrder")
    public  String createOrder(String productId, String num, xxxUser user, String parentId, String attributeId, String address, String realName, String officeId, @ApiIgnore HttpServletResponse response) throws CommonExchellotion {
        xxxUserOpenid xxxUserOpenid = xxxUserOpenidService.getByUserIdAndOfficeId(user.getId(),officeId);
        xxxProduct product = productDao.selectByIdForUpdate(productId);

        //检测限购数量
        if ( product.getLimitNum()!= 0){
            Integer numA = orderDao.selectOrderByStatusAndProduct(productId, StatusConstants.ORDER_STATUS_NO,user.getId());
            Integer numB = orderDao.selectOrderByStatusAndProduct(productId, StatusConstants.ORDER_STATUS_USE,user.getId());
            Integer numC = orderDao.selectOrderByStatusAndProduct(productId, StatusConstants.ORDER_STATUS_GIVE_ING,user.getId());
            Integer numD = orderDao.selectOrderByStatusAndProduct(productId, StatusConstants.ORDER_STATUS_GIVE_SUCCESS,user.getId());
            if (numA == null){
                numA = 0;
            }
            if (numB == null){
                numB = 0;
            }
            if (numC == null){
                numC = 0;
            }
            if (numD == null){
                numD = 0;
            }
            int sum = numA + numB + numC + numD + Integer.parseInt(num);
            if ( sum > product.getLimitNum()){
                throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0009,"已超过购买数量",null);
            }
        }


        //减少数量
        if (product.getProductNum() <= 0){
            throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0001,"已售罄",null);
        }
        if(product.getProductNum() < Integer.parseInt(num)){
            throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0001,"份数不足",null);
        }
        if (product.getBuyEndTime()!=null&&System.currentTimeMillis()>product.getBuyEndTime().getTime()){
            throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0002,"抢购已结束",null);
        }
        if (!product.getProductStatus().equals(StatusConstants.PRODUCT_STATUS_YES)){
            throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0003,"产品已下架",null);
        }
        if (FlagConstants.PRODUCT_ADDRESS_FLAG_TRUE.equals(product.getAddressFlag())){
            if (StringUtils.isEmpty(address) || StringUtils.isEmpty(realName) ){
                throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0007,"快递信息不完整",null);
            }
        }
        if (StringUtils.isEmpty(attributeId)){
            throw new CommonExchellotion(ReturnConstants.ORDER_CODE_0008,"未选择套餐",null);
        }
        product.setProductNum(product.getProductNum() - Integer.parseInt(num));
        productDao.update(product);
        xxxOrder order = new xxxOrder();
        xxxAttribute attribute = xxxAttributeService.get(attributeId);
        order.setSinglhellorice(attribute.getContent());
        order.setCountPrice(attribute.getContent().multiply(new BigDecimal(num)));
        order.setAttributeName(attribute.getPunctuation());
        order.setCostPrice(attribute.getCostPrice().multiply(new BigDecimal(num)));
        order.setOfficeId(officeId);
        order.setAddress(address);
        order.setRealName(realName);
        order.setUserOpenId(xxxUserOpenid.getOpenId());
        order.setUserId(user.getId());
        order.setNum(Integer.parseInt(num));
        order.setPhone(user.getPhone());
        order.setProductId(product.getId());
        order.setOrderStatus(StatusConstants.ORDER_STATUS_NO);
        order.setVaildStartTime(product.getValidStartTime());
        order.setVaildEndTime(product.getVaildEndTime());
        order.setOrderNo(CommonUtil.createOrderNo());
        order.setSharhellorice(product.getSharhellorice().setScale(2));
        order.setCommentStatus(StatusConstants.ORDER_COMMENT_NO);
        if (StringUtils.isNotEmpty(parentId)){
            order.setParentId(parentId);
        }
        else {
            order.setParentId(FlagConstants.ORDER_FLAG_PARENT);
        }
        save(order);
        String codeBashelloath = Global.getConfig("imgUrl")+MiniProgramConfig.QR_PATH;
        //添加订单项
        for (int i=0;i<order.getNum();i++){
            xxxOrderItem orderItem = new xxxOrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderNo(order.getOrderNo());
            orderItem.setRandomNum(CommonUtil.createCode());
            orderItem.setProductId(order.getProductId());
            orderItem.setStatus(StatusConstants.ORDER_ITEM_STATUS_NO);
            orderItem.setUserId(user.getId());
            orderItem.setOfficeId(officeId);
            String ipName = Global.getConfig("ipName");
            String imgPath = codeBashelloath+orderItem.getRandomNum()+".png";
            ZxingHandler.encode2(orderItem.getRandomNum(),100,100,imgPath);
            orderItem.setQrCode(ipName+imgPath);
            orderItemService.save(orderItem);
        }
        return "success";
    }

}
