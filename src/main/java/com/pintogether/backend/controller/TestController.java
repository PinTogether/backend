package com.pintogether.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class TestController {

    @Value("test")
    private String test;
    public String test(){
        System.out.println("test = " + test);
        return "index";
    }
}
