package com.wms.repository;
import com.wms.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
// ADD THIS CORRECT VERSION
import org.springframework.data.jpa.repository.Query; 
// Add this method to ItemRepository.java
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findBySku(String sku);
    boolean existsBySku(String sku);

    @Query("SELECT SUM(i.quantity) FROM Item i")
    Optional<Long> getTotalItemQuantity();

    @Query("SELECT i FROM Item i WHERE i.quantity <= i.threshold")
List<Item> findLowStockItems();
}
