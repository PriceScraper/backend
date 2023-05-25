package be.xplore.pricescraper.services;

import org.springframework.stereotype.Service;

/**
 * Service to get info of barcode.
 */
@Service
public interface BarcodeItemService {
  /**
   * Get product name from barcode.
   */
  String getProductName(String barcode);
}
