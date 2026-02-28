package com.meritcap.transformer;

import com.meritcap.DTOs.requestDTOs.address.AddressRequestDto;
import com.meritcap.DTOs.responseDTOs.address.AddressResponseDto;
import com.meritcap.model.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressTransformer {

    public static Address toEntity(AddressRequestDto addressRequestDto) {
        return Address.builder()
                .addressLine1(addressRequestDto.getAddressLine1())
                .addressLine2(addressRequestDto.getAddressLine2())
                .cityVillage(addressRequestDto.getCityVillage())
                .pinCode(addressRequestDto.getPinCode())
                .landmark(addressRequestDto.getLandmark())
                .state(addressRequestDto.getState())
                .country(addressRequestDto.getCountry())
                .addressType(addressRequestDto.getAddressType())
                .entityType(addressRequestDto.getEntityType())
                .build();
    }

    public static AddressResponseDto toResDTO(Address address, Long entityId) {
        return AddressResponseDto.builder()
                .addressId(address.getId())
                .entityId(entityId)
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .cityVillage(address.getCityVillage())
                .pinCode(address.getPinCode())
                .landmark(address.getLandmark())
                .state(address.getState())
                .country(address.getCountry())
                .addressType(address.getAddressType())
                .entityType(address.getEntityType())
                .build();
    }

    public static List<AddressResponseDto> toResDTO(List<Address> addresses, Long entityId) {
        List<AddressResponseDto> addressResponseDtos = new ArrayList<>();
        for (Address address : addresses) {
            addressResponseDtos.add(toResDTO(address, entityId));
        }
        return addressResponseDtos;
    }

    public static void updateAddressDetails(Address address, AddressRequestDto addressRequestDto) {
        address.setAddressLine1(addressRequestDto.getAddressLine1());
        address.setAddressLine2(addressRequestDto.getAddressLine2());
        address.setCityVillage(addressRequestDto.getCityVillage());
        address.setPinCode(addressRequestDto.getPinCode());
        address.setLandmark(addressRequestDto.getLandmark());
        address.setState(addressRequestDto.getState());
        address.setCountry(addressRequestDto.getCountry());
        address.setAddressType(addressRequestDto.getAddressType());
        address.setEntityType(addressRequestDto.getEntityType());
    }
}
