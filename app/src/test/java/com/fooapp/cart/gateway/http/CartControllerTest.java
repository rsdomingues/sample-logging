package com.fooapp.cart.gateway.http;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fooapp.cart.domain.Cart;
import com.fooapp.cart.gateway.http.jsons.CartRequest;
import com.fooapp.cart.usecase.AddItemToCart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class CartControllerTest extends AbstractHttpTest {

    private MockMvc mockMvc;

    @Mock
    private AddItemToCart addItemToCart;

    @InjectMocks
    private CartController cartController;


    @Before
    public void setup() {
        mockMvc = buildMockMvcWithBusinessExecptionHandler(cartController);
        FixtureFactoryLoader.loadTemplates("com.fooapp.echo.templates");
    }

    @Test
    public void returnOkForANullCart() throws Exception {
        //GIVEN a request with a new cart
        CartRequest cartRequest = Fixture.from(CartRequest.class).gimme("new cart");

        //AND a valid cool cart
        Cart coolCart = Fixture.from(Cart.class).gimme("Cool");
        when(addItemToCart.execute(any(), any())).thenReturn(coolCart);

        //WHEN the controller is called
        MvcResult result = mockMvc
            .perform(
                post("/api/v1/cart")
                        .content(asJsonString(cartRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))


        //THEN no errors are thrown and the status returned is OK
            .andExpect(status().isOk())
            .andReturn();

        // AND i get the Cool card back
        Cart responseCart = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Cart.class);
        assertThat(responseCart).isNotNull();
        assertThat(responseCart.getCartId()).isEqualTo(coolCart.getCartId());
        assertThat(result.getResolvedException()).isNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    public void returnOkWhenUncoolRequest() throws Exception {
        //GIVEN an cool Request
        CartRequest cartRequest = Fixture.from(CartRequest.class).gimme("add iPhone to existing cart");

        //AND a valida apple cart
        Cart coolCart = Fixture.from(Cart.class).gimme("Apple");
        when(addItemToCart.execute(any(), any())).thenReturn(coolCart);

        //WHEN the controller is called
        MvcResult result = mockMvc
                .perform(
                        post("/api/v1/cart")
                                .content(asJsonString(cartRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))


                //THEN no errors are thrown and the status returned is OK
                .andExpect(status().isOk())
                .andReturn();

        // AND i get the Cool card back
        Cart responseCart = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Cart.class);
        assertThat(responseCart).isNotNull();
        assertThat(responseCart.getCartId()).isEqualTo(coolCart.getCartId());
        assertThat(result.getResolvedException()).isNull();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
