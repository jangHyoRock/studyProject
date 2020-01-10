package com.project.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/index")
public class OverviewController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String Home(Model model) {
		model.addAttribute("msg", "Hello World INDEX");
		return "form";
	}
}
