package com.wms.service;
import com.wms.dto.*;
import com.wms.entity.*;
import com.wms.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
@Service
public class OrderService {
    @Autowired private OrderRepository orderRepository;
    @Autowired private ItemRepository itemRepository;
    @Autowired private OrderItemRepository orderItemRepository;
    @Autowired private InventoryService inventoryService;
    public List<Order> getAllOrders() { return orderRepository.findAll(); }
    public Optional<Order> getOrderById(Long id) { return orderRepository.findById(id); }
    @Transactional
    public Order createOrder(OrderRequestDto orderRequest, String username) {
        Order order = new Order(orderRequest.getCustomerName());
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDto itemRequest : orderRequest.getItems()) {
            Item item = itemRepository.findById(itemRequest.getItemId()).orElseThrow(() -> new EntityNotFoundException("Item not found"));
            inventoryService.deductStock(item.getId(), itemRequest.getQuantity(), username);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); orderItem.setItem(item); orderItem.setQuantity(itemRequest.getQuantity());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        validateStatusChange(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
    private void validateStatusChange(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.SHIPPED || current == OrderStatus.CANCELLED) throw new IllegalStateException("Order is already final.");
        if (current == OrderStatus.PENDING && (next != OrderStatus.PICKED && next != OrderStatus.CANCELLED)) throw new IllegalStateException("Pending order can only become Picked or Cancelled.");
        if (current == OrderStatus.PICKED && (next != OrderStatus.SHIPPED && next != OrderStatus.CANCELLED)) throw new IllegalStateException("Picked order can only become Shipped or Cancelled.");
    }
    
    public List<Order> getRecentOrders(int limit) {
        // This finds orders sorted by date in descending order and limits the result.
        return orderRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "date"))).getContent();
    }
}
