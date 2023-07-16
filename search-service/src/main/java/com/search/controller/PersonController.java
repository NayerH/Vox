package com.search.controller;
import com.search.entity.Person;
import com.search.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/person")
public class PersonController {
    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping
    public void save(@RequestBody final Person person) {
        service.save(person);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable final String id) {
        return service.findById(id);
    }

    @GetMapping("/")
    public List<Person> findAll() {
        return service.findAll();
    }
    @PostMapping("/person")
    public List<Person> findFuzzy(@RequestBody Map<String, String> payload ) {
        System.out.println(payload);
        return service.findFuzzy(payload.get("name"));
    }
    @PostMapping("/f")
    public List<Person> findfarah(@RequestBody Map<String, String> payload ) {
        //System.out.println(f);
        return service.getSuggestions(payload.get("name"));
    }
}