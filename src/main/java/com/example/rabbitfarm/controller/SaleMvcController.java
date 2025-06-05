package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Sale;
import com.example.rabbitfarm.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sales")
public class SaleMvcController {

    private final SaleService saleService;

    @Autowired
    public SaleMvcController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public String listSales(Model model) {
        List<Sale> sales = saleService.getAllSales();
        model.addAttribute("sales", sales);
        model.addAttribute("pageTitle", "Sales Records");
        return "sales"; // src/main/resources/templates/sales.html
    }
}
