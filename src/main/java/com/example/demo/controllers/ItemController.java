package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {


	private final ItemRepository itemRepository;

	public static final Logger logger = LogManager.getLogger(UserController.class);

	public ItemController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {

		Optional<Item> item = itemRepository.findById(id);

		if (item.isPresent()) {
			logger.info("Item found: "+item.get().toString());
			return ResponseEntity.ok(item.get());
		}

		logger.error("Item not found: "+id);
		return ResponseEntity.notFound().build();

	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);

		if (items != null && !items.isEmpty()) {
			logger.info("Items found: "+ items.size());
			return ResponseEntity.ok(items);
		}else{
			logger.error("Items not found: "+name);
			return ResponseEntity.notFound().build();
		}
	}
	
}
