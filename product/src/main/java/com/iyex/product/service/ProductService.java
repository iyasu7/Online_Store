package com.iyex.product.service;

import com.iyex.product.domain.Product;
import com.iyex.product.dto.ProductRequest;
import com.iyex.product.dto.ProductResponse;
import com.iyex.product.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepo productRepo;

    public ProductResponse saveProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
         productRepo.save(product);
         return ProductResponse.builder()
                 .id(product.getId())
                 .name(product.getName())
                 .description(product.getDescription())
                 .price(product.getPrice())
                 .build();
    }

    public List<ProductResponse> findAllProducts() {

        List<Product> products = productRepo.findAll();
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse findProductById(Long id) {
        Product product = productRepo.findById(id).orElseThrow(()-> new NoSuchElementException("Product with id "+id+" NOT found!!"));
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
    public ProductResponse findProductByName(String name) {
        Product product = productRepo.findByName(name);
        if (product == null) {
            throw new NoSuchElementException("Product with name "+name+" NOT found!!");
        }
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
