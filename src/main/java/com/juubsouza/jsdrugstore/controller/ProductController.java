package com.juubsouza.jsdrugstore.controller;

import com.juubsouza.jsdrugstore.model.Product;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.service.ProductService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
//@Api(tags = "Product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/all")
    public List<ProductDTO> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO findProductById(@PathVariable Integer id) {
        return productService.findProductById(id);
    }
}
