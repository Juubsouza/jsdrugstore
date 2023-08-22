package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Seller;
import com.juubsouza.jsdrugstore.model.dto.SellerDTO;
import com.juubsouza.jsdrugstore.model.dto.SellerDTOAdd;
import com.juubsouza.jsdrugstore.repository.SellerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    @Autowired
    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public SellerDTO addSeller(SellerDTOAdd sellerDTOAdd) {
        Seller seller = new Seller();
        seller.setFirstName(sellerDTOAdd.getFirstName());
        seller.setLastName(sellerDTOAdd.getLastName());
        seller.setShift(sellerDTOAdd.getShift());
        seller.setAdmissionDate(sellerDTOAdd.getAdmissionDate());

        sellerRepository.save(seller);

        return new SellerDTO(seller.getId(), seller.getFirstName(), seller.getLastName(), seller.getShift(), seller.getAdmissionDate());
    }

    public boolean sellerExists(Long id) {
        return sellerRepository.existsById(id);
    }

    public List<SellerDTO> findAllSellers() {
        return sellerRepository.findAllDTOs();
    }

    public SellerDTO findSellerById(Long id) {
        return sellerRepository.findDTOById(id).orElse(null);
    }

    public List<SellerDTO> findSellersByFirstOrLastName(String name) {
        return sellerRepository.findDTOsByFirstOrLastName(name);
    }

    public SellerDTO updateSeller(SellerDTO sellerDTO) throws EntityNotFoundException {
        Seller existingSeller = sellerRepository.findById(sellerDTO.getId()).orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        existingSeller.setFirstName(sellerDTO.getFirstName());
        existingSeller.setLastName(sellerDTO.getLastName());
        existingSeller.setShift(sellerDTO.getShift());
        existingSeller.setAdmissionDate(sellerDTO.getAdmissionDate());

        sellerRepository.save(existingSeller);

        return new SellerDTO(existingSeller.getId(), existingSeller.getFirstName(), existingSeller.getLastName(), existingSeller.getShift(), existingSeller.getAdmissionDate());
    }

    public void deleteSeller(Long id) {
        Seller existingSeller = sellerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        sellerRepository.deleteById(existingSeller.getId());
    }
}
