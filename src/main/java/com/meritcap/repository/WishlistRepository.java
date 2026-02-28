package com.meritcap.repository;

import com.meritcap.model.Wishlist;
import com.meritcap.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByStudent(Student student);
    Optional<Wishlist> findByStudentId(Long studentId);
}
