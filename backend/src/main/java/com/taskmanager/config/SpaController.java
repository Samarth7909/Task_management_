package com.taskmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaController {

    @RequestMapping(value = "{path:[^\\.]*}")
    public String redirect(HttpServletRequest request) {
        return "forward:/index.html";
    }
}
