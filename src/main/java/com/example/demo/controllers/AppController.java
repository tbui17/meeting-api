package com.example.demo.controllers;

import com.example.demo.services.AppService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
class AppController {


    private final AppService appService;

    AppController(AppService appService) {
        this.appService = appService;
    }


    @Hidden
    @GetMapping
    public RedirectView get() {
        return new RedirectView("/swagger-ui.html");
    }

    @Operation(
            summary = "Seed the database with sample data"
    )
    @PostMapping("/seed")
    @ResponseStatus(HttpStatus.CREATED)
    public void post() {
        appService.seed();
    }

    @Operation(
            summary = "Truncate the database"
    )
    @DeleteMapping("/seed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        appService.deleteSeed();
    }
}

