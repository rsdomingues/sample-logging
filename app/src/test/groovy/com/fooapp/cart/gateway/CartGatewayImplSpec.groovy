package com.fooapp.cart.gateway

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.fooapp.cart.domain.Cart
import com.fooapp.cart.domain.Item
import com.fooapp.cart.gateway.repository.CartRepository
import spock.lang.Specification

class CartGatewayImplSpec extends Specification {

    private CartRepository cartRepository

    private CartGateway cartGateway

    def setup() {
        cartRepository = Mock(CartRepository)

        cartGateway = new CartGatewayImpl(cartRepository)
        FixtureFactoryLoader.loadTemplates("com.fooapp.cart.templates");
    }

    def "Save cart"() {
        given: "i have a cart"
        def cart = Fixture.from(Cart.class).gimme("Apple");

        when: "i save"
        cartGateway.saveCart(cart)

        then: "it get no exception, and the gateway is called 1 time"
        noExceptionThrown()
        1 * cartRepository.save(_)
    }

    def "Get cart"() {
        given: "i have saved cart"
        def savedCart = Fixture.from(Cart.class).gimme("Apple");

        when: "i try to get it"
        def cart = cartGateway.getCart("the id")

        then: "i get the apple cart, via 1 call to the repository"
        noExceptionThrown()
        1 * cartRepository.findById(_ as String) >> Optional.of(savedCart)
        cart.get() == savedCart
    }
}