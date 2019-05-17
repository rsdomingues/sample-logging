package com.fooapp.cart.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@RedisHash("Cart")
public class Cart {

    @Id
    private String cartId;
    private String cartHash;
    private Collection<Item> items;
}
