package com.meritcap.validations;

import com.meritcap.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserValidations {

    public static List<String> checkEmailAndPhoneExist(Long userId, User user1, User user2) {
        List<String> errors = new ArrayList<>();
        if (user1 != null && user2 != null) {
            if (!Objects.equals(user1.getId(), userId)) {
                errors.add("Email already exists");
            }
            if (!Objects.equals(user2.getId(), userId)) {
                errors.add("Phone Number already exists");
            }
        } else if (user1 != null) {
            if (!Objects.equals(user1.getId(), userId)) {
                errors.add("Email already exists");
            }
        } else if (user2 != null) {
            if (!Objects.equals(user2.getId(), userId)) {
                errors.add("Phone Number already exists");
            }
        }
        return errors;
    }
}
