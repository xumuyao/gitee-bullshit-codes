package com.nmoney.cashloan.manage.job;

import com.nmoney.cashloan.cl.domain.BorrowRepayLog;
import com.nmoney.cashloan.cl.domain.UrgeRepayOrder;
import com.nmoney.cashloan.cl.domain.UrgeRepayOrderFlow;
import com.nmoney.cashloan.cl.domain.UrgeRepayUserAlloca;
import com.nmoney.cashloan.cl.enums.UrgeRepayType;
import com.nmoney.cashloan.cl.model.UrgeRepayOrderModel;
import com.nmoney.cashloan.cl.service.BorrowRepayLogService;
import com.nmoney.cashloan.cl.service.ClUrgeRepayUserAllocaService;
import com.nmoney.cashloan.cl.service.UrgeRepayOrderFlowService;
import com.nmoney.cashloan.cl.service.UrgeRepayOrderService;
import com.nmoney.cashloan.core.common.context.Global;
import com.nmoney.cashloan.core.common.exception.ServiceException;
import com.nmoney.cashloan.core.domain.User;
import com.nmoney.cashloan.core.service.CloanUserService;
import com.nmoney.cashloan.manage.domain.QuartzInfo;
import com.nmoney.cashloan.manage.domain.QuartzLog;
import com.nmoney.cashloan.manage.service.QuartzInfoService;
import com.nmoney.cashloan.manage.service.QuartzLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tool.util.BeanUtil;
import tool.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 催收员动态分配
 *
 * @author xym
 */
@Component
@Lazy(value = false)
public class QuartzUrgeRepayUserAlloca implements Job {

    private static final Logger logger = LoggerFactory.getLogger(QuartzUrgeRepayUserAlloca.class);

    /**
     * 催收员动态分配催收单
     *
     * @throws ServiceException
     */
    public String urgeUserAlloca() throws ServiceException {

        long startTime = System.currentTimeMillis();
        final UrgeRepayOrderService urgeRepayOrderService = (UrgeRepayOrderService) BeanUtil.getBean("urgeRepayOrderService");
        final UrgeRepayOrderFlowService urgeRepayOrderFlowService = (UrgeRepayOrderFlowService) BeanUtil.getBean("urgeRepayOrderFlowService");
        final BorrowRepayLogService borrowRepayLogService = (BorrowRepayLogService) BeanUtil.getBean("borrowRepayLogService");
        final ClUrgeRepayUserAllocaService urgeRepayUserAllocaService = (ClUrgeRepayUserAllocaService) BeanUtil.getBean("urgeRepayUserAllocaService");
        final CloanUserService cloanUserService = (CloanUserService) BeanUtil.getBean("cloanUserService");
        boolean isDev = "dev".equals(Global.getValue("app_environment"));
        //所有催收计划
        List<UrgeRepayOrder> urgeRepayOrders = urgeRepayOrderService.listAll(new HashMap<String, Object>());
        /**
         * 前手的规则可以动态调整
         */
        String[] frontHandRule = Global.getValue("urgeRepay_fronthand_rule").split(",");

        /**
         * 用户归类分类
         */
        Map<Integer, List<UrgeRepayUserAlloca>> classifyUserMap = new HashMap<Integer, List<UrgeRepayUserAlloca>>(4) {{
            put(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
            put(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
            put(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
            put(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
        }};

        Map<Integer, List<UrgeRepayUserAlloca>> classifyVacationUserMap = new HashMap<Integer, List<UrgeRepayUserAlloca>>(4) {{
            put(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
            put(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
            put(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
            put(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode(), new ArrayList<UrgeRepayUserAlloca>());
        }};

        /**
         * 筛选具有权限的用户
         */
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("state", 0);
        List<UrgeRepayUserAlloca> urgeRepayUserAllocas = urgeRepayUserAllocaService.selectAll(paramsMap);
        if (urgeRepayUserAllocas != null && urgeRepayUserAllocas.size() > 0) {
            for (UrgeRepayUserAlloca urgeRepayUserAlloca : urgeRepayUserAllocas) {
                boolean isFront = urgeRepayUserAlloca.getAllocastr().contains(String.valueOf(UrgeRepayType.FRONT_HAND.getCode()));
                boolean isNew = urgeRepayUserAlloca.getAllocastr().contains(String.valueOf(UrgeRepayType.NEW_CUSTOMER.getCode()));
                if (isFront && isNew) {
                    classifyUserMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                } else if (isFront && !isNew) {
                    classifyUserMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                } else if (!isFront && !isNew) {
                    classifyUserMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                } else {
                    classifyUserMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                }
            }
        }

        paramsMap.put("state", 1);
        List<UrgeRepayUserAlloca> urgeRepayVacationUserAllocas = urgeRepayUserAllocaService.selectAll(paramsMap);
        if (urgeRepayVacationUserAllocas != null && urgeRepayVacationUserAllocas.size() > 0) {
            for (UrgeRepayUserAlloca urgeRepayUserAlloca : urgeRepayVacationUserAllocas) {
                boolean isFront = urgeRepayUserAlloca.getAllocastr().contains(String.valueOf(UrgeRepayType.FRONT_HAND.getCode()));
                boolean isNew = urgeRepayUserAlloca.getAllocastr().contains(String.valueOf(UrgeRepayType.NEW_CUSTOMER.getCode()));
                if (isFront && isNew) {
                    classifyVacationUserMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                } else if (isFront && !isNew) {
                    classifyVacationUserMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                } else if (!isFront && !isNew) {
                    classifyVacationUserMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                } else {
                    classifyVacationUserMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayUserAlloca);
                }
            }
        }

        /**
         * 催收分类
         */
        Map<Integer, List<UrgeRepayOrder>> classifyUrgeRepayMap = new HashMap<Integer, List<UrgeRepayOrder>>(4) {{
            put(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode(), new ArrayList<UrgeRepayOrder>());
            put(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode(), new ArrayList<UrgeRepayOrder>());
            put(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode(), new ArrayList<UrgeRepayOrder>());
            put(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode(), new ArrayList<UrgeRepayOrder>());
        }};

        /**
         * 筛选单子
         */
        if (null != urgeRepayOrders && urgeRepayOrders.size() > 0) {
            List<User> borrowUserList = cloanUserService.listSelective(new HashMap<String, Object>());
            List<String> borrowUserPhoneList = new ArrayList<String>();
            Map<String,Long> phone2IdMap=new HashMap<String,Long>();
            for(User user : borrowUserList){
                borrowUserPhoneList.add(user.getLoginName());
                phone2IdMap.put(user.getLoginName(),user.getId());
            }

            List<BorrowRepayLog> borrowRepayLogList = borrowRepayLogService.listSelective(new HashMap<String, Object>());
            List<Long> borrowUserIdList = new ArrayList<Long>();
            for(BorrowRepayLog borrowRepayLog : borrowRepayLogList){
                borrowUserIdList.add(borrowRepayLog.getUserId());
            }


            for (UrgeRepayOrder urgeRepayOrder : urgeRepayOrders) {
                if(UrgeRepayOrderModel.STATE_ORDER_SUCCESS.equals(urgeRepayOrder.getState()) || UrgeRepayOrderModel.STATE_ORDER_LOCK.equals(urgeRepayOrder.getState())) {
                    continue;
                }

//                User borrowUser = cloanUserService.findByPhone(urgeRepayOrder.getPhone());
//                if (null == borrowUser) {
                if (!borrowUserPhoneList.contains(urgeRepayOrder.getPhone())) {
                    logger.error("未找到当前借款用户 {}", urgeRepayOrder.getPhone());
                    continue;
                }

//                boolean hasRepay = borrowRepayLogService.existByUserId(borrowUser.getId());
                boolean hasRepay=false;
                if(borrowUserIdList.contains(phone2IdMap.get(urgeRepayOrder.getPhone()))){
                    hasRepay = true;
                }
                boolean isFront = ((urgeRepayOrder.getPenaltyDay() >= Integer.valueOf(frontHandRule[0]) && urgeRepayOrder.getPenaltyDay() <= Integer.valueOf(frontHandRule[1])));
                if (hasRepay && isFront) {
                    if("".equals(urgeRepayOrder.getState()) || "10".equals(urgeRepayOrder.getState())){
                        classifyUrgeRepayMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayOrder);         //无状态或者状态为未分配的催单
                    }else if(!checkUserExist(urgeRepayOrder.getUserId(),classifyUserMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()),classifyVacationUserMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()))){
                        classifyUrgeRepayMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayOrder);        //催单员不存在或者状态异常
                    }

                } else if (!hasRepay && isFront) {
                    if("".equals(urgeRepayOrder.getState()) || "10".equals(urgeRepayOrder.getState())){
                        classifyUrgeRepayMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayOrder);        //无状态或者状态为未分配的催单
                    }else if(!checkUserExist(urgeRepayOrder.getUserId(),classifyUserMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()),classifyVacationUserMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()))){
                        classifyUrgeRepayMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayOrder);       //催单员不存在或者状态异常
                    }
                } else if (hasRepay && !isFront) {
                    if("".equals(urgeRepayOrder.getState()) || "10".equals(urgeRepayOrder.getState())){
                        classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayOrder);      //无状态或者状态为未分配的催单
                    }else if(!checkUserExist(urgeRepayOrder.getUserId(),classifyUserMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()),classifyVacationUserMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()))){
                        classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayOrder);      //催单员不存在或者状态异常
                    } else if(urgeRepayOrder.getPenaltyDay().intValue()%Integer.valueOf(frontHandRule[2]) == 0){
                        classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).add(urgeRepayOrder);      //达到最大持单时间，重新分配
                    }
                } else {
                    if("".equals(urgeRepayOrder.getState()) || "10".equals(urgeRepayOrder.getState())){
                        classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayOrder);        //无状态或者状态为未分配的催单
                    }else if(!checkUserExist(urgeRepayOrder.getUserId(),classifyUserMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()),classifyVacationUserMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()))){
                        classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayOrder);        //催单员不存在或者状态异常
                    } else if(urgeRepayOrder.getPenaltyDay().intValue()%Integer.valueOf(frontHandRule[2]) == 0){
                        classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).add(urgeRepayOrder);        //达到最大持单时间，重新分配
                    }

                }
            }
        }



        if (isDev) {
            logger.info("开始自动分配催收任务,催收数据明细：");
            logger.info("符合[前手新客]数据条数：{}", classifyUrgeRepayMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()).size());
            logger.info("符合[前手老客]数据条数：{}", classifyUrgeRepayMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()).size());
            logger.info("符合[后手新客]数据条数：{}", classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).size());
            logger.info("符合[后手老客]数据条数：{}", classifyUrgeRepayMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).size());
            logger.info("\n\n");
            logger.info("符合[前手新客]用户个数：{}", classifyUserMap.get(UrgeRepayType.FRONT_HAND_NEW_CUSTOMER.getCode()).size());
            logger.info("符合[前手老客]用户个数：{}", classifyUserMap.get(UrgeRepayType.FRONT_HAND_OLD_CUSTOMER.getCode()).size());
            logger.info("符合[后手新客]用户个数：{}", classifyUserMap.get(UrgeRepayType.BACK_HAND_NEW_CUSTOMER.getCode()).size());
            logger.info("符合[后手老客]用户个数：{}", classifyUserMap.get(UrgeRepayType.BACK_HAND_OLD_CUSTOMER.getCode()).size());
            logger.info("\n\n");
        }
        ExecutorService executorService = null;
        String quartzRemark = null;
        try {
            /**
             * 开启4个线程
             */
            executorService = Executors.newFixedThreadPool(classifyUrgeRepayMap.size());
            final CountDownLatch latch=new CountDownLatch(4);
            for (UrgeRepayType urgeRepayType : UrgeRepayType.getGroupClassify()) {
                final List<UrgeRepayOrder> urgeRepayOrders1 = classifyUrgeRepayMap.get(urgeRepayType.getCode());
                final List<UrgeRepayUserAlloca> users = classifyUserMap.get(urgeRepayType.getCode());
                //单子和催收员都要有数据
                int usize = users.size();
                int osize = urgeRepayOrders1.size();
                if (usize > 0 && osize > 0) {
                    //分配
                    rectificateProcess(executorService, urgeRepayOrders1, urgeRepayOrderService,urgeRepayOrderFlowService, users,latch);
                } else {
                    logger.info("当前没有要进行分配的逾期单或者催收人员，osize={}，usize={}", osize, usize);
                    latch.countDown();
                    continue;
                }
            }
            //当前线程挂起等待
            latch.await(9, TimeUnit.HOURS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("定时扣款计数器出错", e);
        }
        //线程池中所有任务都执行完毕后，线程池关闭
        executorService.shutdown();
        quartzRemark = "催收单自动分配完毕,当前耗时 " + (System.currentTimeMillis() - startTime) + "ms";
        return quartzRemark;
    }

    private Boolean checkUserExist(Long userId, List<UrgeRepayUserAlloca> users, List<UrgeRepayUserAlloca> vacationUsers){
        Boolean ret=false;
        for(UrgeRepayUserAlloca user : users){
            if(userId!=null && user.getUserId()!=null && userId.longValue()==user.getUserId().longValue()){
                ret=true;
                break;
            }
        }
        for(UrgeRepayUserAlloca user : vacationUsers){
            if(userId!=null && user.getUserId()!=null && userId.longValue()==user.getUserId().longValue()){
                ret=true;
                break;
            }
        }
        return ret;
    }


    /**
     * 分配均衡时，数量较大，多线程代劳
     *
     * @param executorService
     * @param urgeRepayOrders
     * @param urgeRepayOrderService
     * @param users
     */
    private void rectificateProcess(ExecutorService executorService, final List<UrgeRepayOrder> urgeRepayOrders, final UrgeRepayOrderService urgeRepayOrderService, final UrgeRepayOrderFlowService urgeRepayOrderFlowService, final List<UrgeRepayUserAlloca> users, final CountDownLatch latch) {
        final int osize = urgeRepayOrders.size();
        final int usize = users.size();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<String> usersIndexList=new ArrayList<String>();
                int m=0;
                for (int i = 0; i < osize; i++) {
                    UrgeRepayOrder order = urgeRepayOrders.get(i);
                    if(m%usize==0){
                        //生成一组随机整数
                        usersIndexList= gerateIntArray(usize);
                    }

                    if(UrgeRepayOrderModel.STATE_ORDER_SUCCESS.equals(urgeRepayOrderService.getById(order.getId()).getState())){
                        continue;
                    }
                    UrgeRepayUserAlloca urgeRepayUserAlloca = users.get(Integer.parseInt(usersIndexList.get(m%usize)));
                    Map<String, Object> params = new HashMap<>();
                    params.put("userId", urgeRepayUserAlloca.getUserId());
                    params.put("userName", urgeRepayUserAlloca.getRealName());
                    if("".equals(order.getState()) || UrgeRepayOrderModel.STATE_ORDER_PRE.equals(order.getState())){
                        params.put("state",UrgeRepayOrderModel.STATE_ORDER_WAIT);
                    }
                    params.put("id",order.getId());
                    urgeRepayOrderService.orderAllotUser(params);

                    UrgeRepayOrderFlow record=new UrgeRepayOrderFlow();
                    record.setDueId(order.getId());
                    record.setBorrowId(order.getBorrowId());
                    record.setUserId(urgeRepayUserAlloca.getUserId());
                    record.setUserName(urgeRepayUserAlloca.getRealName());
                    record.setCreateTime(DateUtil.getNow());
                    record.setState(order.getState());
                    record.setPenaltyDay(order.getPenaltyDay());
                    urgeRepayOrderFlowService.addflowRecord(record);
                    m++;
                }
                latch.countDown();
            }
        });
    }

    private List<String> gerateIntArray(int length){
        List<String> retArray=new ArrayList<String>();
        for(int i=0;retArray.size()<length;i++){
            String str =String.valueOf((int)(Math.random() * (length)));
            if(!retArray.contains(str)){
                retArray.add(str);
            }
        }
        return retArray;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QuartzInfoService quartzInfoService = (QuartzInfoService) BeanUtil.getBean("quartzInfoService");
        QuartzLogService quartzLogService = (QuartzLogService) BeanUtil.getBean("quartzLogService");
        // 查询当前任务信息
        QuartzInfo quartzInfo = quartzInfoService.findByCode("urgeUserAlloca");
        //@remarks:只有启用状态才会调用.@date:20170814 @author:nmnl
        if (null == quartzInfo) {
            logger.info(" [定时任务][扣款还款] 启动失败:原因未启用! ");
            return;
        }
        Map<String, Object> qiData = new HashMap<>();
        qiData.put("id", quartzInfo.getId());

        QuartzLog quartzLog = new QuartzLog();
        quartzLog.setQuartzId(quartzInfo.getId());
        quartzLog.setStartTime(DateUtil.getNow());
        try {
            String remark = urgeUserAlloca();

            quartzLog.setTime(DateUtil.getNow().getTime() - quartzLog.getStartTime().getTime());
            quartzLog.setResult("10");
            quartzLog.setRemark(remark);
            qiData.put("succeed", quartzInfo.getSucceed() + 1);
        } catch (Exception e) {
            quartzLog.setResult("20");
            qiData.put("fail", quartzInfo.getFail() + 1);
            logger.error(e.getMessage(), e);
        } finally {
            logger.info("保存定时任务日志");
            quartzLogService.save(quartzLog);
            quartzInfoService.update(qiData);
        }
    }
}
