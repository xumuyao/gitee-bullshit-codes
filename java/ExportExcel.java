		/**
         * 一个poi导出excel 数据库sql不超过1G 相应的sql也非常厉害 一条sql 7,8个表 select 后 一堆子查询
         * 涉及的几个表 数据量也很少 没有3000条以上的 
         * 线上这段执行时长2小时
		 * 代码缩进是仿照他的
         */
		@RequestMapping(value="/export")
		public ModelAndView export(Page page) throws Exception{
			ModelAndView mv = new ModelAndView();
			Map<String,Map> Map = new HashMap<String,Map>();
			
			
			List<PageData> aList = aService.query(page);
			
			List<PageData> bList = bService.query(page);
			
			a:
			for(int i=0;i<bList.size();i++){
			PageData b = bList.get(i);
			PageData a = aService.findB(b);
			List<PageData> cList = cService.query(b);
			for(int j=0;j<aList.size();j++){
				String hhh = varList.get(i).getString("hhh");
				if(xxxx){
					if(yyyy){
						aList.get(j).put("aa","aa");
						....
						cList.add(aList.get(j))
					}else{
						String[] hhhArr = hhh.split(",");
						b:
						for(int k=0;k<hhhArr.length;k++){
							for(int l=0;l<cList.size();l++){
								if(hhhArr[k].contains(cList.get(l).getString("hhh")==null?xxxx太长了xxxx)){
									cList.get(l).put("xx","xx")
									break b;
								}	
							}
						}
					}
				}
				PageDate d = new PageDate();
				d.put("hhh","xxxx");
				///这里很多赋值
				...
				d = dService.query(d);
				if((Double)d.get("jine")>0){
					aList.get(i).put("xxxx",d.get(xxx));
					aList.get(i).put("xxxx",d.get(xxx));
					cList.add(aList.get(i));
				}
			}
			///没错就是大写的
			List<String> StrList = new ArrayList<String>();
			c:for(int q=0;q<cList.size();q++){
				for(int w=0;w<StrList.size();w++){
					if(zzzzz){
						continue c;
					}
				}
				PageDate e = new PageDate();
				e.put("xxx","xxxx");
				///这里很多赋值
				...
				List<PageData> eList = aService.queryE(e);
				for(int r=0;r<eList.size();r++){
					PageDate f = new PageDate();
					f.put("xxx","xxxx");
					///这里很多赋值
					...
					PageDate g = new PageDate();
					g.put("xxx","xxxx");
					g = bService.queryG(g);
					
					f.put("xxx",g.get("xxx"));
					///这里很多赋值
					...
					cList.add(f);
				}
				
				Collections.sort(hxList, new Comparator(){
					///写个排序
					...
				});
				
				///接下一大串是 poi的
			}
			}
			return mv;
		}