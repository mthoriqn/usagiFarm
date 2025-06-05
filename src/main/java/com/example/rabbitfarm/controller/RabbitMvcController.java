package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.Cage; // For populating cage dropdown
import com.example.rabbitfarm.model.Gender;
import com.example.rabbitfarm.model.RabbitStatus;
import com.example.rabbitfarm.service.RabbitService;
import com.example.rabbitfarm.service.CageService; // For populating cage dropdown
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rabbits") // Base path for all rabbit web pages
public class RabbitMvcController {

    private final RabbitService rabbitService;
    private final CageService cageService; // To list cages in the form

    @Autowired
    public RabbitMvcController(RabbitService rabbitService, CageService cageService) {
        this.rabbitService = rabbitService;
        this.cageService = cageService;
    }

    // Utility to add common model attributes
    private void addCommonAttributes(Model model) {
        List<Cage> cages = cageService.getAllCages();
        model.addAttribute("cages", cages);
        model.addAttribute("genders", Gender.values());
        model.addAttribute("statuses", RabbitStatus.values());
    }

    // Display list of rabbits (Master View)
    @GetMapping
    public String listRabbits(Model model) {
        List<Rabbit> rabbits = rabbitService.getAllRabbits();
        model.addAttribute("rabbits", rabbits);
        model.addAttribute("pageTitle", "Manage Rabbits");
        return "rabbits"; // src/main/resources/templates/rabbits.html
    }

    // Show form for adding a new rabbit
    @GetMapping("/add")
    public String showAddRabbitForm(Model model) {
        model.addAttribute("rabbit", new Rabbit());
        addCommonAttributes(model);
        model.addAttribute("pageTitle", "Add New Rabbit");
        return "rabbit-form"; // src/main/resources/templates/rabbit-form.html
    }

    // Show form for editing an existing rabbit
    @GetMapping("/edit/{id}")
    public String showEditRabbitForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Rabbit> rabbitOptional = rabbitService.getRabbitById(id);
        if (rabbitOptional.isPresent()) {
            model.addAttribute("rabbit", rabbitOptional.get());
            addCommonAttributes(model);
            model.addAttribute("pageTitle", "Edit Rabbit");
            return "rabbit-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Rabbit not found with ID: " + id);
            return "redirect:/rabbits";
        }
    }

    // Process the form for adding or editing a rabbit
    @PostMapping("/save")
    public String saveRabbit(@ModelAttribute("rabbit") Rabbit rabbit, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        // Basic validation example (can be expanded with @Valid and validators)
        if (result.hasErrors()) {
            addCommonAttributes(model);
            model.addAttribute("pageTitle", (rabbit.getId() == null ? "Add New Rabbit" : "Edit Rabbit"));
            return "rabbit-form";
        }

        // Handle unselected cage (empty string from form)
        if (rabbit.getCage() != null && rabbit.getCage().getId() == null) {
            rabbit.setCage(null);
        }

        try {
            rabbitService.saveRabbit(rabbit); // saveRabbit handles both create and update
            redirectAttributes.addFlashAttribute("successMessage", "Rabbit saved successfully!");
        } catch (Exception e) {
            // Log error e.getMessage()
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving rabbit: " + e.getMessage());
            addCommonAttributes(model);
            model.addAttribute("pageTitle", (rabbit.getId() == null ? "Add New Rabbit" : "Edit Rabbit"));
            model.addAttribute("rabbit", rabbit); // Send the rabbit object back to the form
            return "rabbit-form";
        }
        return "redirect:/rabbits";
    }

    // Handle request to delete a rabbit
    @GetMapping("/delete/{id}")
    public String deleteRabbit(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            rabbitService.deleteRabbit(id);
            redirectAttributes.addFlashAttribute("successMessage", "Rabbit deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting rabbit: " + e.getMessage());
        }
        return "redirect:/rabbits";
    }
}
