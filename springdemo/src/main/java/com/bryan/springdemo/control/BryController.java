package com.bryan.springdemo.control;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bry")
public class BryController {

    @GetMapping("/helloworld2")
    public String helloWorld2() {
        return "hello world from bryan";
    }

}
