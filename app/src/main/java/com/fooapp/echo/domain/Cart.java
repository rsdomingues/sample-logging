package com.fooapp.echo.domain;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Cart {

    private String cartId;
    private String cartHash;
    private Collection<Item> items;
}
