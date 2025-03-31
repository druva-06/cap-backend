package com.consultancy.education.service.impl;

import com.consultancy.education.DTOs.requestDTOs.user.UserRequestDto;
import com.consultancy.education.DTOs.responseDTOs.user.UserResponseDto;
import com.consultancy.education.exception.AlreadyExistException;
import com.consultancy.education.exception.NotFoundException;
import com.consultancy.education.exception.ValidationException;
import com.consultancy.education.model.User;
import com.consultancy.education.repository.UserRepository;
import com.consultancy.education.service.UserService;
import com.consultancy.education.transformer.UserTransformer;
import com.consultancy.education.validations.UserValidations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
//        boolean existsByEmail = userRepository.existsByEmail(userRequestDto.getEmail());
//        boolean existsByPhoneNumber = userRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber());
//        if(existsByEmail || existsByPhoneNumber){
//            List<String> errors = new ArrayList<>();
//            if(existsByEmail){
//                errors.add("Email already exists");
//            }
//            if(existsByPhoneNumber){
//                errors.add("Phone number already exists");
//            }
//            throw new AlreadyExistException(errors);
//        }
//        User user = UserTransformer.toEntity(userRequestDto);
//        user = userRepository.save(user);
//        return UserTransformer.toResDTO(user);
        return null;
    }

    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto, Long userId) {
//        if(userRepository.findById(userId).isPresent()){
//            User user1 =  userRepository.findByEmail(userRequestDto.getEmail());
//            User user2 =  userRepository.findByPhoneNumber(userRequestDto.getPhoneNumber());
//            List<String> errors = UserValidations.checkEmailAndPhoneExist(userId, user1, user2);
//            if (!errors.isEmpty()) {
//                throw new AlreadyExistException(errors);
//            }
//            User user = userRepository.findById(userId).get();
//            UserTransformer.updateUser(user, userRequestDto);
//            user = userRepository.save(user);
//            return UserTransformer.toResDTO(user);
//        }
//        else{
//            throw new NotFoundException("User not found");
//        }
        return null;
    }

    @Override
    public UserResponseDto getUser(Long userId) {
//        if(userRepository.findById(userId).isPresent()){
//            return UserTransformer.toResDTO(userRepository.findById(userId).get());
//        }
//        else{
//            throw new NotFoundException("User not found");
//        }
        return null;
    }

}
