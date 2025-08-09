package com.wms.controller;

import com.wms.dto.DashboardStatsDto;
import com.wms.dto.ItemDto;
import com.wms.dto.LowStockReportDto; // Make sure this import is present
import com.wms.dto.SignUpRequest;
import com.wms.entity.Item;
import com.wms.entity.Order;
import com.wms.entity.OrderItem;
import com.wms.entity.OrderStatus;
import com.wms.entity.User;
import com.wms.repository.UserRepository;
import com.wms.service.InventoryService;
import com.wms.service.OrderService;
import com.wms.service.ReportsService;
import com.wms.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired private InventoryService inventoryService;
    @Autowired private OrderService orderService;
    @Autowired private ReportsService reportsService;
    @Autowired private UserDetailsServiceImpl userDetailsService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;


    // --- Login and Dashboard ---
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        DashboardStatsDto stats = new DashboardStatsDto();
        stats.setTotalDistinctItems(inventoryService.getTotalDistinctItems());
        stats.setTotalItemQuantity(inventoryService.getTotalItemQuantity());
        stats.setLowStockItemCount(reportsService.getLowStockItemCount());
        stats.setRecentOrders(orderService.getRecentOrders(5));
        model.addAttribute("stats", stats);
        return "dashboard";
    }

    // --- Inventory CRUD Operations ---
    @GetMapping("/web/items")
    public String showItemsPage(Model model) {
        model.addAttribute("items", inventoryService.getAllItems());
        return "items-list";
    }

    @GetMapping("/web/items/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showNewItemForm(Model model) {
        model.addAttribute("item", new Item());
        return "item-form";
    }

    @GetMapping("/web/items/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditItemForm(@PathVariable Long id, Model model) {
        Item item = inventoryService.getItemById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item Id:" + id));
        model.addAttribute("item", item);
        return "item-form";
    }
    
    @PostMapping("/web/items/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveItem(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        try {
            ItemDto itemDto = new ItemDto();
            itemDto.setName(item.getName());
            itemDto.setSku(item.getSku());
            itemDto.setLocation(item.getLocation());
            itemDto.setCategory(item.getCategory());
            itemDto.setThreshold(item.getThreshold());

            if (item.getId() == null) {
                String username = SecurityContextHolder.getContext().getAuthentication().getName();
                itemDto.setQuantity(0);
                inventoryService.createItem(itemDto, username);
                redirectAttributes.addFlashAttribute("success_message", "Item created successfully!");
            } else {
                inventoryService.updateItem(item.getId(), itemDto);
                redirectAttributes.addFlashAttribute("success_message", "Item updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_message", "Error saving item: " + e.getMessage());
        }
        return "redirect:/web/items";
    }

    @PostMapping("/web/items/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inventoryService.deleteItem(id);
            redirectAttributes.addFlashAttribute("success_message", "Item deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_message", "Error deleting item: " + e.getMessage());
        }
        return "redirect:/web/items";
    }

    // --- User Management ---
    @GetMapping("/web/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUsersPage(Model model) {
        model.addAttribute("users", userDetailsService.findAllUsers());
        return "users-list";
    }

    @GetMapping("/web/users/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showNewUserForm(Model model) {
        model.addAttribute("signupRequest", new SignUpRequest());
        return "user-form";
    }

    @PostMapping("/web/users/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveUser(@ModelAttribute("signupRequest") SignUpRequest signUpRequest, RedirectAttributes redirectAttributes) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                throw new Exception("Username is already taken!");
            }
            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                throw new Exception("Email is already in use!");
            }
            User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                com.wms.entity.Role.valueOf(signUpRequest.getRole().toUpperCase())
            );
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("success_message", "User created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error_message", "Failed to create user: " + e.getMessage());
        }
        return "redirect:/web/users";
    }

    @PostMapping("/web/users/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userToDelete = userRepository.findById(id).orElse(null);

        if (userToDelete != null && authentication.getName().equals(userToDelete.getUsername())) {
             redirectAttributes.addFlashAttribute("error_message", "Error: You cannot delete your own account.");
             return "redirect:/web/users";
        }

        userRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success_message", "User deleted successfully!");
        return "redirect:/web/users";
    }

    // --- Order Management ---
    @GetMapping("/web/orders")
    public String showOrdersPage(Model model) {
        List<Order> orders = orderService.getAllOrders().stream()
                .sorted(Comparator.comparing(Order::getDate).reversed())
                .collect(Collectors.toList());
        model.addAttribute("orders", orders);
        return "orders-list";
    }

    @GetMapping("/web/orders/picking-list/{id}")
    public String showPickingListPage(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id:" + id));
        
        List<OrderItem> sortedItems = order.getOrderItems().stream()
                .sorted(Comparator.comparing(oi -> oi.getItem().getLocation()))
                .collect(Collectors.toList());

        model.addAttribute("order", order);
        model.addAttribute("sortedOrderItems", sortedItems);
        return "picking-list";
    }

    // --- Reports & Exports ---
    @GetMapping("/web/items/export")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void exportItemsCsv(HttpServletResponse response) throws Exception {
        String fileName = "inventory-report.csv";
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        reportsService.exportItemsToCsv(response.getWriter());
    }

    // THIS IS THE NEW METHOD, NOW PLACED CORRECTLY
    @GetMapping("/web/reports/low-stock")
    public String showLowStockReportPage(Model model) {
        List<LowStockReportDto> lowStockItems = reportsService.getLowStockItemsReport();
        model.addAttribute("items", lowStockItems);
        return "low-stock-list";
    }
    // Add these two new methods to the WebController.java file

@PostMapping("/web/orders/pick/{id}")
public String pickOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        orderService.updateOrderStatus(id, OrderStatus.PICKED);
        redirectAttributes.addFlashAttribute("success_message", "Order #" + id + " marked as PICKED.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error_message", "Error: " + e.getMessage());
    }
    // Redirect back to the same picking list page to see the updated status
    return "redirect:/web/orders/picking-list/" + id;
}

@PostMapping("/web/orders/ship/{id}")
public String shipOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        orderService.updateOrderStatus(id, OrderStatus.SHIPPED);
        redirectAttributes.addFlashAttribute("success_message", "Order #" + id + " marked as SHIPPED.");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error_message", "Error: " + e.getMessage());
    }
    // Redirect back to the same picking list page
    return "redirect:/web/orders/picking-list/" + id;
}
}