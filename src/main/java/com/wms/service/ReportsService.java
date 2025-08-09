package com.wms.service;
import com.wms.dto.*;
import com.wms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
// Add these new imports at the top of ReportsService.java
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.wms.entity.Item;
import java.io.Writer;
import java.io.IOException;
@Service
@Transactional(readOnly = true)
public class ReportsService {
    @Autowired private ItemRepository itemRepository;
    @Autowired private InventoryLogRepository inventoryLogRepository;
    
    public List<InventoryMovementReportDto> getInventoryMovementReport(Long itemId) {
        return inventoryLogRepository.findByItemIdOrderByTimestampDesc(itemId).stream()
                .map(InventoryMovementReportDto::fromEntity)
                .collect(Collectors.toList());
    }
    // Add this method to ReportsService.java

    public long getLowStockItemCount() {
        // This reuses the existing low stock logic but efficiently counts the results.
        return itemRepository.findAll().stream()
                .filter(item -> item.getQuantity() <= item.getThreshold())
                .count();
    }
    public void exportItemsToCsv(Writer writer) throws CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, IOException {
    // 1. Fetch all items from the database
    List<Item> items = itemRepository.findAll();

    // 2. Use OpenCSV's StatefulBeanToCsv to write the list of items to the provided writer
    StatefulBeanToCsv<Item> beanToCsv = new StatefulBeanToCsvBuilder<Item>(writer)
            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
            .build();

    // 3. Write all the beans (items) to the CSV
    beanToCsv.write(items);
}
/**
 * Generates a report of all items where the current quantity
 * is less than or equal to the item's specified threshold.
 *
 * @return A list of DTOs representing low-stock items.
 */

// In ReportsService.java
public List<LowStockReportDto> getLowStockItemsReport() {
    // This now uses the correct, existing query method name
    List<Item> lowStockItems = itemRepository.findLowStockItems();
    return lowStockItems.stream()
            .map(LowStockReportDto::fromEntity)
            .collect(Collectors.toList());
}
}
