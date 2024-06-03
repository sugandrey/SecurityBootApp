package com.sugandrey.SecurityBootApp.controllers;

import com.sugandrey.SecurityBootApp.dto.AuthenticationDTO;
import com.sugandrey.SecurityBootApp.dto.PersonDTO;
import com.sugandrey.SecurityBootApp.models.Person;
import com.sugandrey.SecurityBootApp.security.JWTUtil;
import com.sugandrey.SecurityBootApp.services.RegistrationService;
import com.sugandrey.SecurityBootApp.utils.PersonValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegistrationService registrationService;

    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(final PersonValidator personValidator, final RegistrationService registrationService,
                          final JWTUtil jwtUtil,
                          final ModelMapper modelMapper,
                          final AuthenticationManager authenticationManager
    ) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
    return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") final Person person) { // ModelAttribute положит в модель пустого человека
        return "auth/registration";
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid final PersonDTO personDTO, final BindingResult result) {
        final Person person = convertToperson(personDTO);
        personValidator.validate(person, result);
        if (result.hasErrors()) {
//            для REST
            return Map.of("message", "Ошибка!");
//            для web
//            return "auth/registration";
        }
            registrationService.register(person);
        //            для web
//            return "redirect:/auth/login";
        final String token = jwtUtil.generateToken(person.getUserName());
        return Map.of("jwt-token", token);
        }

        @PostMapping("/login")
        public Map<String, String> performLogin(@RequestBody final AuthenticationDTO authenticationDTO) {
            final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authenticationDTO.getUserName(),
                    authenticationDTO.getPassword());
            try {
                authenticationManager.authenticate(token);
            }catch (BadCredentialsException e) {
                return Map.of("message", "Incorrect credentials");
            }
            final String newToken = jwtUtil.generateToken(authenticationDTO.getUserName());
            return Map.of("jwt-token", newToken);
        }

        private Person convertToperson(final PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
        }
}
