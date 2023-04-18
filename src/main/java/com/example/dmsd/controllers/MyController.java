package com.example.dmsd.controllers;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
public class MyController {

    static class Body {
        public Integer age;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String home(@PathParam("name") String name) {
//        , @RequestBody Body body
        return "Hello World!" + name;
    }
}