package com.fooapp.cart.usecase;


import com.fooapp.cart.conf.ff4j.Features;
import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.domain.Item;
import lombok.extern.slf4j.Slf4j;
import org.ff4j.FF4j;
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

    private FF4j featureGateway;

    @Autowired
    public AddItemToCart(NewCart newCart, ValidateItem validateItem, FF4j ff4j) {
        this.newCart = newCart;
        this.validateItem = validateItem;
        this.featureGateway = ff4j;

    }

    public Cart execute(Cart cart, Item item) {
        cart = (cart == null) ? this.newCart.execute() : cart;
        Collection<Item> items = cart.getItems();
        checkNotNull(items, "invalid cart");

        log.info("Adding item to cart {}", value("cartId", cart.getCartId()));

        validateItem.execute(item);


        if(items.contains(item) && isMergeEnabled()){
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

    private boolean isMergeEnabled() {
        return featureGateway.check(Features.MERGE_ITEM.getKey());
    }
}
