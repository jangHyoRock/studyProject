package com.example.app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.common.model.db.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
