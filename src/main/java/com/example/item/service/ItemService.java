package com.example.item.service;

import com.example.item.exception.InvalidInputException;
import com.example.item.exception.ItemNotFoundException;
import com.example.item.model.Item;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ItemService {

    private final Map<String, Item> itemStore = new ConcurrentHashMap<>();

    public Item createItem(Item item) {
        validateItem(item, true);

        if (itemStore.containsKey(item.getItemId())) {
            throw new InvalidInputException("Item with itemId " + item.getItemId() + " already exists");
        }

        itemStore.put(item.getItemId(), item);
        return item;
    }

    public Item getItem(String itemId) {
        Item item = itemStore.get(itemId);
        if (item == null) {
            throw new ItemNotFoundException("Item with itemId " + itemId + " not found");
        }
        return item;
    }

    public Item updateItem(String itemId, Item item) {
        if (!itemStore.containsKey(itemId)) {
            throw new ItemNotFoundException("Item with itemId " + itemId + " not found");
        }

        validateItem(item, false);

        Item existingItem = itemStore.get(itemId);

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }

        itemStore.put(itemId, existingItem);
        return existingItem;
    }

    public void deleteItem(String itemId) {
        if (!itemStore.containsKey(itemId)) {
            throw new ItemNotFoundException("Item with itemId " + itemId + " not found");
        }
        itemStore.remove(itemId);
    }

    private void validateItem(Item item, boolean isCreate) {
        if (item == null) {
            throw new InvalidInputException("Item cannot be null");
        }

        if (isCreate) {
            if (item.getItemId() == null || item.getItemId().trim().isEmpty()) {
                throw new InvalidInputException("itemId is mandatory");
            }
            if (item.getName() == null || item.getName().trim().isEmpty()) {
                throw new InvalidInputException("name is mandatory");
            }
            if (item.getDescription() == null || item.getDescription().trim().isEmpty()) {
                throw new InvalidInputException("description is mandatory");
            }
        } else {
            if (item.getName() != null && item.getName().trim().isEmpty()) {
                throw new InvalidInputException("name cannot be empty");
            }
            if (item.getDescription() != null && item.getDescription().trim().isEmpty()) {
                throw new InvalidInputException("description cannot be empty");
            }
        }
    }
}