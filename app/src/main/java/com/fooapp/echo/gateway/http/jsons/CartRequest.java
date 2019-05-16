package com.fooapp.echo.gateway.http.jsons;

import com.fooapp.echo.domain.Cart;
import com.fooapp.echo.domain.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {

    private Cart cart;

    private Item item;
}
