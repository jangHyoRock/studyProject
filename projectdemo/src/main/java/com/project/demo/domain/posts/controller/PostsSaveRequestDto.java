package com.project.demo.domain.posts.controller;

import com.project.demo.domain.posts.Posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsSaveRequestDto {
	
	private String title;
	private String content;
	private String author;
	
	public Posts toEntity() {
		return Posts.builder()
					.title(title)
					.content(content)
					.author(author)
					.build();
	}
}
