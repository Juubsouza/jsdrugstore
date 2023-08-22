package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.model.dto.ProductDTOAdd;
import com.juubsouza.jsdrugstore.service.ProductService;
import com.juubsouza.jsdrugstore.utils.ValidationResponse;
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
@RequestMapping("/product")
@Tag(name = "Product", description = "API operations related to products")
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
    public ResponseEntity<?> addProduct(@Parameter @RequestBody ProductDTOAdd productDTOAdd) {
        ValidationResponse validationResponse = validateFields(productDTOAdd.getName(), productDTOAdd.getManufacturer(),
                productDTOAdd.getPrice(), productDTOAdd.getStock(), true, null);

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(validationResponse.getMessage());

        try {
            ProductDTO addedProduct = productService.addProduct(productDTOAdd);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/update")
    @Operation(summary = "Update a product", description = "Updates a product in the database")
    public ResponseEntity<?> updateProduct(@Parameter @RequestBody ProductDTO productDTO) {
        ValidationResponse validationResponse = validateFields(productDTO.getName(), productDTO.getManufacturer(),
                productDTO.getPrice(), productDTO.getStock(), false, productDTO.getId());

        if (validationResponse.getStatus() != HttpStatus.OK)
            return ResponseEntity.status(validationResponse.getStatus()).body(validationResponse.getMessage());

        try {
            ProductDTO updatedProduct = productService.updateProduct(productDTO);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
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

    private ValidationResponse validateFields(String name, String manufacturer, BigDecimal price, Integer stock, boolean isCreating, Long id) {
        if (!isCreating) {
            if (id == null || id <= 0)
                return new ValidationResponse("Product ID must be greater than zero.", HttpStatus.BAD_REQUEST);
        } else {
            if (productService.productExists(name))
                return new ValidationResponse("There already is a product with this name in the database.", HttpStatus.BAD_REQUEST);
        }

        if (name == null || name.isEmpty())
            return new ValidationResponse("Product name cannot be empty.", HttpStatus.BAD_REQUEST);

        if (manufacturer == null || manufacturer.isEmpty())
            return new ValidationResponse("Product manufacturer cannot be empty.", HttpStatus.BAD_REQUEST);

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            return new ValidationResponse("Product price must be greater than 0.", HttpStatus.BAD_REQUEST);

        if (stock == null || stock < 0)
            return new ValidationResponse("Product stock must be greater than or equal to 0.", HttpStatus.BAD_REQUEST);

        return new ValidationResponse("OK", HttpStatus.OK);
    }
}
