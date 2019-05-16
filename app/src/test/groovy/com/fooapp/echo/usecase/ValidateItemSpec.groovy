package com.fooapp.echo.usecase

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.fooapp.echo.domain.Item
import spock.lang.Specification

class ValidateItemSpec extends Specification {

    private ValidateItem validateItem

    def setup() {
        validateItem = new ValidateItem()
        FixtureFactoryLoader.loadTemplates("com.fooapp.echo.templates");
    }

    def "valid item"() {
        given: "i a valid Item"
        def item = Fixture.from(Item.class).gimme("Answer");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "it create a empty new cart"
        noExceptionThrown()
    }

    def "null SKU"() {
        given: "i a invalid Item with sku null"
        def item = Fixture.from(Item.class).gimme("Null SKU");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("SKU is required;")
    }

    def "Empty SKU"() {
        given: "i a invalid Item with empty sku"
        def item = Fixture.from(Item.class).gimme("Empty SKU");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("SKU is required;")
    }

    def "null amount"() {
        given: "i a invalid Item with null amount"
        def item = Fixture.from(Item.class).gimme("Null Amount");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Amount is required;")
    }

    def "zero amount"() {
        given: "i a valid Item zero amount"
        def item = Fixture.from(Item.class).gimme("Zero Amount");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Amount must be greater then zero;")
    }

    def "less zero amount"() {
        given: "i a valid Item with less then zero amount"
        def item = Fixture.from(Item.class).gimme("Less Then Zero Amount");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Amount must be greater then zero;")
    }

    def "null unit price"() {
        given: "i a invalid Item with null unit price"
        def item = Fixture.from(Item.class).gimme("Null Price");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Unit Price is required;")
    }

    def "zero unit price"() {
        given: "i a valid Item zero unit price"
        def item = Fixture.from(Item.class).gimme("Zero Price");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Unit Price must be greater then zero;")
    }

    def "less zero unit price"() {
        given: "i a valid Item with less then zero unit price"
        def item = Fixture.from(Item.class).gimme("Less Then Zero Price");

        when: "i try to validate it"
        validateItem.execute(item)

        then: "i get an exceptio"
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Unit Price must be greater then zero;")
    }
}
