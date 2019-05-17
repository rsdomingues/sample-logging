package com.fooapp.cart.usecase;


import com.fooapp.cart.conf.ff4j.Features;
import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.domain.Item;
import com.fooapp.cart.gateway.CartGateway;
import lombok.extern.slf4j.Slf4j;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Collection;
import java.util.Optional;

import static com.fooapp.cart.conf.ff4j.Features.MERGE_ITEM;
import static com.fooapp.cart.conf.ff4j.Features.USE_REDIS;
import static com.google.common.base.Preconditions.checkNotNull;
import static net.logstash.logback.argument.StructuredArguments.value;

@Slf4j
@Component
public class AddItemToCart {

    private NewCart newCart;

    private ValidateItem validateItem;

    private FF4j featureGateway;

    private CartGateway cartGateway;

    @Autowired
    public AddItemToCart(NewCart newCart, ValidateItem validateItem, FF4j ff4j, CartGateway cartGateway) {
        this.newCart = newCart;
        this.validateItem = validateItem;
        this.featureGateway = ff4j;
        this.cartGateway = cartGateway;

    }

    public Cart execute(Cart cart, Item item) {
        cart = getCart(Optional.ofNullable(cart));
        Collection<Item> items = cart.getItems();
        checkNotNull(items, "invalid cart");

        log.info("Adding item to cart {}", value("cartId", cart.getCartId()));

        validateItem.execute(item);
        this.mergeItemToCart(item, items);
        this.updateCart(cart);

        log.info("Item {} added to cart {}", value("item", item.getSku()), value("cartId", cart.getCartId()));

        return cart;
    }

    private void updateCart(Cart cart) {
        String hash = DigestUtils.md5DigestAsHex(cart.toString().getBytes());

        log.info("Updating hash for cart {}, from {} to {}",
                value("cartId", cart.getCartId()),
                value("oldHash", cart.getCartHash()),
                value("newHash", hash));

        cart.setCartHash(hash);

        if(featureGateway.check(USE_REDIS.getKey())){
            cartGateway.saveCart(cart);
        }
    }

    private void mergeItemToCart(Item item, Collection<Item> items) {
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
    }

    private Cart getCart(Optional<Cart> oCart) {
        Cart cart = oCart.orElse(newCart.execute());

        if(featureGateway.check(USE_REDIS.getKey())) {
            oCart = cartGateway.getCart(cart.getCartId());
        }

        return oCart.orElse(cart);
    }

    private boolean isMergeEnabled() {
        return featureGateway.check(MERGE_ITEM.getKey());
    }
}
