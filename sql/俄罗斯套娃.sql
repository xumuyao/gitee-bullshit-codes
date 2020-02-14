select
	res_url,
	sum(bid) as bbid,
	min(resource_code) resource_code,
	min(l_level_menu) l_level_menu,
	min(menu_type) menu_type,
	min(biz_code) biz_code,
	sum(flag) / count(flag) flag
from
	(
		select
			a.*, b.id as bid
		from
			(
				(
					select
						*
					from
						(
							(select * from user_resource) e
							left join (
								select
									user_role,
									resource_code as fresource_code,
									biz_code as fbiz_value
								from
									user_role_resource
							) f on f.fresource_code = e.resource_code
						)
				) a
				left join (
					select
						id
					from
						(
							(
								select distinct
									m.user_role as buser_role
								from
									(
										select
											user_role
										from
											user_role
										where
											aaaa_num = #{userid} union all
											(
												select
													k.user_role as user_role
												from
													(
														(
															select
																user_role,
																user_group
															from
																user_group_role
														) k
														left join (
															select
																user_group
															from
																user_info_group
															where
																aaaa_num = #{userid} )j on k.user_group=j.user_group)  where j.user_group is not null)) m where buser_role in (select user_role from user_role_duty where status=1  )
														) c
														left join (
															select
																id,
																user_role as duser_role
															from
																user_role_duty
														) d on c.buser_role = d.duser_role
													)
												where
													id is not null
											) b on a.user_role = b.id
									)
							) k
							group by
								res_url