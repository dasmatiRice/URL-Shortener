package com.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller{

@GetMapping("/test")
public String testGetMethod(@RequestParam(value = "name", defaultValue = "test") String name){

    

    return String.format("Hello this is a get method %s",name);
 
}

@GetMapping("/hello")
public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
  return String.format("Hello %s!", name);
}
}

