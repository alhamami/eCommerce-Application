package com.example.demo.controller;

import com.example.demo.model.requests.ModifyCartRequest;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
public class CartControllerTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartController cartController;
    @Mock
    private CartRepository cartRepository;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCartReturnsUpdatedCartWhenItemIsAdded() {

        ModifyCartRequest cartRequest = new ModifyCartRequest();

        cartRequest.setUsername("jalal");

        cartRequest.setItemId(1L);

        cartRequest.setQuantity(7);

        Item item = new Item();

        item.setId(1L);

        item.setName("ItemA");

        BigDecimal price = new BigDecimal("25.00");

        item.setPrice(price);

        User newUser = new User();

        newUser.setUsername("jalal");

        newUser.setCart(new Cart());

        Optional<Item> optionalItem = Optional.of(item);

        when(userRepository.findByUsername("jalal")).thenReturn(newUser);

        when(itemRepository.findById(1L)).thenReturn(optionalItem);

        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testRemoveFromCartReturnsUpdatedCartWhenItemIsRemoved() {

        ModifyCartRequest cartRequest = new ModifyCartRequest();

        cartRequest.setUsername("jalal");

        cartRequest.setItemId(1L);

        cartRequest.setQuantity(7);

        Item item = new Item();

        item.setId(1L);

        item.setName("ItemA");

        BigDecimal price = new BigDecimal("25.00");

        item.setPrice(price);

        User userFound = userRepository.findByUsername("jalal");

        User newUser = new User();

        newUser.setUsername("jalal");

        newUser.setCart(new Cart());

        when(userFound).thenReturn(newUser);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddToCartReturnsNotFoundWhenUserDoesNotExist() {

        User user = null;

        HttpStatus status = HttpStatus.NOT_FOUND;

        ModifyCartRequest cartRequest = new ModifyCartRequest();

        cartRequest.setUsername("ahmed");

        User userFound = userRepository.findByUsername("ahmed");

        when(userFound).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addTocart(cartRequest);

        assertNotNull(response);

        assertNotNull(response.getStatusCode());

        assertEquals(status, response.getStatusCode());

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testRemoveFromCartReturnsNotFoundWhenItemDoesNotExist() {

        HttpStatus status = HttpStatus.NOT_FOUND;

        User userFound = userRepository.findByUsername("jalal");

        ModifyCartRequest cartRequest = new ModifyCartRequest();

        cartRequest.setUsername("jalal");

        cartRequest.setItemId(1L);

        User neUser = new User();

        neUser.setUsername("jalal");

        when(userFound).thenReturn(neUser);

        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(cartRequest);

        assertNotNull(response);

        assertNotNull(response.getStatusCode());

        assertEquals(status, response.getStatusCode());

        verify(cartRepository, never()).save(any(Cart.class));
    }
}
