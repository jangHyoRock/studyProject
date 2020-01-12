package com.example.app.common.service;

import java.util.List;

import com.example.app.common.model.db.Person;

public interface PersonService {
	
	Person createPerson(Person person);

	void deletePerson(Long id);

	List<Person> getAllPersons();

}
