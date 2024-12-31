package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.example.demo.controllers.ItemController;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.assertEquals;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindItemsReturnsItemsWhenItemsExist() {

        Item item1 = new Item();

        Item item2 = new Item();

        List<Item> listOfItems = Arrays.asList(item1, item2);

        List<Item> itemsFound = itemRepository.findAll();

        when(itemsFound).thenReturn(listOfItems);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertNotNull(response.getBody());

        assertEquals(200, response.getStatusCodeValue());

        assertEquals(listOfItems, response.getBody());
    }

    @Test
    public void testFindItemByIdReturnsItemWhenFound() {

        Item item1 = new Item();

        item1.setId(1L);

        Optional<Item> itemFound = itemRepository.findById(1L);

        when(itemFound).thenReturn(Optional.of(item1));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertNotNull(response.getBody());

        assertEquals(200, response.getStatusCodeValue());

        assertEquals(item1, response.getBody());
    }

    @Test
    public void testFindItemByIdReturns404WhenItemNotFound() {

        Optional emptyOptional = Optional.empty();

        Optional<Item> itemFound = itemRepository.findById(anyLong());

        when(itemFound).thenReturn(emptyOptional);

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testFindItemsByNameReturnsItemsWhenItemsExist() {

        Item item1 = new Item();

        Item item2 = new Item();

        List<Item> listOfItems = Arrays.asList(item1, item2);

        List<Item> itemsFound = itemRepository.findByName("ItemA");

        when(itemsFound).thenReturn(listOfItems);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("ItemA");

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertNotNull(response.getBody());

        assertEquals(200, response.getStatusCodeValue());

        assertEquals(listOfItems, response.getBody());
    }

    @Test
    public void testFindItemsByNameReturns404WhenItemsNotFound() {

        List<Item> newItems = null;

        List<Item> itemsFound = itemRepository.findByName(anyString());

        when(itemsFound).thenReturn(newItems);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("ItemB");

        assertNotNull(response);

        assertNotNull(response.getStatusCodeValue());

        assertEquals(404, response.getStatusCodeValue());
    }
}
