package com.consultancy.education.validations;

import com.consultancy.education.model.Student;
import com.consultancy.education.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

public class StudentValidations {

    private final StudentRepository studentRepository;

    public StudentValidations(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

//    public String isUsernameUnique(String username, Long id) {
//        Student student = studentRepository.findByUsername(username);
//        if(student != null) {
//            if(id != null){
//                if(student.getId().equals(id)) {
//                    return null;
//                }
//                else{
//                    return "username is already registered";
//                }
//            }
//            else{
//                return "username is already registered";
//            }
//        }
//        return null;
//    }
//
//    public String isEmailUnique(String email, Long id) {
//        Student student = studentRepository.findByEmail(email);
//        if(student != null) {
//            if(id != null){
//                if(student.getId().equals(id)) {
//                    return null;
//                }
//                else{
//                    return "email is already registered";
//                }
//            }
//            else{
//                return "email is already registered";
//            }
//        }
//        return null;
//    }
//
//    public String isPhoneUnique(String phone, Long id) {
//        Student student = studentRepository.findByPhoneNumber(phone);
//        if(student != null) {
//            if(id != null){
//                if(student.getId().equals(id)) {
//                    return null;
//                }
//                else{
//                    return "phone number is already registered";
//                }
//            }
//            else{
//                return "phone number is already registered";
//            }
//        }
//        return null;
//    }

    public String isAadhaarUnique(String aadhaar, Long id) {
//        Student student = studentRepository.findByAadhaarNumber(aadhaar);
//        if(student != null) {
//            if(id != null){
//                if(student.getId().equals(id)) {
//                    return null;
//                }
//                else{
//                    return "Aadhaar number is already registered";
//                }
//            }
//            else{
//                return "Aadhaar number is already registered";
//            }
//        }
        return null;
    }

    public String isPassportUnique(String passport, Long id) {
//        Student student = studentRepository.findByPassportNumber(passport);
//        if(student != null) {
//            if(id != null){
//                if(student.getId().equals(id)) {
//                    return null;
//                }
//                else{
//                    return "Passport number is already registered";
//                }
//            }
//            else{
//                return "Passport number is already registered";
//            }
//        }
        return null;
    }

//    public List<String> checkStudentValidation(Student student) {
//
//        List<String> errors = new ArrayList<>();
//
//        if(isUsernameUnique(student.getUsername(), null)!=null) {
//            errors.add("username is already registered");
//        }
//        if(isEmailUnique(student.getEmail(), null)!=null) {
//            errors.add("email is already registered");
//        }
//        if(isPhoneUnique(student.getPhoneNumber(), null)!=null) {
//            errors.add("phone number is already registered");
//        }
////        if(isAadhaarUnique(student.getAadhaarNumber(), null)!=null) {
////            errors.add("aadhaar is already registered");
////        }
////        if(isPassportUnique(student.getPassportNumber(), null)!=null) {
////            errors.add("passport is already registered");
////        }
//
//        return errors;
//    }
//
//    public List<String> checkStudentValidationExclude(Student student, Long id) {
//
//        List<String> errors = new ArrayList<>();
//
//        if(isUsernameUnique(student.getUsername(), id)!=null) {
//            errors.add("username is already registered");
//        }
//        if(isEmailUnique(student.getEmail(), id)!=null) {
//            errors.add("email is already registered");
//        }
//        if(isPhoneUnique(student.getPhoneNumber(), id)!=null) {
//            errors.add("phone number is already registered");
//        }
////        if(isAadhaarUnique(student.getAadhaarNumber(), id)!=null) {
////            errors.add("aadhaar is already registered");
////        }
////        if(isPassportUnique(student.getPassportNumber(), id)!=null) {
////            errors.add("passport is already registered");
////        }
//
//        return errors;
//    }
//
//    public String validatePhoneNumbers(Student student) {
//        if (student.getPhoneNumber().equals(student.getAlternatePhoneNumber())) {
//            return "Primary phone number and alternate phone number cannot be the same";
//        }
//        return null;
//    }
}
