#方吗？
select 
yingh.repay_time,
count(yingh.borrow_id) "当日应还笔数",
count(yih.borrow_id) "当日正常还款数",
count(yingh.borrow_id) - count(yih.borrow_id) "当日未还逾期笔数",
count(yqyih.borrow_id) "当日逾期已还",
count(yingh.borrow_id) - count(yih.borrow_id) - count(yqyih.borrow_id) "当日逾期剩余未还",
count(yq1.borrow_id) "逾期一天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) "逾期二天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) "逾期三天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) "逾期四天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) "逾期五天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) - count(yq6.borrow_id) "逾期六天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) - count(yq6.borrow_id) - count(yq7.borrow_id) "逾期七天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) - count(yq6.borrow_id) - count(yq7.borrow_id) - count(yq8_15.borrow_id) "逾期8-15天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) - count(yq6.borrow_id) - count(yq7.borrow_id) - count(yq8_15.borrow_id) - count(yq16_30.borrow_id) "逾期16-30天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) - count(yq6.borrow_id) - count(yq7.borrow_id) - count(yq8_15.borrow_id) - count(yq16_30.borrow_id)- count(yq31_60.borrow_id) "逾期31-60天未还",
count(yq1.borrow_id) - count(yq2.borrow_id) - count(yq3.borrow_id) - count(yq4.borrow_id) - count(yq5.borrow_id) - count(yq6.borrow_id) - count(yq7.borrow_id) - count(yq8_15.borrow_id) - count(yq16_30.borrow_id)- count(yq31_60.borrow_id)- count(yq60m.borrow_id) "逾期60天以上未还"

from cl_borrow_repay yingh
LEFT JOIN cl_borrow_repay yih on yingh.borrow_id = yih.borrow_id and yih.state = 10 and yingh.user_id = yih.user_id and yih.penalty_day = 0
#LEFT JOIN cl_borrow_repay wh on yingh.borrow_id = wh.borrow_id and wh.state = 20 and yingh.user_id = wh.user_id
LEFT JOIN(
select bp_yh.borrow_id,br.repay_time from cl_borrow_progress bp_yq join (select borrow_id from cl_borrow_progress where state = 40) bp_yh on bp_yq.state = 50 and bp_yq.borrow_id = bp_yh.borrow_id
LEFT JOIN cl_borrow_repay br on bp_yq.borrow_id =  br.borrow_id
 where bp_yq.state = 50 and bp_yq.borrow_id = bp_yh.borrow_id
) yqyih on yqyih.borrow_id = yingh.borrow_id and yqyih.repay_time = yingh.repay_time
LEFT JOIN(
select br.borrow_id,br.user_id from cl_borrow_repay br LEFT JOIN cl_borrow_progress bp on br.borrow_id = bp.borrow_id and br.user_id = bp.user_id
where bp.state = 50 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -1 DAY), '%Y-%m-%d')
) yq1 on yingh.borrow_id = yq1.borrow_id and yq1.user_id = yingh.user_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -2 DAY), '%Y-%m-%d')
)yq2 on yq2.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -3 DAY), '%Y-%m-%d')
)yq3 on yq3.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -4 DAY), '%Y-%m-%d')
)yq4 on yq4.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -5 DAY), '%Y-%m-%d')
)yq5 on yq5.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -6 DAY), '%Y-%m-%d')
)yq6 on yq6.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -7 DAY), '%Y-%m-%d')
)yq7 on yq7.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -15 DAY), '%Y-%m-%d') 
and DATE_FORMAT(bp.create_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -8 DAY), '%Y-%m-%d')
)yq8_15 on yq8_15.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -30 DAY), '%Y-%m-%d') 
and DATE_FORMAT(bp.create_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -16 DAY), '%Y-%m-%d')
)yq16_30 on yq16_30.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -60 DAY), '%Y-%m-%d') 
and DATE_FORMAT(bp.create_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -31 DAY), '%Y-%m-%d')
)yq31_60 on yq16_30.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,bp.borrow_id,br.repay_time,bp.create_time from cl_borrow_repay br LEFT JOIN
cl_borrow_progress bp on br.borrow_id = bp.borrow_id 
where bp.state = 40 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') > DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -60 DAY), '%Y-%m-%d') 
)yq60m on yq60m.borrow_id =yq1.borrow_id
GROUP BY yingh.repay_time;




#优化一点点后，计算公式之类的方代码里面执行
select
DATE_FORMAT(yingh.repay_time,'%Y-%m-%d') repayTime,
count(yingh.borrow_id) shouldRepayToday,
count(yih.borrow_id) normalRepayToday,
count(yqyih.borrow_id) overdueRepayToday,
count(dthz.borrow_id) badCount,
count(yq1.borrow_id)  overdueRepay1,
count(yq1yh.borrow_id)  overdueRepay1ed,
count(yq2.borrow_id),
count(yq3.borrow_id),
count(yq4.borrow_id),
count(yq5.borrow_id),
count(yq6.borrow_id),
count(yq7.borrow_id),
count(yq8_15.borrow_id),
count(yq16_30.borrow_id),
count(yq31_60.borrow_id),
count(yq60m.borrow_id),
count(yqyh1_3.borrow_id) repayCount1and3,
count(yqyh4_15.borrow_id) repayCount4and15
from cl_borrow_repay yingh
LEFT JOIN cl_borrow_repay yih on yingh.borrow_id = yih.borrow_id and yih.state = 10 and yingh.user_id = yih.user_id and yih.penalty_day = 0
LEFT JOIN(
select bp_yh.borrow_id,bp_yh.repay_time from cl_borrow_progress bp_yq join (select borrow_id,repay_time from cl_borrow_repay where state =10) bp_yh on bp_yq.state = 50 and bp_yq.borrow_id = bp_yh.borrow_id
) yqyih on yqyih.borrow_id = yingh.borrow_id and yqyih.repay_time = yingh.repay_time
LEFT JOIN(
select borrow_id from cl_borrow_progress where state = 90
)dthz on yingh.borrow_id = dthz.borrow_id
LEFT JOIN(
select br.borrow_id,br.user_id from cl_borrow_repay br LEFT JOIN cl_borrow_progress bp on br.borrow_id = bp.borrow_id and br.user_id = bp.user_id
where bp.state = 50 and DATE_FORMAT(bp.create_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -1 DAY), '%Y-%m-%d')
) yq1 on yingh.borrow_id = yq1.borrow_id and yq1.user_id = yingh.user_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -1 DAY), '%Y-%m-%d')
) yq1yh on yingh.borrow_id = yq1yh.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -2 DAY), '%Y-%m-%d')
)yq2 on yq2.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -3 DAY), '%Y-%m-%d')
)yq3 on yq3.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -4 DAY), '%Y-%m-%d')
)yq4 on yq4.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -5 DAY), '%Y-%m-%d')
)yq5 on yq5.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -6 DAY), '%Y-%m-%d')
)yq6 on yq6.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') = DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -7 DAY), '%Y-%m-%d')
)yq7 on yq7.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -15 DAY), '%Y-%m-%d')
and DATE_FORMAT(brl.repay_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -8 DAY), '%Y-%m-%d')
)yq8_15 on yq8_15.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -30 DAY), '%Y-%m-%d')
and DATE_FORMAT(brl.repay_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -16 DAY), '%Y-%m-%d')
)yq16_30 on yq16_30.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -60 DAY), '%Y-%m-%d')
and DATE_FORMAT(brl.repay_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -31 DAY), '%Y-%m-%d')
)yq31_60 on yq16_30.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') > DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -60 DAY), '%Y-%m-%d')
)yq60m on yq60m.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -3 DAY), '%Y-%m-%d')
and DATE_FORMAT(brl.repay_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -1 DAY), '%Y-%m-%d')
)yqyh1_3 on yqyh1_3.borrow_id =yq1.borrow_id
LEFT JOIN(
select br.borrow_id brborrow_id,brl.borrow_id,br.repay_time from cl_borrow_repay br LEFT JOIN
cl_borrow_repay_log brl on br.borrow_id = brl.borrow_id
where DATE_FORMAT(brl.repay_time, '%Y-%m-%d') <= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -15 DAY), '%Y-%m-%d')
and DATE_FORMAT(brl.repay_time, '%Y-%m-%d') >= DATE_FORMAT(DATE_SUB(br.repay_time, INTERVAL -4 DAY), '%Y-%m-%d')
)yqyh4_15 on yqyh4_15.borrow_id =yq1.borrow_id
where DATE_FORMAT(yingh.repay_time, '%Y-%m-%d') < DATE_FORMAT('2019-04-20', '%Y-%m-%d')
GROUP BY yingh.repay_time