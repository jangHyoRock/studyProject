package com.example.app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.common.model.db.MenuEntity;
import com.example.app.common.model.db.MenuEntityId;

public interface MenuRepository extends JpaRepository<MenuEntity, MenuEntityId> {

}
