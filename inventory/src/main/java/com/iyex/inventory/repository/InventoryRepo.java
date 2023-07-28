package com.iyex.inventory.repository;

import com.iyex.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    boolean existsBySkuCode(String skuCode);
    List<Inventory> findBySkuCodeIn(List<String> skuCode);


}