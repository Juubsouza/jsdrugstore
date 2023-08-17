package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "API operations related to products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/all")
    @Operation(summary = "Find all products", description = "Returns a list of available products")
    public List<ProductDTO> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find product by id", description = "Returns a product matching the provided id, if it exists")
    public ProductDTO findProductById(@Parameter(description = "Product ID") @PathVariable Long id) {
        return productService.findProductById(id);
    }
}
