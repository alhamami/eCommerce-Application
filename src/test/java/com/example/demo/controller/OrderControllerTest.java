package com.example.demo.controller;

import com.example.demo.controllers.OrderController;

import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;


public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;


    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        user = new User();

        user.setUsername("jalal");

        Cart cart = new Cart();

        cart.setItems(Arrays.asList());

        user.setCart(cart);
    }

    @Test
    public void testSaveOrderReturns200WhenUserIsFound() {

        User foundUser = userRepository.findByUsername("jalal");

        when(foundUser).thenReturn(user);

        UserOrder order = orderRepository.save(any(UserOrder.class));

        UserOrder newOrder = new UserOrder();

        when(order).thenReturn(newOrder);

        ResponseEntity<UserOrder> response = orderController.submit("jalal");

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(200, response.getStatusCodeValue());

        verify(orderRepository, times(1)).save(any(UserOrder.class));
    }

    @Test
    public void testFindOrdersForUserReturns404WhenUserNotFound() {

        User userFound = userRepository.findByUsername("ahmed");

        User notFoundUser = null;

        when(userFound).thenReturn(notFoundUser);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("ahmed");

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }


    @Test
    public void testSaveOrderReturns404WhenUserNotFound() {

        User user = userRepository.findByUsername("ahmed");

        User notFoundUser = null;

        when(user).thenReturn(notFoundUser);

        ResponseEntity<UserOrder> response = orderController.submit("ahmed");

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());

        verify(orderRepository, never()).save(any(UserOrder.class));
    }

    @Test
    public void testFindOrdersForUserReturnsOrdersWhenUserIsFound() {

        User userFound = userRepository.findByUsername("jalal");

        when(userFound).thenReturn(user);

        List<UserOrder> orders = orderRepository.findByUser(user);

        List<UserOrder> newOrders = Collections.singletonList(new UserOrder());

        when(orders).thenReturn(newOrders);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("jalal");

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertNotNull(response.getBody().size());

        assertEquals(200, response.getStatusCodeValue());

        assertEquals(1, response.getBody().size());
    }

}
