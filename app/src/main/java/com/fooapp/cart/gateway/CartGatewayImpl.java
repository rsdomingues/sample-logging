package com.fooapp.cart.gateway;

import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.gateway.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartGatewayImpl implements CartGateway {

    private CartRepository cartRepository;

    @Autowired
    public CartGatewayImpl(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    }

    @Override
    public Optional<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    @Override
    public void saveCart(Cart cart) {
        this.cartRepository.save(cart);
    }
}
