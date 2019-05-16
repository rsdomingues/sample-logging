package com.fooapp.echo.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.fooapp.echo.domain.Cart;
import com.fooapp.echo.domain.Item;

import java.util.HashSet;

public class CartTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Cart.class)
                .addTemplate("Apple", new Rule() {
                    {
                        add("cartId", "c6656100-cbff-43cd-a938-a6b1b2d4e867");
                        add("cartHash", "7590f3f2a140264370eedb51c1af595f");
                        add("items", has(2).of(Item.class, "Iphone XS", "MacBook"));
                    }
                })
                .addTemplate("Apple 1", new Rule() {
                    {
                        add("cartId", "c6656100-cbff-43cd-a938-a6b1b2d4e867");
                        add("cartHash", "7590f3f2a140264370eedb51c1af595f");
                        add("items", has(1).of(Item.class, "Iphone XS"));
                    }
                })
                .addTemplate("Cool", new Rule() {
                    {
                        add("cartId", "c6656100-cbff-82sw-a938-a6b1b2d4e867");
                        add("cartHash", "7590f3f2a140264370iwjd51c1af595f");
                        add("items", has(1).of(Item.class, "Answer"));
                    }
                })
                .addTemplate("New", new Rule() {
                    {
                        add("cartId", "c6656100-cbff-82sw-a938-a6b1b2d4e867");
                        add("cartHash", "");
                        add("items", new HashSet<>());
                    }
                });
    }
}
