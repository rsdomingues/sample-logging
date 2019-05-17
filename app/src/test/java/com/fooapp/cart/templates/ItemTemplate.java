package com.fooapp.cart.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.fooapp.cart.domain.Item;

public class ItemTemplate implements TemplateLoader {

    @Override
    public void load() {
        Fixture.of(Item.class)
                .addTemplate("MacBook", new Rule() {
                    {
                        add("sku", "123-43214-8982");
                        add("amount", 1);
                        add("unitPrice", 1699.00D);
                    }
                })
                .addTemplate("Iphone XS", new Rule() {
                    {
                        add("sku", "123-43214-89XS");
                        add("amount", 2);
                        add("unitPrice", 999.00D);
                    }
                })
                .addTemplate("Iphone XR", new Rule() {
                    {
                        add("sku", "123-43214-89XS");
                        add("amount", 1);
                        add("unitPrice", 799.00D);
                    }
                })
                .addTemplate("Answer", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", 42);
                        add("unitPrice", 42D);
                    }
                })
                .addTemplate("Null SKU", new Rule() {
                    {
                        add("sku", null);
                        add("amount", 42);
                        add("unitPrice", 42D);
                    }
                })
                .addTemplate("Empty SKU", new Rule() {
                    {
                        add("sku", "");
                        add("amount", 42);
                        add("unitPrice", 42D);
                    }
                })
                .addTemplate("Null Amount", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", null);
                        add("unitPrice", 42D);
                    }
                })
                .addTemplate("Zero Amount", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", 0);
                        add("unitPrice", 42D);
                    }
                })
                .addTemplate("Less Then Zero Amount", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", -6);
                        add("unitPrice", 42D);
                    }
                })
                .addTemplate("Null Price", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", 1);
                        add("unitPrice", null);
                    }
                })
                .addTemplate("Zero Price", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", 1);
                        add("unitPrice", 0D);
                    }
                })
                .addTemplate("Less Then Zero Price", new Rule() {
                    {
                        add("sku", "424-24242-4242");
                        add("amount", 1);
                        add("unitPrice", -13D);
                    }
                });
    }
}
