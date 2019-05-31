# 一度以为是DBA来写Java了，这么长SQL，怎么复用？怎么维护？
# 蛋疼的是，一个XML里面大量这样的SQL,几千行谁看得懂？
# 反例教材
select

demand_id,
demand_code,
demand_name,
demand_priority,
demand_priority_str,
receiver_addr,
receiver_name,
business_area,
demand_type,
demand_type_str,
demand_status,
demand_status_str,
expected_work_time,
created,
created_name,
created_by​

 from
  (select
    aa.demand_id,
    aa.demand_code,
    aa.demand_name,
    aa.demand_priority,
    aa.demand_priority_str,
    aa.business_area,
    aa.demand_type,
    aa.demand_type_str,
    aa.demand_status,
    aa.demand_status_str,
    aa.acceptance_addr,
    aa.created_by,
    aa.created_name,
    aa.created,
    aa.receiver_addr,
    aa.receiver_name,
    (select count(1)
     from dm_demand_t ddt
     where ddt.demand_id = aa.demand_id
           and (ddt.demand_status = 1 and
              (ddt.created_by= #{report.loginEmail} or
               ddt.receiver_addr = #{report.loginEmail} or
               ddt.pm_email_addr = #{report.loginEmail} or #{report.pmoFlag} = 'Y'))
           or (ddt.demand_status = 7 and
               (ddt.created_by= #{report.loginEmail} or
                ddt.receiver_addr = #{report.loginEmail} or
                ddt.pm_email_addr = #{report.loginEmail} or #{report.pmoFlag} = 'Y'))
    )                    as enable_edit,
    (select count(1)
    from dm_demand_t ddt
    where ddt.demand_id = aa.demand_id
    and (ddt.demand_status = 1 and
    (ddt.created_by= #{report.loginEmail} or
    ddt.receiver_addr = #{report.loginEmail} or
    ddt.pm_email_addr = #{report.loginEmail} or #{report.pmoFlag} = 'Y'))
    or (ddt.demand_status = 7 and
    (ddt.created_by= #{report.loginEmail} or
    ddt.receiver_addr = #{report.loginEmail} or
    ddt.pm_email_addr = #{report.loginEmail} or #{report.pmoFlag} = 'Y'))
    )                    as enable_close,
    (select count(1)
     from dm_demand_t ddt
     where ddt.demand_id =aa.demand_id
      and (ddt.demand_status= 3 and
           (ddt.receiver_addr= #{report.loginEmail} or ddt.pm_email_addr =#{report.loginEmail} or #{report.pmoFlag} = 'Y'))
    )                    as enable_project,
    (select count(1)
     from dm_demand_t ddt
     where ddt.demand_id=aa.demand_id
         and(ddt.demand_status=8
         and(ddt.created_by=#{report.loginEmail} or locate(#{report.loginEmail},aa.acceptance_addr)>0))
    )as enable_accept,
    aa.total_time as expected_work_time,
&#45;&#45;     concat(ifnull(aa.total_person,0),'/',ifnull(aa.total_time,0)) as expected_work_time,
    group_concat(aa.dept_id)      as lev1_depts,
    group_concat(aa.lev2_dept_id) as lev2_depts
   from
    (SELECT
         dt.demand_id,
         dt.demand_code,
         dt.demand_name,
         dt.demand_priority,
         cd1.display AS demand_priority_str,
         dt.business_area,
         dt.demand_type,
         cd3.display AS demand_type_str,
         dt.demand_status,
         cd2.display AS demand_status_str,
         dt.created_by,
         ts.name as created_name,
         dt.created,
    dt.receiver_addr,
    ts2.name AS receiver_name,
       (case dt.demand_type
          when 1 then convert(sum(ifnull(dtt.work_time,0)),decimal(10,1))
          when 2 then convert(sum(ifnull(dtt.work_time,0)),decimal(10,1))
          when 3 then
             (select convert(sum(aa.bus_time),decimal(10,1))
               from
             (select
               tt.demand_id,
               op.id as operate_id,
               ifnull(op.operation_cycle, 0) * (ifnull(tt.proportion, 0) / 100) * ifnull(op.hr_config, 0) as bus_time
              from dm_demand_time_t tt
               inner join dm_demand_operation_t op on op.status=1 AND op.ident_code = 0
                  and tt.project_code=op.project_code and tt.demand_id=op.demand_id
                where tt.status = 1 AND tt.ident_code = 0) aa
              where aa.demand_id =dt.demand_id)
         else 0
         end
        ) as total_time,
         (select count(distinct st.user_email_addr)
          from dm_demand_source_t st
          where st.status = 1 and st.demand_id = dt.demand_id) as total_person,
         ds.dept_id,
         ds.lev2_dept_id,
         group_concat(dat.acceptance_incharge_addr)      as     acceptance_addr
     FROM dm_demand_t dt
       left join ts_user ts on ts.email_addr=dt.created_by
    left join ts_user ts2 on ts2.email_addr=dt.receiver_addr
       left join (select
                   st.demand_id,
                   tu.m_dept_des_level2 as dept_id,
                   tu.m_dept_des_level3 as lev2_dept_id
                  from
                    dm_demand_source_t st
                  left join ts_user tu on tu.email_addr=st.user_email_addr
                   where st.status = 1
                  ) ds on ds.demand_id = dt.demand_id
    left join dm_code_dict cd1 ON cd1.ident_key='priority' and cd1.code = dt.demand_priority
    left join dm_code_dict cd2 ON cd2.ident_key='demandStatus' and cd2.code = dt.demand_status
    left join dm_code_dict cd3 ON cd3.ident_key='demandType' and cd3.code = dt.demand_type
    left join dm_demand_acceptance_t dat on dat.status = 1 and dat.demand_id = dt.demand_id
    left join dm_demand_time_t dtt on dtt.status = 1 and dtt.demand_id = dt.demand_id AND dtt.ident_code = 0
     where 1 = 1
       and dt.created between if(#{report.beginDate}=null,'1000-01-01',if(#{report.beginDate}='','1000-01-01',#{report.beginDate}))
       and if(#{report.endDate}=null,'9999-01-01',if(#{report.endDate}='','9999-01-01',#{report.endDate}))
   <if test="report.businessArea !=null and report.businessArea!=''">
      and dt.business_area = #{report.businessArea}
   </if>

   <if test="report.demandName !=null and report.demandName!=''">
      and locate(#{report.demandName}, dt.demand_name) > 0
   </if>
   <if test="report.demandCode !=null and report.demandCode!=''">
      and locate(#{report.demandCode}, dt.demand_code) > 0
   </if>
     group by
         dt.demand_id,
         dt.demand_code,
         dt.demand_name,
         dt.demand_priority,
         demand_priority_str,
         dt.business_area,
         dt.demand_type,
         demand_type_str,
         dt.demand_status,
         demand_status_str,
         dt.created_by,
         dt.created,
         created_name,
         total_person,
         ds.dept_id,
         ds.lev2_dept_id
    ) aa
  group by aa.demand_id,
         aa.demand_code,
         aa.demand_name,
         aa.demand_priority,
         aa.demand_priority_str,
         aa.business_area,
         aa.demand_type,
         aa.demand_type_str,
         aa.demand_status,
         aa.demand_status_str,
         aa.created_by,
         aa.created,
         aa.created_name,
         aa.acceptance_addr,
         aa.receiver_addr,
         aa.receiver_name,
         enable_edit,
         enable_accept,
         enable_project,
         enable_close) bb
    where 1=1
    <if test="report.demandType !=null and report.demandType!=''">
       and bb.demand_type_str = #{report.demandType}
    </if>
    <if test="report.demandPriority !=null and report.demandPriority!=''">
       and bb.demand_priority_str = #{report.demandPriority}
    </if>
    <if test="report.demandStatus !=null and report.demandStatus!=''">
       and bb.demand_status_str=#{report.demandStatus}
    </if>
    <if test="report.demandPerson !=null and report.demandPerson!=''">
       and locate(#{report.demandPerson},bb.receiver_addr)>0
    </if>
    <if test="report.lev1Dept !=null and report.lev1Dept!=''">
       and locate(concat(',',#{report.lev1Dept},','),concat(',',bb.lev1_depts,','))>0
    </if>
    <if test="report.lev2Dept !=null and report.lev2Dept!=''">
       and locate(concat(',',#{report.lev2Dept},','),concat(',',bb.lev2_depts,','))>0
    </if>

    ORDER BY bb.created DESC