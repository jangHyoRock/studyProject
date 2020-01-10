package com.project.common.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.common.repository.MenuRepository;
import com.project.common.repository.OverviewRepository;
import com.project.common.service.RepositoryService;

@Service
public class RepositoryServiceImpl implements RepositoryService{

	@Autowired
	private OverviewRepository overviewRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	public RepositoryServiceImpl() {
		
	}
}
