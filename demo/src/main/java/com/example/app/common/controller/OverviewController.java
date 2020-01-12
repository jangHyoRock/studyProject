package com.example.app.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/form")
public class OverviewController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String Home(Model model) {
		model.addAttribute("msg", "Hello World FormPage");
		return "form";
	}
}
