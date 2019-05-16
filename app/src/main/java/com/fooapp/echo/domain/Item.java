package com.fooapp.echo.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "sku")
public class Item {

    @NotEmpty(message = "SKU is required; ")
    private String sku;

    @NotNull(message = "Amount is required; ")
    @Positive(message = "Amount must be greater then zero; ")
    private Integer amount;

    @NotNull(message = "Unit Price is required; ")
    @Positive(message = "Unit Price must be greater then zero; ")
    private Double unitPrice;
}
