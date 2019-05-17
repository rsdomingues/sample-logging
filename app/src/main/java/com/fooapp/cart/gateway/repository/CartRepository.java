package com.fooapp.cart.gateway.repository;

import com.fooapp.cart.domain.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart, String> {

}
