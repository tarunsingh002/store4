package com.ecommerce.store.service;

import com.ecommerce.store.entity.Product;
import com.ecommerce.store.entity.User;
import com.ecommerce.store.entity.WishList;
import com.ecommerce.store.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WishListService {
    @Autowired
    private WishListRepository repository;

    @Autowired
    private ProductService productService;

    public String addProduct(User user, Product product) {
        WishList wishList = repository.findByUser(user);
        Set<Product> products = wishList.getProducts();
        products.add(product);
        wishList.setProducts(products);
        repository.save(wishList);
        return "Product added to Wishlist";
    }

    public Set<Product> getWishList(User user) {
        return repository
                .findByUser(user)
                .getProducts();
    }


    public String removeProduct(int id, User user) {
        WishList wishList = repository.findByUser(user);
        Set<Product> products = wishList.getProducts();
        products.remove(productService.getProductById(id));
        wishList.setProducts(products);
        repository.save(wishList);
        return "Product removed from wishlist";
    }

    public List<WishList> getAllWishLists() {
        return repository.findAll();
    }

    public String isProductWishListed(int id, User user) {
        WishList wishList = repository.findByUser(user);
        Set<Product> products = wishList.getProducts();

        boolean isWishlisted = false;

        Set<Product> wp = products.stream().filter(p -> p.getProductId() == id).collect(Collectors.toSet());
        if (wp.size() > 0)
            return "Product is wishlisted";
        else
            return "Product is not wishlisted";
    }

    public long numberOfTimesWishListed(int id) {
        List<WishList> wishlists = repository.findAll();
        long t = 0;
        t = wishlists.stream().filter(w -> {
            if (w.getProducts().stream().filter(p -> p.getProductId() == id).count() > 0)
                return true;
            else return false;
        }).count();
        for (int i = 0; i < wishlists.size(); i++) {
            t += wishlists.get(i).getProducts().stream().filter(p -> p.getProductId() == id).count();
        }
        return t;
    }

    public void removeFromAllWishLists(int id) {
        List<WishList> wishlists = repository.findAll();

        wishlists.stream().filter(w -> {
            if (w.getProducts().stream().filter(p -> p.getProductId() == id).count() > 0)
                return true;
            else return false;
        }).forEach(w -> {
            Set<Product> products = w.getProducts();
            products.remove(productService.getProductById(id));
            w.setProducts(products);
            repository.save(w);
        });
    }
}
