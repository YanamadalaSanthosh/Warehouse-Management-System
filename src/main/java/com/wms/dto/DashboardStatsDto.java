package com.wms.dto;

import com.wms.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardStatsDto {

    // For the Inventory Summary widget
    private long totalDistinctItems;
    private long totalItemQuantity;

    // For the Low Stock Alert widget
    private long lowStockItemCount;

    // For the Recent Orders widget
    private List<Order> recentOrders;

}