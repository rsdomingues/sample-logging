package com.fooapp.cart.conf.ff4j

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.fooapp.cart.domain.Cart
import com.fooapp.cart.domain.Item
import com.fooapp.cart.gateway.CartGateway
import com.fooapp.cart.usecase.AddItemToCart
import com.fooapp.cart.usecase.NewCart
import com.fooapp.cart.usecase.ValidateItem
import org.ff4j.FF4j
import spock.lang.Specification

class AddItemToCartSpec extends Specification {

    private NewCart newCart

    private ValidateItem validateItem

    private AddItemToCart addItemToCart

    private CartGateway cartGateway

    private FF4j ff4j;

    def setup() {
        newCart = Mock(NewCart)
        validateItem = Mock(ValidateItem)
        cartGateway = Mock(CartGateway)
        ff4j = Mock(FF4j)

        addItemToCart = new AddItemToCart(newCart, validateItem, ff4j, cartGateway)
        FixtureFactoryLoader.loadTemplates("com.fooapp.cart.templates");
    }

    def "Add item to new cart"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("Answer")

        and: "new cart usecase return "
        Cart cartNew = Fixture.from(Cart.class).gimme("New")
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
        0 * cartGateway.getCart(_)
        0 * cartGateway.saveCart(_)
    }

    def "Add item to existing  cart"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook")
        def appleCart = Fixture.from(Cart.class).gimme("Apple 1")
        def appleCartHash = appleCart.getCartHash()

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 2
        cart.getItems().contains(item)
        0 * cartGateway.getCart(_)
        0 * cartGateway.saveCart(_)
    }

    def "Add item to cart with multiple itens"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("Answer")
        def appleCart = Fixture.from(Cart.class).gimme("Apple")
        def appleCartHash = appleCart.getCartHash()

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 3
        cart.getItems().contains(item)
        0 * cartGateway.getCart(_)
        0 * cartGateway.saveCart(_)
    }

    def "Add item to cart with matching items with MERGE_ITEM disabled"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook")
        def appleCart = Fixture.from(Cart.class).gimme("Apple Not Merge")
        def appleCartHash = appleCart.getCartHash()

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        and: "the feature MERGE_ITEM is disabled"
        ff4j.check(_) >> false

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 3
        cart.getItems().contains(item)
        cart.getItems().every { it.amount == 1 }
        0 * cartGateway.getCart(_)
        0 * cartGateway.saveCart(_)
    }

    def "Add item to cart with matching items with MERGE_ITEM enabled"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook")
        def appleCart = Fixture.from(Cart.class).gimme("Apple")
        def appleCartHash = appleCart.getCartHash()

        and: "the feature MERGE_ITEM is enabled"
        ff4j.check(Features.MERGE_ITEM.key) >> true

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 2
        cart.getItems().contains(item)
        cart.getItems().every { it.amount == 2 }
        0 * cartGateway.getCart(_)
        0 * cartGateway.saveCart(_)
    }


    def "Add item to cart with matching items with USE_REDIS enabled with cart on cache"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook")
        def appleCart = Fixture.from(Cart.class).gimme("Apple Not Merge")
        def appleCartHash = appleCart.getCartHash()

        and: "the feature USE_REDIS is enabled"
        ff4j.check(Features.USE_REDIS.key) >> true

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 3
        cart.getItems().contains(item)
        cart.getItems().every { it.amount == 1 }
        1 * cartGateway.getCart(_) >> Optional.of(appleCart)
        1 * cartGateway.saveCart(_)
    }

    def "Add item to cart with matching items with USE_REDIS enabled with no cart on cache"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("MacBook")
        def appleCart = Fixture.from(Cart.class).gimme("Apple Not Merge")
        def appleCartHash = appleCart.getCartHash()
        newCart.execute() >> Fixture.from(Cart.class).gimme("New")

        and: "the feature USE_REDIS is enabled"
        ff4j.check(Features.USE_REDIS.key) >> true

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        cart.getCartId() == appleCart.getCartId()
        cart.getCartHash() != appleCartHash
        cart.getItems().size() == 3
        cart.getItems().contains(item)
        cart.getItems().every { it.amount == 1 }
        1 * cartGateway.getCart(_) >> Optional.ofNullable(null)
        1 * cartGateway.saveCart(_)
    }

    def "try to add invalid item"() {
        given: "i have no cart and a valid item"
        def item = Fixture.from(Item.class).gimme("Empty SKU")
        def appleCart = Fixture.from(Cart.class).gimme("Apple")
        validateItem.execute(_) >> { throw new IllegalArgumentException() }

        when: "i add the new item"
        def cart = addItemToCart.execute(appleCart, item)

        then: "it add the item and generate a new hash"
        thrown(IllegalArgumentException)
        0 * cartGateway.getCart(_)
        0 * cartGateway.saveCart(_)
    }
}
