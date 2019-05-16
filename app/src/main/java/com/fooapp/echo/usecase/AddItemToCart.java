package com.fooapp.echo.usecase;


import com.fooapp.echo.domain.Cart;
import com.fooapp.echo.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@Component
public class AddItemToCart {

    private NewCart newCart;

    private ValidateItem validateItem;

    @Autowired
    public AddItemToCart(NewCart newCart, ValidateItem validateItem) {
        this.newCart = newCart;
        this.validateItem = validateItem;

    }

    public Cart execute(Cart cart, Item item) {
        cart = (cart == null) ? this.newCart.execute() : cart;
        Collection<Item> items = cart.getItems();
        checkNotNull(items, "invalid cart");

        log.info("Adding item to cart {}", value("cartId", cart.getCartId()));

        validateItem.execute(item);

        if(items.contains(item)){
            items.forEach(cartItem -> {
                if(cartItem.equals(item)){
                    cartItem.setAmount(cartItem.getAmount() + item.getAmount());
                    cartItem.setUnitPrice(item.getUnitPrice());
                }
            });
        } else {
            items.add(item);
        }

        log.info("Item {} added to cart {}", value("item", item.getSku()), value("cartId", cart.getCartId()));

        String hash = DigestUtils.md5DigestAsHex(cart.toString().getBytes());

        log.info("Updating hash for cart {}, from {} to {}",
                value("cartId", cart.getCartId()),
                value("oldHash", cart.getCartHash()),
                value("newHash", hash));

        cart.setCartHash(hash);
        return cart;
    }
}
