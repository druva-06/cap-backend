package com.meritcap.service.impl;

import com.meritcap.DTOs.requestDTOs.address.AddressRequestDto;
import com.meritcap.DTOs.responseDTOs.address.AddressResponseDto;
import com.meritcap.enums.EntityType;
import com.meritcap.exception.NotFoundException;
import com.meritcap.model.Address;
import com.meritcap.model.Student;
import com.meritcap.repository.AddressRepository;
import com.meritcap.repository.CollegeRepository;
import com.meritcap.repository.StudentRepository;
import com.meritcap.service.AddressService;
import com.meritcap.transformer.AddressTransformer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final StudentRepository studentRepository;

    public AddressServiceImpl(AddressRepository addressRepository, StudentRepository studentRepository, CollegeRepository collegeRepository) {
        this.addressRepository = addressRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public AddressResponseDto addAddress(AddressRequestDto addressRequestDto, Long id) {
        Address address = AddressTransformer.toEntity(addressRequestDto);
        if(addressRequestDto.getEntityType() == EntityType.STUDENT){
            if(studentRepository.findById(id).isPresent()){
                Student student = studentRepository.findById(id).get();
                student.getAddresses().add(address);
                address.setStudent(student);
                addressRepository.save(address);
                return AddressTransformer.toResDTO(address, student.getId());
            }
            throw new NotFoundException("Student not found");
        }
        else{
            throw new NotFoundException("Entity type not found");
        }
    }

    @Override
    public AddressResponseDto updateAddress(AddressRequestDto addressRequestDto, Long id) {
        if(addressRepository.findById(id).isPresent()){
            Address address = addressRepository.findById(id).get();
            AddressTransformer.updateAddressDetails(address, addressRequestDto);
            address = addressRepository.save(address);
            AddressResponseDto addressResponseDto;
            if(address.getEntityType() == EntityType.STUDENT){
                addressResponseDto = AddressTransformer.toResDTO(address, address.getStudent().getId());
            }
            else {
                throw new NotFoundException("Entity type not found");
            }
            return addressResponseDto;
        }
        throw new NotFoundException("Address not found");
    }

    @Override
    public List<AddressResponseDto> getAddress(Long entityId, String entityType) {
        if(entityType.equals("STUDENT")){
            List<Address> addresses = addressRepository.findByStudent_Id(entityId);
            return AddressTransformer.toResDTO(addresses, addresses.get(0).getStudent().getId());
        }
        else {
            throw new NotFoundException("Entity type not found");
        }
    }
}
