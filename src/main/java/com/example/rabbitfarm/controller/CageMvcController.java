package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Cage;
import com.example.rabbitfarm.service.CageService;
import com.example.rabbitfarm.service.RabbitService; // To check for rabbits in cage before deletion
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cages") // Base path for all cage web pages
public class CageMvcController {

    private final CageService cageService;
    private final RabbitService rabbitService;

    @Autowired
    public CageMvcController(CageService cageService, RabbitService rabbitService) {
        this.cageService = cageService;
        this.rabbitService = rabbitService;
    }

    // Display list of cages (Master View)
    @GetMapping
    public String listCages(Model model) {
        List<Cage> cages = cageService.getAllCages();
        model.addAttribute("cages", cages);
        model.addAttribute("pageTitle", "Manage Cages");
        return "cages"; // src/main/resources/templates/cages.html
    }

    // Show form for adding a new cage
    @GetMapping("/add")
    public String showAddCageForm(Model model) {
        model.addAttribute("cage", new Cage());
        model.addAttribute("pageTitle", "Add New Cage");
        return "cage-form"; // src/main/resources/templates/cage-form.html
    }

    // Show form for editing an existing cage
    @GetMapping("/edit/{id}")
    public String showEditCageForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Cage> cageOptional = cageService.getCageById(id);
        if (cageOptional.isPresent()) {
            model.addAttribute("cage", cageOptional.get());
            model.addAttribute("pageTitle", "Edit Cage");
            return "cage-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Cage not found with ID: " + id);
            return "redirect:/cages";
        }
    }

    // Process the form for adding or editing a cage
    @PostMapping("/save")
    public String saveCage(@ModelAttribute("cage") Cage cage, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", (cage.getId() == null ? "Add New Cage" : "Edit Cage"));
            return "cage-form";
        }

        try {
            // Check for duplicate cage number before saving
            Optional<Cage> existingCage = cageService.getCageByCageNumber(cage.getCageNumber());
            if (existingCage.isPresent() && (cage.getId() == null || !existingCage.get().getId().equals(cage.getId()))) {
                model.addAttribute("pageTitle", (cage.getId() == null ? "Add New Cage" : "Edit Cage"));
                model.addAttribute("cage", cage); // Send the cage object back
                // Add specific error for cage number
                result.rejectValue("cageNumber", "duplicate.cageNumber", "Cage number already exists.");
                 model.addAttribute("errorMessage", "Cage number already exists."); // General message too
                return "cage-form";
            }
            cageService.saveCage(cage);
            redirectAttributes.addFlashAttribute("successMessage", "Cage saved successfully!");
        } catch (Exception e) {
            // Log error e.getMessage()
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving cage: " + e.getMessage());
            model.addAttribute("pageTitle", (cage.getId() == null ? "Add New Cage" : "Edit Cage"));
            model.addAttribute("cage", cage); // Send the cage object back to the form
            return "cage-form";
        }
        return "redirect:/cages";
    }

    // Handle request to delete a cage
    @GetMapping("/delete/{id}")
    public String deleteCage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // Check if cage has rabbits
            if (!rabbitService.getRabbitsByCageId(id).isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete cage: It still contains rabbits. Please move or delete the rabbits first.");
                return "redirect:/cages";
            }
            cageService.deleteCage(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cage deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting cage: " + e.getMessage());
        }
        return "redirect:/cages";
    }
}
