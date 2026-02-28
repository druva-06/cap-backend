package com.meritcap.repository;

import com.meritcap.model.WishlistItem;
import com.meritcap.model.Wishlist;
import com.meritcap.model.CollegeCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByWishlist(Wishlist wishlist);

    boolean existsByWishlistAndCollegeCourse(Wishlist wishlist, CollegeCourse collegeCourse);

    Optional<WishlistItem> findByWishlistAndCollegeCourse(Wishlist wishlist, CollegeCourse collegeCourse);
}
