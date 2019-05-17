package com.fooapp.cart.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.domain.Item;
import com.fooapp.cart.gateway.http.jsons.CartRequest;

public class CartRequestTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(CartRequest.class)
                .addTemplate("add iPhone to existing cart", new Rule() {
                    {
                        add("cart", one(Cart.class, "Apple"));
                        add("item", one(Item.class, "Iphone XS"));
                    }
                })
                .addTemplate("new cart", new Rule() {
                    {
                        add("cart", null);
                        add("item", one(Item.class, "Answer"));
                    }
                });
    }
}
