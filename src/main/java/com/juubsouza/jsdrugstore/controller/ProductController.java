package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.model.dto.ProductDTOAdd;
import com.juubsouza.jsdrugstore.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "API operations related to products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/all")
    @Operation(summary = "Find all products", description = "Returns a list of available products")
    public List<ProductDTO> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/by-id={id}")
    @Operation(summary = "Find product by id", description = "Returns a product matching the provided id, if it exists")
    public ProductDTO findProductById(@Parameter(description = "Product ID") @PathVariable Long id) {
        return productService.findProductById(id);
    }

    @GetMapping("/by-name={name}")
    @Operation(summary = "Find product by name", description = "Returns a product matching the provided name, if it exists")
    public List<ProductDTO> findProductByName(@Parameter(description = "Product name") @PathVariable String name) {
        return productService.findProductsByName(name);
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new product", description = "Adds a new product to the database")
    public ResponseEntity<?> addProduct(@Parameter(description = "Information about the product") @RequestBody ProductDTOAdd productDTOAdd) {
        if (productDTOAdd.getName() == null || productDTOAdd.getName().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product name cannot be empty.");

        if (productService.productExists(productDTOAdd.getName()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There already is a product with this name in the database.");

        if (productDTOAdd.getManufacturer() == null || productDTOAdd.getManufacturer().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product manufacturer cannot be empty.");

        if (productDTOAdd.getPrice() == null || productDTOAdd.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product price must be greater than 0.");

        if (productDTOAdd.getStock() == null || productDTOAdd.getStock() < 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product stock must be greater than or equal to 0.");

        try {
            ProductDTO addedProduct = productService.addProduct(productDTOAdd);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete={id}")
    @Operation(summary = "Delete product by id", description = "Deletes a product matching the provided id, if it exists")
    public ResponseEntity<?> deleteProduct(@Parameter(description = "Product ID") @PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
