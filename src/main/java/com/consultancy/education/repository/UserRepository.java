package com.consultancy.education.repository;

import com.consultancy.education.enums.Role;
import com.consultancy.education.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phone);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    User findByUsername(String username);

    List<User> findByRole(Role role);

    Page<User> findByRole(Role role, Pageable pageable);
}
