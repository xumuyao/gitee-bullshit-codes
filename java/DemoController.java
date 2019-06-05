package com.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 三年前接手的一个政府网站项目，真假不知，据说是一个两年web开发经验的程序员写的， 用的spring系列还是3.x版本，前端还是jsp，可能是老项目。
 * 主要特点是：实体类中不写注释，数据库表中也没有注释，命名经常使用英文拼音混合，而且还可能是简写(grzlChangeStatus:个人资料变更状态)
 * 
 * 挖坑不填坑，菊花塞电灯
 */
@Controller
public class DemoController {
	/**
	 * 这个方法一共有800多行，有30多个if或else if的条件判断,只写代表性的几个
	 * 
	 * 具体方法名称记不得，逻辑是这样,前端jsp提交一个请求过来，界面中除了一些必填信息外，还有有5个下拉框，后台判断各种组合， 
	 * 分别往request里面塞入不同的对象，返回的页面视图也可能不同。
	 */
	@RequestMapping("/submit")
	public String template(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String param1 = request.getParameter("param1");
		String param2 = request.getParameter("param2");
		String param3 = request.getParameter("param3");
		String param4 = request.getParameter("param4");
		String param5 = request.getParameter("param5");
		// 你没看错，各种状态全部存字串，而且判断的时候不考虑空指针问题
		if (param1.equals("已消毒")) {
			// 逻辑
			request.setAttribute("attr", "value");
			return "jsp1";
		} else if (param1.equals("已消毒") && param2.equals("1.8米以上")) {
			// 逻辑
			request.setAttribute("attr", "value");
			// 你没看错，可能又是另一个视图
			return "jsp2";
			// 你没看错，还可能出现多个条件，|| 和 &&
		} else if (param1.equals("已消毒") && param2.equals("2米以上") || param3.equals("缅甸")) {
			// 逻辑
			request.setAttribute("attr", "value");
			return "jsp3";
			// 你没看错，因为他的状态或选项没有用数字类型,所以不能用 > <　之类的来判断，要列举所有状态
		} else if (param1.equals("未进站") || param1.equals("已进站") || param1.equals("已消毒") && param2.equals("2米以上")) {
			request.setAttribute("attr", "value");
			return "jsp3";
			//你没看错，还会出现　true==true 或false==false这样的代码
		}else if(param4.equals("禁止入境")==true && param5.equals("农用车")) {
			request.setAttribute("attr", "value");
			return "jsp4";
			//你没看错，还会出现逻辑非，而且，要先判断什么条件，从来不用括号包起来，要理他的逻辑，还要先理条件判断优先级
		}else if(param1.equals("未进站") || param1.equals("已进站") || param1.equals("已消毒") && param2.equals("2米以上") && !param3.equals("缅甸")) {
			request.setAttribute("attr", "value");
			return "jsp5";
		}
		//省略剩下的30多个类似的判断
		else {
			return "default";
		}
	}
}
