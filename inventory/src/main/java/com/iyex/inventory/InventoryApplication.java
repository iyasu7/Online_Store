package com.iyex.inventory;

import com.iyex.inventory.domain.Inventory;
import com.iyex.inventory.repository.InventoryRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepo inventoryRepo){
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setSkuCode("Surface Pro 7");
            inventory.setQuantity(0);

            Inventory inventory1 = new Inventory();
            inventory1.setSkuCode("lenovo Yog");
            inventory1.setQuantity(6);

            inventoryRepo.save(inventory);
            inventoryRepo.save(inventory1);

        };
    }

}
