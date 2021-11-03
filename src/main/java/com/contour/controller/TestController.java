package com.contour.controller;

import com.contour.service.V2rayService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private final V2rayService v2rayService;

    public TestController(V2rayService v2rayService) {
        this.v2rayService = v2rayService;
    }
}
