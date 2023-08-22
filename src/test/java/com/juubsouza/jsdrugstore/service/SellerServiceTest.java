package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Seller;
import com.juubsouza.jsdrugstore.model.dto.SellerDTO;
import com.juubsouza.jsdrugstore.model.dto.SellerDTOAdd;
import com.juubsouza.jsdrugstore.repository.SellerRepository;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SellerServiceTest {

    @InjectMocks
    private SellerService sellerService;

    @Mock
    private SellerRepository sellerRepository;

    private final Long SELLER_ID = 1L;

    @Test
    public void testAddSellerOk() {
        SellerDTOAdd sellerDTOAdd = MockDTOs.newMockSellerDTOAdd();

        sellerService.addSeller(sellerDTOAdd);

        verify(sellerRepository, times(1)).save(any());
    }

    @Test
    public void testSellerExists() {
        sellerService.sellerExists(SELLER_ID);

        verify(sellerRepository, times(1)).existsById(SELLER_ID);
    }

    @Test
    public void testDeleteSellerOk() {
        Seller seller = new Seller();
        seller.setId(SELLER_ID);

        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.of(seller));

        sellerService.deleteSeller(SELLER_ID);

        verify(sellerRepository, times(1)).deleteById(any());
    }

    @Test
    public void testDeleteSellerException() {
        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sellerService.deleteSeller(SELLER_ID));
    }

    @Test
    public void testFindAllSellersOk() {
        sellerService.findAllSellers();

        verify(sellerRepository, times(1)).findAllDTOs();
    }

    @Test
    public void testFindSellersByFirstOrLastNameOk() {
        sellerService.findSellersByFirstOrLastName("John");

        verify(sellerRepository, times(1)).findDTOsByFirstOrLastName("John");
    }

    @Test
    public void testFindSellerByIdOk() {
        sellerService.findSellerById(SELLER_ID);

        verify(sellerRepository, times(1)).findDTOById(SELLER_ID);
    }

    @Test
    public void testUpdateSellerOk() {
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();

        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.of(new Seller()));

        sellerService.updateSeller(sellerDTO);

        verify(sellerRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateSellerException() {
        SellerDTO sellerDTO = MockDTOs.newMockSellerDTO();

        when(sellerRepository.findById(SELLER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> sellerService.updateSeller(sellerDTO));
    }
}
