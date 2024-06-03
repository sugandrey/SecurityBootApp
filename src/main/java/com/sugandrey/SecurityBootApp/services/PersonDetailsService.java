package com.sugandrey.SecurityBootApp.services;

import com.sugandrey.SecurityBootApp.models.Person;
import com.sugandrey.SecurityBootApp.repositories.PeopleRepository;
import com.sugandrey.SecurityBootApp.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(final PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUserName(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User with such name is not found");
        }
        return new PersonDetails(person.get());
    }
}
