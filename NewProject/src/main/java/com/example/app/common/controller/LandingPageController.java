package com.example.app.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/land")
public class LandingPageController {
	
	 @RequestMapping(method=RequestMethod.GET)
	 public String redirect() {
	        return "landingPage";
	    }
}
