package com.project.demo.domain.posts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.demo.domain.posts.PostsRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class WebRestController {
	private PostsRepository postsRepository;

	@GetMapping("/hello")
	public String hello() {
		return "HelloWorld";
	}

	/*
	 * @PostMapping("/") public void savePosts(@RequestBody PostsSaveRequestDto dto)
	 * { postsRepository.save(dto.toEntity()); }
	 */

	
	  public WebRestController(PostsRepository postsRepository) {
		  System.out.println(">>>postsRepository "+postsRepository);
		  this.postsRepository = postsRepository; 
	  }
	 
}
