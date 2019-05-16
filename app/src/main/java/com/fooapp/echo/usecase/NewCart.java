package com.fooapp.echo.usecase;

import com.fooapp.echo.domain.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Slf4j
@Component
public class NewCart {

    public Cart execute() {

        return Cart.builder()
                .cartHash(null)
                .cartId(UUID.randomUUID().toString())
                .items(new HashSet<>())
                .build();
    }
}
