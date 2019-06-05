function isHidden(label,rolelist){
	var role_id = "11e8-8fbc-a36307c7-a8ff-0ddbf53d8f41";
	var role_id2 = "11e8-8fbc-87c18432-a8ff-0ddbf53d8f41";
	var role_id3 = "11e8-8fbc-81a863c1-a8ff-0ddbf53d8f41";
	var role_id5 = "11e8-93da-ec6e86a4-b948-93f67372d36f";
	var role_id6 = "11e8-8fbc-72366130-a8ff-0ddbf53d8f41";
	var role_id7 = "11e8-b6fa-9f8346c0-96f1-b5c94816698a";
	var role_id8 = "11e8-cde4-4231619d-85b0-b54ddee67fdc";
	var flag = false;
	if(rolelist.indexOf(role_id2)>-1||rolelist.indexOf(role_id3)>-1||rolelist.indexOf(role_id5)>-1||rolelist.indexOf(role_id6)>-1||rolelist.indexOf(role_id7)>-1||rolelist.indexOf(role_id8)>-1){//副处及以上能查看
		flag = false;
	}else if(rolelist.indexOf(role_id4)>-1||rolelist.indexOf(role_id)>-1){
		flag = true;
	}
	if(label=="提交申请"||label==null){//自己不能看考核结果
		flag = true;
	}
	if(authorid==userid){//自己不能看考核结果
		flag = true;
	}
	return flag;
}