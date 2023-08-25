package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.*;
import com.juubsouza.jsdrugstore.model.dto.SaleDTO;
import com.juubsouza.jsdrugstore.model.dto.SaleDTOAdd;
import com.juubsouza.jsdrugstore.model.dto.SaleProductDTO;
import com.juubsouza.jsdrugstore.model.dto.SaleProductDTOAdd;
import com.juubsouza.jsdrugstore.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    private final SaleProductRepository saleProductRepository;

    private final SellerRepository sellerRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    private final StockRepository stockRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, SaleProductRepository saleProductRepository, SellerRepository sellerRepository,
                       CustomerRepository customerRepository, ProductRepository productRepository, StockRepository stockRepository) {
        this.saleRepository = saleRepository;
        this.saleProductRepository = saleProductRepository;
        this.sellerRepository = sellerRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public SaleDTO addSale(SaleDTOAdd saleDTOAdd) throws EntityNotFoundException {
        Sale sale = new Sale();
        sale.setPaymentMethod(saleDTOAdd.getPaymentMethod());
        sale.setPaymentStatus("PENDING");
        sale.setShippingStatus("PENDING");

        Customer customer = customerRepository.findById(saleDTOAdd.getCustomerId()).orElseThrow(() -> new EntityNotFoundException("Customer not found."));
        Seller seller = sellerRepository.findById(saleDTOAdd.getSellerId()).orElseThrow(() -> new EntityNotFoundException("Seller not found."));

        sale.setCustomer(customer);
        sale.setSeller(seller);

        List<SaleProductDTOAdd> saleProductDTOAddList = saleDTOAdd.getSaleProducts();

        calculateTotal(saleProductDTOAddList, sale);
        saleRepository.save(sale);

        List<SaleProduct> saleProductsToBeAdded = new ArrayList<>();

        for (SaleProductDTOAdd saleProductDTOAdd : saleProductDTOAddList) {
            SaleProduct saleProduct = new SaleProduct();
            saleProduct.setQuantity(saleProductDTOAdd.getQuantity());
            saleProduct.setSale(sale);

            Product product = productRepository.findById(saleProductDTOAdd.getProductId()).orElseThrow(() -> new EntityNotFoundException("Product not found."));
            saleProduct.setProduct(product);

            saleProductsToBeAdded.add(saleProduct);
        }

        saleProductRepository.saveAll(saleProductsToBeAdded);

        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(sale.getId());
        saleDTO.setPaymentMethod(sale.getPaymentMethod());
        saleDTO.setPaymentStatus(sale.getPaymentStatus());
        saleDTO.setShippingStatus(sale.getShippingStatus());
        saleDTO.setTotal(sale.getTotal());
        saleDTO.setCustomerId(sale.getCustomer().getId());
        saleDTO.setSellerId(sale.getSeller().getId());

        List<SaleProductDTO> saleProductDTOList = saleProductRepository.findAllDTOsBySaleId(sale.getId());

        saleDTO.setSaleProducts(saleProductDTOList);

        updateStocks(saleDTOAdd.getSaleProducts());

        return saleDTO;
    }

    public List<SaleDTO> findAllSalesForCustomer(Long customerId) {
        List<SaleDTO> saleDTOs = saleRepository.findAllDTOsByCustomerId(customerId);

        for (SaleDTO saleDTO : saleDTOs) {
            List<SaleProductDTO> saleProductDTOs = saleProductRepository.findAllDTOsBySaleId(saleDTO.getId());

            saleDTO.setSaleProducts(saleProductDTOs);
        }

        return saleDTOs;
    }

    private void calculateTotal(List<SaleProductDTOAdd> products, Sale sale) {
        BigDecimal total = BigDecimal.ZERO;

        List<Product> fetchedProducts = getProductsUsingDTOList(products);

        for (SaleProductDTOAdd product : products) {
            Long productId = product.getProductId();

            Product productToBeAdded = fetchProductForIteration(fetchedProducts, productId);

            BigDecimal productPrice = productToBeAdded.getPrice().getPrice();
            BigDecimal productQuantity = BigDecimal.valueOf(product.getQuantity());

            BigDecimal productCost = productPrice.multiply(productQuantity);
            total = total.add(productCost);
        }

        sale.setTotal(total);
    }

    private void updateStocks(List<SaleProductDTOAdd> products) {
        List<Product> fetchedProducts = getProductsUsingDTOList(products);

        List<Stock> stocksToBeUpdated = new ArrayList<>();

        for (SaleProductDTOAdd product : products) {
            Long productId = product.getProductId();

            Product productToBeAdded = fetchProductForIteration(fetchedProducts, productId);

            Stock stock = stockRepository.findByProductId(productToBeAdded.getId()).orElseThrow(() -> new EntityNotFoundException("Stock not found."));
            stock.setStock(stock.getStock() - product.getQuantity());

            stocksToBeUpdated.add(stock);
        }

        stockRepository.saveAll(stocksToBeUpdated);
    }

    private List<Product> getProductsUsingDTOList(List<SaleProductDTOAdd> products) {
        List<Long> productIds = products.stream()
                .map(SaleProductDTOAdd::getProductId)
                .collect(Collectors.toList());

        return productRepository.findByIdIn(productIds);
    }

    private Product fetchProductForIteration(List<Product> products, Long productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));
    }
}
