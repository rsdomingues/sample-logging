package com.fooapp.cart.gateway.http.jsons;

import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.domain.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {

    private Cart cart;

    private Item item;
}
