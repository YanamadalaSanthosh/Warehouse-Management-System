package com.wms.repository;
import com.wms.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
    List<InventoryLog> findByItemIdOrderByTimestampDesc(Long itemId);
}
