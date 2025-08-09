package com.wms.service;
import com.wms.dto.ItemDto;
import com.wms.entity.*;
import com.wms.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class InventoryService {
    @Autowired private ItemRepository itemRepository;
    @Autowired private InventoryLogRepository logRepository;
    @Autowired private UserRepository userRepository;
    public List<Item> getAllItems() { return itemRepository.findAll(); }
    public Optional<Item> getItemById(Long id) { return itemRepository.findById(id); }
    @Transactional
    public Item createItem(ItemDto itemDto, String username) {
        if (itemRepository.existsBySku(itemDto.getSku())) throw new IllegalArgumentException("SKU exists");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Item item = new Item();
        item.setName(itemDto.getName()); item.setSku(itemDto.getSku()); item.setQuantity(itemDto.getQuantity());
        item.setLocation(itemDto.getLocation()); item.setCategory(itemDto.getCategory()); item.setThreshold(itemDto.getThreshold());
        Item savedItem = itemRepository.save(item);
        if (savedItem.getQuantity() > 0) logStockChange(savedItem, ActionType.IN, savedItem.getQuantity(), user);
        return savedItem;
    }
    @Transactional
    public Item updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.setName(itemDto.getName()); item.setSku(itemDto.getSku()); item.setLocation(itemDto.getLocation());
        item.setCategory(itemDto.getCategory()); item.setThreshold(itemDto.getThreshold());
        return itemRepository.save(item);
    }
    @Transactional
    public void deleteItem(Long id) { itemRepository.deleteById(id); }
    @Transactional
    public Item receiveStock(Long itemId, int quantity, String username) {
        return updateStock(itemId, quantity, username, ActionType.IN);
    }
    @Transactional
    public Item deductStock(Long itemId, int quantity, String username) {
        return updateStock(itemId, -quantity, username, ActionType.OUT);
    }
    private Item updateStock(Long itemId, int quantity, String username, ActionType actionType) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("Item not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (actionType == ActionType.OUT && item.getQuantity() < Math.abs(quantity)) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        item.setQuantity(item.getQuantity() + quantity);
        Item updatedItem = itemRepository.save(item);
        logStockChange(updatedItem, actionType, Math.abs(quantity), user);
        return updatedItem;
    }
    private void logStockChange(Item item, ActionType action, int qty, User user) {
        InventoryLog log = new InventoryLog();
        log.setItem(item); log.setActionType(action); log.setQuantity(qty);
        log.setUser(user); log.setTimestamp(LocalDateTime.now());
        logRepository.save(log);
    }
    // Add these two methods to InventoryService.java

    public long getTotalDistinctItems() {
        return itemRepository.count();
    }

    public long getTotalItemQuantity() {
        // This uses a custom query to efficiently sum the quantity of all items.
        // If there are no items, it returns 0.
        return itemRepository.getTotalItemQuantity().orElse(0L);
    }
}
