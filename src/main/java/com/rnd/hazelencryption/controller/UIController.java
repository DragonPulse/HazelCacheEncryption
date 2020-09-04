package com.rnd.hazelencryption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {


    @RequestMapping(value="/viewUsers")
    public String viewUsers() {
        return "users";
    }

}
