package com.fooapp.echo.usecase

import spock.lang.Specification


class NewCartSpec extends Specification {

    private NewCart newCart

    def setup() {
        newCart = new NewCart()
    }

    def "Create new cart"() {
        given: "i have no cart"

        when: "when i ask for one new"
        def cart = newCart.execute()

        then: "it create a empty new cart"
        cart.getItems().isEmpty()
        cart.getCartHash() == null
        !cart.getCartId().isEmpty()

    }
}
