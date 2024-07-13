package com.ecommerce.store.repository;

import com.ecommerce.store.entity.User;
import com.ecommerce.store.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Integer> {

    public WishList findByUser(User user);
}
