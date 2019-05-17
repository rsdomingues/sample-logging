package com.fooapp.cart.gateway.http;


import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.gateway.http.jsons.CartRequest;
import com.fooapp.cart.usecase.AddItemToCart;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@Api(value = "/api/v1/cart", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CartController {


    private AddItemToCart addItemToCart;

    @Autowired
    public CartController(AddItemToCart addItemToCart){
        this.addItemToCart = addItemToCart;
    }

    @ApiOperation(value = "Echo request")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Item add"),
                    @ApiResponse(code = 403, message = "Feature disabled"),
                    @ApiResponse(code = 408, message = "Request Timeout"),
                    @ApiResponse(code = 422, message = "Problem with cart or item"),
                    @ApiResponse(code = 500, message = "Internal Server Error")
            }
    )
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cart> addItem(@RequestBody CartRequest request) {
        Optional<Cart> cartReq = Optional.ofNullable(request.getCart());
        log.info("Request to add item to cart {} received", value("cartId", cartReq.orElseGet(Cart::new).getCartId()));

        Cart cart = addItemToCart.execute(request.getCart(), request.getItem());

        return ResponseEntity.ok(cart);
    }
}
