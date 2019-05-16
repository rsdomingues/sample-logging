package com.fooapp.echo.usecase

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.fooapp.echo.domain.Cart
import com.fooapp.echo.domain.Item
import spock.lang.Specification

class AddItemToCartSpec extends Specification {

    private NewCart newCart

    private ValidateItem validateItem

    private AddItemToCart addItemToCart

    def setup() {
        newCart = Mock(NewCart)
        validateItem = Mock(ValidateItem)

        addItemToCart = new AddItemToCart(newCart, validateItem)
        FixtureFactoryLoader.loadTemplates("com.fooapp.echo.templates");
    }

    def "Add item to new cart"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("Answer");

        and: "new cart usecase return "
        def cartNew = Fixture.from(Cart.class).gimme("New");
        newCart.execute() >> cartNew

        when: "i add the new item"
        def cart = addItemToCart.execute(null, item)

        then: "it create a empty new cart"
        cart != null
        cart.getCartId() == cartNew.getCartId()
        cart.cartHash != null
        cart.getCartHash() == "efff1e81e02ac9189ba3d0293de4b305"
        !cart.getItems().isEmpty()
        cart.getItems().contains(item)

    }

    def "Add item to existing  cart"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook");
        def appleCart = Fixture.from(Cart.class).gimme("Apple 1");
        def appleCartHash = appleCart.getCartHash()

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 2
        cart.getItems().contains(item)
    }

    def "Add item to cart with multiple itens"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("Answer");
        def appleCart = Fixture.from(Cart.class).gimme("Apple");
        def appleCartHash = appleCart.getCartHash()

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 3
        cart.getItems().contains(item)
    }

    def "Add item to cart with matching itens"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook");
        def appleCart = Fixture.from(Cart.class).gimme("Apple");
        def appleCartHash = appleCart.getCartHash()

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 2
        cart.getItems().contains(item)
        cart.getItems().every { it.amount == 2 }
    }

    def "try to add invalid item"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("Empty SKU");
        def appleCart = Fixture.from(Cart.class).gimme("Apple");
        validateItem.execute(_) >> { throw new IllegalArgumentException() }

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        thrown(IllegalArgumentException)
    }
}
