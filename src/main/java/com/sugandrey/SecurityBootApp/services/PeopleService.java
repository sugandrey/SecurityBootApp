package com.sugandrey.SecurityBootApp.services;

import com.sugandrey.SecurityBootApp.models.Person;
import com.sugandrey.SecurityBootApp.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository repository;

    @Autowired
    public PeopleService(final PeopleRepository repository) {
        this.repository = repository;
    }

    public Person findByUserName(final String username) {
        final Optional<Person> user = repository.findByUserName(username);
        return user.orElse(null);
    }

}
