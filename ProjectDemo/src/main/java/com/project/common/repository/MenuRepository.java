package com.project.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.common.model.db.MenuEntity;
import com.project.common.model.db.MenuEntityId;

public interface MenuRepository extends JpaRepository<MenuEntity, MenuEntityId> {

}
