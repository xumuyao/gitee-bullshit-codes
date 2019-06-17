 private String getStr(String queryStr,String url) {
    	String str="";
		if (StringUtils.isNotBlank(queryStr)) {
			if (queryStr.contains("state=CLICK")) {
				return str = "领取任务";
			}
			if (queryStr.contains("state=CLOSE")) {
				return str = "放弃任务";
			}
			if (queryStr.contains("state=RECEIVE")) {
				return str = "领取奖励";
			}
		}
		if (url.contains("/a/u/task/taskList")) {
			return str = "任务列表";
		}
		if (url.contains("/a/u/task/listByPage")) {
			return str = "55";
		}
		if(url.contains("/a/u/task/f")) {
			return str= "22";
		}
		if(url.contains("/a/u/task/m")) {
			return str= "00";
		}
		if (url.contains("/a/u/n/info")) {
			return str = "查看余额";
		}
		if (url.contains("/a/u/j/insert")) {
			return str = "提现";
		}
		if (url.contains("/a/u/gg/verifyCode")) {
			return str = "提现验证";
		}
		if (url.contains("/a/u/ttt/listByPage")) {
			return str = "transactions";
		}
		if (url.contains("/a/u/t/info")) {
			return str = "推广信息";
		}
		if (url.contains("/a/u/ttt/listByPage")) {
			return str = "邀请列表";
		}
		if (url.contains("/a/u/t/listByPage")) {
			return str = "消息列表";
		}
		if (url.contains("/a/u/t/listByPage")) {
			return str = "详情页-activity";
		}
		if(url.contains("/a/u/taskhigh/get")) {
			return str = "9-高额";
		}
		if(url.contains("/a/u/taskhigh/giveUp")) {
			return str = "6-高额";
		}
		if(url.contains("/a/u/l/p")) {
			return str = "2";
		}
		if(url.contains("/a/u/x/upload")) {
			return str = "1";
		}
		if(url.contains("/a/u/user/login")) {
			return str = "登录";
		}
    	return str;
    }