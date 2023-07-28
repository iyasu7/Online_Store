package com.iyex.inventory.service;

import com.iyex.inventory.dto.InventoryDto;
import com.iyex.inventory.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;
    @Transactional(readOnly = true)
    public List<InventoryDto> isInStock(List<String> skuCode){
        return inventoryRepo.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> InventoryDto.builder()
                    .skuCode(inventory.getSkuCode())
                    .isInStock(inventory.getQuantity()>0)
                    .build()
        ).toList();
    }
}
