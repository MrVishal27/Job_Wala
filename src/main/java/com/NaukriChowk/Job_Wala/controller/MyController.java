package com.NaukriChowk.Job_Wala.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @GetMapping("/my")
    public String get(){
        return "this is my home";
    }
}
