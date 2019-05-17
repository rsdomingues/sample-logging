package com.fooapp.cart.gateway;

import com.fooapp.cart.domain.Cart;

import java.util.Optional;

public interface CartGateway {

    Optional<Cart> getCart(String cartId);

    void saveCart(Cart cart);
}
