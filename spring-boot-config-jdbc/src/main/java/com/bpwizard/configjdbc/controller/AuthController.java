package com.bpwizard.configjdbc.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/spring/admin/auth")
@Slf4j
public class AuthController {

    @GetMapping(path="user")
    public String index(Model model, Authentication user) {
        log.info(">>>>>>>GET /: user={}", user);
        model.addAttribute("user", user);
        return "index";
    }
}