package com.example.app.common.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.common.repository.MenuRepository;
import com.example.app.common.repository.OverviewRepository;
import com.example.app.common.service.RepositoryService;

@Service
public class RepositoryServiceImpl implements RepositoryService{

	@Autowired
	private OverviewRepository overviewRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	public RepositoryServiceImpl() {
		
	}
}
