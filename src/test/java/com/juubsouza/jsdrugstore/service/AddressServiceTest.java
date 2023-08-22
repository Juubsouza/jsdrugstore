package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Address;
import com.juubsouza.jsdrugstore.model.Customer;
import com.juubsouza.jsdrugstore.model.CustomerAddress;
import com.juubsouza.jsdrugstore.model.dto.AddressDTO;
import com.juubsouza.jsdrugstore.model.dto.AddressDTOAdd;
import com.juubsouza.jsdrugstore.repository.AddressRepository;
import com.juubsouza.jsdrugstore.repository.CustomerAddressRepository;
import com.juubsouza.jsdrugstore.utils.MockDTOs;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AddressServiceTest {

    @InjectMocks
    private AddressService addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerAddressRepository customerAddressRepository;

    private final Long ADDRESS_ID = 1L;

    @Test
    public void testAddAddressOk() {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();

        when(addressRepository.save(any())).thenReturn(newMockAddress());

        addressService.addAddress(addressDTOAdd);

        verify(addressRepository, times(1)).save(any());
        verify(customerAddressRepository, times(1)).save(any());
    }

    @Test
    public void testAddAddressOkWithIsShippingFalse() {
        AddressDTOAdd addressDTOAdd = MockDTOs.newMockAddressDTOAdd();
        addressDTOAdd.setShipping(false);

        when(addressRepository.save(any())).thenReturn(newMockAddress());

        addressService.addAddress(addressDTOAdd);

        verify(addressRepository, times(1)).save(any());
        verify(customerAddressRepository, times(1)).save(any());
    }

    @Test
    public void testAddressExists() {
        addressService.addressExists(ADDRESS_ID);

        verify(addressRepository, times(1)).existsById(ADDRESS_ID);
    }

    @Test
    public void testDeleteAddressOk() {
        CustomerAddress customerAddress = newMockCustomerAddress();

        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(newMockAddress()));
        when(customerAddressRepository.findByAddressId(ADDRESS_ID)).thenReturn(Optional.of(customerAddress));

        addressService.deleteAddress(ADDRESS_ID);

        verify(customerAddressRepository, times(1)).delete(customerAddress);
        verify(addressRepository, times(1)).deleteById(ADDRESS_ID);
    }

    @Test
    public void testDeleteAddressExceptionAddress() {
        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.deleteAddress(ADDRESS_ID));
    }

    @Test
    public void testDeleteAddressExceptionCustomerAddress() {
        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(newMockAddress()));
        when(customerAddressRepository.findByAddressId(ADDRESS_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.deleteAddress(ADDRESS_ID));
    }

    @Test
    public void testFindAllAddressesByCustomerIdOk() {
        addressService.findAllAddressesByCustomerId(1L);

        verify(addressRepository, times(1)).findAllDTOsByCustomerId(1L);
    }

    @Test
    public void testSetAddressAsShippingTrueOk() {
        Customer customer = new Customer();
        customer.setId(1L);

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomer(customer);

        List<CustomerAddress> customerAddressList = new ArrayList<>();
        customerAddressList.add(customerAddress);

        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(new Address()));
        when(customerAddressRepository.findByAddressId(ADDRESS_ID)).thenReturn(Optional.of(customerAddress));
        when(customerAddressRepository.findAllByCustomerId(1L)).thenReturn(customerAddressList);

        addressService.setAddressAsShippingTrue(ADDRESS_ID);

        verify(customerAddressRepository, times(1)).save(customerAddress);
    }

    @Test
    public void testSetAddressAsShippingTrueWithException() {
        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.setAddressAsShippingTrue(ADDRESS_ID));
    }

    @Test
    public void testUpdateAddressOk() {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(newMockAddress()));
        when(customerAddressRepository.findByAddressId(ADDRESS_ID)).thenReturn(Optional.of(newMockCustomerAddress()));

        addressService.updateAddress(addressDTO);

        verify(addressRepository, times(1)).save(any());
        verify(customerAddressRepository, times(1)).save(any());
    }

    @Test
    public void testUpdateAddressExceptionAddress() {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.updateAddress(addressDTO));
    }

    @Test
    public void testUpdateAddressExceptionCustomerAddress() {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();

        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(newMockAddress()));
        when(customerAddressRepository.findByAddressId(ADDRESS_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> addressService.updateAddress(addressDTO));
    }

    @Test
    public void testUpdateIsShippingFalse() {
        AddressDTO addressDTO = MockDTOs.newMockAddressDTO();
        addressDTO.setShipping(false);

        when(addressRepository.findById(ADDRESS_ID)).thenReturn(Optional.of(newMockAddress()));
        when(customerAddressRepository.findByAddressId(ADDRESS_ID)).thenReturn(Optional.of(newMockCustomerAddress()));

        addressService.updateAddress(addressDTO);

        verify(addressRepository, times(1)).save(any());
        verify(customerAddressRepository, times(1)).save(any());
    }

    private Address newMockAddress() {
        Address address = new Address();
        address.setId(1L);
        address.setDetails("Test Address");
        address.setCity("Test City");
        address.setState("Test State");
        address.setCountry("Test Country");

        return address;
    }

    private Customer newMockCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("johndoe@email.com");

        return customer;
    }

    private CustomerAddress newMockCustomerAddress() {
        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setCustomer(newMockCustomer());
        customerAddress.setAddress(newMockAddress());

        return customerAddress;
    }
}
