package com.example.app.common.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.common.model.db.Person;
import com.example.app.common.repository.PersonRepository;
import com.example.app.common.service.PersonService;

@Service
public class PersonServiceImp  implements PersonService {
	
	 @Autowired // 1
	 private PersonRepository personRepository;

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person); // 2
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id); // 3
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll(); // 4
    }

}
