package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Mating;
import com.example.rabbitfarm.service.MatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/matings")
public class MatingMvcController {

    private final MatingService matingService;

    @Autowired
    public MatingMvcController(MatingService matingService) {
        this.matingService = matingService;
    }

    @GetMapping
    public String listMatings(Model model) {
        List<Mating> matings = matingService.getAllMatings();
        model.addAttribute("matings", matings);
        model.addAttribute("pageTitle", "Mating Records");
        return "matings"; // src/main/resources/templates/matings.html
    }
}
