package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Price;
import com.juubsouza.jsdrugstore.model.Product;
import com.juubsouza.jsdrugstore.model.Stock;
import com.juubsouza.jsdrugstore.model.dto.ProductDTO;
import com.juubsouza.jsdrugstore.model.dto.ProductDTOAdd;
import com.juubsouza.jsdrugstore.repository.PriceRepository;
import com.juubsouza.jsdrugstore.repository.ProductRepository;
import com.juubsouza.jsdrugstore.repository.StockRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    @Autowired
    public ProductService(PriceRepository priceRepository, ProductRepository productRepository, StockRepository stockRepository) {
        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public ProductDTO addProduct(ProductDTOAdd productDTOAdd) throws RuntimeException{
        try {
            Product product = new Product();
            product.setName(productDTOAdd.getName());
            product.setManufacturer(productDTOAdd.getManufacturer());

            Price price = new Price();
            price.setPrice(productDTOAdd.getPrice());

            Stock stock = new Stock();
            stock.setStock(productDTOAdd.getStock());

            Product createdProduct = productRepository.save(product);

            price.setProduct(createdProduct);
            stock.setProduct(createdProduct);

            priceRepository.save(price);
            stockRepository.save(stock);

            return new ProductDTO(createdProduct.getId(), createdProduct.getName(), createdProduct.getManufacturer(), price.getPrice(), stock.getStock());
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to add product to the database", e);
        }
    }

    @Transactional
    public void deleteProduct(Long id) throws EntityNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found"));

        productRepository.deleteById(product.getId());
    }

    public List<ProductDTO> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public ProductDTO findProductById(Long id) {
        return productRepository.findProductById(id).orElse(null);
    }

    public ProductDTO findProductByName(String name) {
        return productRepository.findProductByName(name).orElse(null);
    }

    public boolean productExists(String name) {
        return productRepository.existsByName(name);
    }
}
