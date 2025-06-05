package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Rabbit;
import com.example.rabbitfarm.model.RabbitStatus;
import com.example.rabbitfarm.model.Sale;
import com.example.rabbitfarm.repository.RabbitRepository;
import com.example.rabbitfarm.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SaleService {

    private final SaleRepository saleRepository;
    private final RabbitRepository rabbitRepository; // To update rabbit status

    @Autowired
    public SaleService(SaleRepository saleRepository, RabbitRepository rabbitRepository) {
        this.saleRepository = saleRepository;
        this.rabbitRepository = rabbitRepository;
    }

    public Sale recordSale(Sale sale) {
        // Validate rabbit exists and is available
        Rabbit rabbit = rabbitRepository.findById(sale.getRabbit().getId())
                .orElseThrow(() -> new RuntimeException("Rabbit not found with id: " + sale.getRabbit().getId()));

        if (rabbit.getStatus() != RabbitStatus.AVAILABLE) {
            throw new RuntimeException("Rabbit " + rabbit.getName() + " is not available for sale. Current status: " + rabbit.getStatus());
        }

        // Update rabbit status to SOLD
        rabbit.setStatus(RabbitStatus.SOLD);
        rabbit.setCage(null); // Or move to a "sold" virtual cage
        rabbitRepository.save(rabbit);

        sale.setRabbit(rabbit); // Ensure the sale object has the full rabbit details
        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    // Update and Delete for Sales might be complex (e.g., reverting rabbit status)
    // For now, let's keep them simple or omit if not strictly CRUD-like
    public Sale updateSale(Long id, Sale saleDetails) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));

        // Potentially complex logic if rabbit needs to be changed or status reverted
        // For simplicity, we'll update basic details. Reverting status is a business decision.
        sale.setSaleDate(saleDetails.getSaleDate());
        sale.setCustomerName(saleDetails.getCustomerName());
        sale.setPrice(saleDetails.getPrice());
        sale.setNotes(saleDetails.getNotes());
        // Changing rabbit_id on an existing sale is usually not allowed or complex.
        return saleRepository.save(sale);
    }

    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
        // Business decision: what happens to the rabbit if a sale is deleted?
        // For now, just delete the sale record. Rabbit status remains SOLD.
        // To revert, rabbit status should be set back to AVAILABLE.
        // Rabbit rabbit = sale.getRabbit();
        // rabbit.setStatus(RabbitStatus.AVAILABLE);
        // rabbitRepository.save(rabbit);
        saleRepository.deleteById(id);
    }
}
