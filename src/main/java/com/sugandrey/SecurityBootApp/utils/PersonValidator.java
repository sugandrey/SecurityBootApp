package com.sugandrey.SecurityBootApp.utils;

import com.sugandrey.SecurityBootApp.models.Person;
import com.sugandrey.SecurityBootApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PeopleService service;

    @Autowired
    public PersonValidator(final PeopleService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final Person person = (Person) target;
        if (service.findByUserName(person.getUserName()) != null) {
            errors.rejectValue("userName", "", "Пользователь с таким именем уже зарегистрирован!");
        }
        if (person.getPassword() == null || person.getPassword().isEmpty()) {
            errors.rejectValue("password", "", "Поле с паролем не должно быть пустым!");
        }
    }
}
