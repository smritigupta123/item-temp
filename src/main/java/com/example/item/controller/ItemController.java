package com.example.item.controller;

import com.example.item.exception.InvalidInputException;
import com.example.item.exception.ItemNotFoundException;
import com.example.item.model.ErrorResponse;
import com.example.item.model.Item;
import com.example.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody Item item) {
        try {
            Item createdItem = itemService.createItem(item);
            return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid Input", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable String itemId) {
        try {
            Item item = itemService.getItem(itemId);
            return new ResponseEntity<>(item, HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Not Found", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable String itemId, @RequestBody Item item) {
        try {
            Item updatedItem = itemService.updateItem(itemId, item);
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Not Found", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>(new ErrorResponse("Invalid Input", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable String itemId) {
        try {
            itemService.deleteItem(itemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse("Not Found", e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
}