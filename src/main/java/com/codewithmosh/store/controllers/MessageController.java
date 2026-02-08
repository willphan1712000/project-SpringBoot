package com.codewithmosh.store.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.entities.Message;

@RestController
public class MessageController {
    @RequestMapping("/about")
    public Message sayHello() {
        return new Message("Will phan 1712000");
    }
}
