package capyeats.address.service;

import capyeats.address.model.Address;
import capyeats.address.repository.AddressRepository;
import capyeats.user.model.User;
import capyeats.user.repository.UserRepository;
import capyeats.web.dto.AddressRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService {

    private  final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public AddressService(UserRepository userRepository, AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    public void createAddress(UUID userId, AddressRequest request) {
        Optional<User> userOptional = userRepository.findById(userId);

        Address address = Address.builder()
                .user(userOptional.get())
                .label(request.getLabel())
                .addressType(request.getAddressType())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .street(request.getStreet())
                .streetNumber(request.getStreetNumber())
                .apartment(request.getApartment())
                .build();

        addressRepository.save(address);
    }


    @Transactional
    public void deleteAddress(UUID userId, UUID addressId) {
        Address address = findOwnedAddress(userId, addressId);
        addressRepository.delete(address);
    }

    private Address findOwnedAddress(UUID userId, UUID addressId) {
        return addressRepository.findByIdAndUser_Id(addressId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
    }

    public AddressRequest getAddressForEdit(UUID userId, UUID addressId) {
        Address address = findOwnedAddress(userId, addressId);

        return new AddressRequest(
                address.getLabel(),
                address.getAddressType(),
                address.getCity(),
                address.getPostalCode(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getApartment()
        );
    }

    public void updateAddress(UUID userId, UUID addressId,
                              AddressRequest request) {
        Address address = findOwnedAddress(userId, addressId);

        address.setLabel(request.getLabel());
        address.setAddressType(request.getAddressType());
        address.setCity(request.getCity());
        address.setPostalCode(request.getPostalCode());
        address.setStreet(request.getStreet());
        address.setStreetNumber(request.getStreetNumber());
        address.setApartment(request.getApartment());

        addressRepository.save(address);
    }
}
