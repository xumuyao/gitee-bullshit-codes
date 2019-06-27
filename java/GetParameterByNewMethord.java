
/*
花式解析httpservetrequest请求参数
*/
public Object fileDownload(HttpServletRequest request, HttpServletResponse response) {		
		String filePathTo = request.getQueryString();
		String[] fieldTo = filePathTo.split("&");

		int appKeyIndex = fieldTo[0].indexOf("=");
		String appKeyName = fieldTo[0].substring(0, appKeyIndex);

		int signIndex = fieldTo[1].indexOf("=");
		String signName = fieldTo[1].substring(0, signIndex);

		int reqTimeIndex = fieldTo[2].indexOf("=");
		String reqTimeName = fieldTo[2].substring(0, reqTimeIndex);
		if ((!"appKey".equals(appKeyName) && !"appKey".equals(signName) && !"appKey".equals(reqTimeName))
				|| (!"sign".equals(appKeyName) && !"sign".equals(signName) && !"sign".equals(reqTimeName))
				|| (!"reqTime".equals(appKeyName) && !"reqTime".equals(signName)
				&& !"reqTime".equals(reqTimeName))) {
			outerJson.put("status", "fail");
			outerJson.put("returnCode", "030");
			outerJson.put("returnInfo", "请求成功");
			outerJson.put("msg", "参数名称无效");
			outerJson.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			outerJson.put("datas", innerJson);
			return outerJson;
		}
    }