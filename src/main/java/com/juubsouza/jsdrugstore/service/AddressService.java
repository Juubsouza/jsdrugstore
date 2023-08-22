package com.juubsouza.jsdrugstore.service;

import com.juubsouza.jsdrugstore.model.Address;
import com.juubsouza.jsdrugstore.model.Customer;
import com.juubsouza.jsdrugstore.model.CustomerAddress;
import com.juubsouza.jsdrugstore.model.dto.AddressDTO;
import com.juubsouza.jsdrugstore.model.dto.AddressDTOAdd;
import com.juubsouza.jsdrugstore.repository.AddressRepository;
import com.juubsouza.jsdrugstore.repository.CustomerAddressRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerAddressRepository customerAddressRepository;

    public boolean addressExists(Long id) {
        return addressRepository.existsById(id);
    }

    @Autowired
    public AddressService(AddressRepository addressRepository, CustomerAddressRepository customerAddressRepository) {
        this.addressRepository = addressRepository;
        this.customerAddressRepository = customerAddressRepository;
    }

    public void setAddressAsShippingTrue(Long id) {
        addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found"));
        CustomerAddress customerAddress = customerAddressRepository.findByAddressId(id).orElseThrow(() -> new EntityNotFoundException("CustomerAddress not found"));

        setAllCustomerAddressToIsShippingFalse(customerAddress.getCustomer().getId());

        customerAddress.setIsShipping(true);

        customerAddressRepository.save(customerAddress);
    }

    @Transactional
    public AddressDTO addAddress(AddressDTOAdd addressDTOAdd) {
        Address address = new Address();

        address.setDetails(addressDTOAdd.getDetails());
        address.setCountry(addressDTOAdd.getCountry());
        address.setState(addressDTOAdd.getState());
        address.setCity(addressDTOAdd.getCity());

        Address addedAddress = addressRepository.save(address);

        if (addressDTOAdd.isShipping())
            setAllCustomerAddressToIsShippingFalse(addressDTOAdd.getCustomerId());

        Customer customer = new Customer();
        customer.setId(addressDTOAdd.getCustomerId());

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setIsShipping(addressDTOAdd.isShipping());
        customerAddress.setAddress(address);
        customerAddress.setCustomer(customer);

        customerAddressRepository.save(customerAddress);

        return new AddressDTO(addedAddress.getId(), addedAddress.getDetails(), addedAddress.getCity(), addedAddress.getState(),
                addedAddress.getCountry(), addressDTOAdd.isShipping());
    }

    @Transactional
    public void deleteAddress(Long id) throws EntityNotFoundException {
        Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found"));

        CustomerAddress customerAddress = customerAddressRepository.findByAddressId(id).orElseThrow(() -> new EntityNotFoundException("CustomerAddress not found"));
        customerAddressRepository.delete(customerAddress);

        addressRepository.deleteById(existingAddress.getId());
    }

    public List<AddressDTO> findAllAddressesByCustomerId(Long customerId) {
        return addressRepository.findAllDTOsByCustomerId(customerId);
    }

    @Transactional
    public AddressDTO updateAddress(AddressDTO addressDTOUpdate) throws EntityNotFoundException {
        Address existingAddress = addressRepository.findById(addressDTOUpdate.getId()).orElseThrow(() -> new EntityNotFoundException("Address not found"));

        existingAddress.setDetails(addressDTOUpdate.getDetails());
        existingAddress.setCity(addressDTOUpdate.getCity());
        existingAddress.setState(addressDTOUpdate.getState());
        existingAddress.setCountry(addressDTOUpdate.getCountry());

        addressRepository.save(existingAddress);

        CustomerAddress customerAddress = customerAddressRepository.findByAddressId(addressDTOUpdate.getId())
                .orElseThrow(() -> new EntityNotFoundException("CustomerAddress not found"));

        if (addressDTOUpdate.isShipping())
            setAllCustomerAddressToIsShippingFalse(customerAddress.getCustomer().getId());

        customerAddress.setIsShipping(addressDTOUpdate.isShipping());

        customerAddressRepository.save(customerAddress);

        return new AddressDTO(existingAddress.getId(), existingAddress.getDetails(), existingAddress.getCity(), existingAddress.getState(),
                existingAddress.getCountry(), addressDTOUpdate.isShipping());
    }

    private void setAllCustomerAddressToIsShippingFalse(Long customerId) {
        List<CustomerAddress> customerAddresses = customerAddressRepository.findAllByCustomerId(customerId);

        for (CustomerAddress customerAddress : customerAddresses) {
            customerAddress.setIsShipping(false);
        }

        customerAddressRepository.saveAll(customerAddresses);
    }
}
