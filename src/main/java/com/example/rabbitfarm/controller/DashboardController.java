package com.example.rabbitfarm.controller;

// import com.example.rabbitfarm.service.RabbitService; // Example if fetching data
// import com.example.rabbitfarm.service.CageService;   // Example if fetching data
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.security.core.Authentication; // To get user info
// import org.springframework.security.core.context.SecurityContextHolder; // To get user info

@Controller
public class DashboardController {

    // Example: Autowire services if you need to fetch data for the dashboard
    // @Autowired
    // private RabbitService rabbitService;
    // @Autowired
    // private CageService cageService;

    @GetMapping("/")
    public String viewDashboard(Model model) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String currentPrincipalName = authentication.getName();
        // model.addAttribute("username", currentPrincipalName);

        // Example: Fetch data and add to model
        // model.addAttribute("totalRabbits", rabbitService.getAllRabbits().size());
        // model.addAttribute("totalCages", cageService.getAllCages().size());

        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("welcomeMessage", "Welcome to the Rabbit Farm Management System!");
        return "dashboard"; // This will resolve to src/main/resources/templates/dashboard.html
    }

    // If you want a specific /dashboard endpoint as well or instead of /
    @GetMapping("/dashboard")
    public String viewDashboardExplicit(Model model) {
        // You can reuse the same logic or have different logic
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("welcomeMessage", "Welcome to the Rabbit Farm Management System!");
        return "dashboard";
    }
}
