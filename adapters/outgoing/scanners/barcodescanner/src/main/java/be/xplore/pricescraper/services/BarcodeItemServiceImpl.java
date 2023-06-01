package be.xplore.pricescraper.services;

import be.xplore.pricescraper.exceptions.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.coderion.service.OpenFoodFactsWrapper;
import pl.coderion.service.impl.OpenFoodFactsWrapperImpl;

/**
 * Implementation of service.
 */
@Service
@Slf4j
public class BarcodeItemServiceImpl implements BarcodeItemService {
  private final OpenFoodFactsWrapper wrapper;

  public BarcodeItemServiceImpl() {
    this.wrapper = new OpenFoodFactsWrapperImpl();
  }

  @Override
  public String getProductName(String barcode) {
    var res = wrapper.fetchProductByCode(barcode);
    if (!res.isStatus()) {
      log.debug("Bad status for barcode: " + barcode);
      throw new ItemNotFoundException();
    }
    if (res.getProduct() == null) {
      log.debug("Product was null for barcode: " + barcode);
      throw new ItemNotFoundException();
    }
    if (res.getProduct().getGenericName() == null) {
      log.debug("Generic name was null for barcode: " + barcode);
      if (res.getProduct().getProductName() == null) {
        log.debug("Product name was null for barcode: " + barcode);
        throw new ItemNotFoundException();
      }
    }
    return cleanProductName(res.getProduct().getGenericName() != null
        ? res.getProduct().getGenericName()
        : res.getProduct().getProductName());
  }

  public String cleanProductName(String name) {
    return name.replaceAll("([0-9,.]{1,8})(cl|ml|l)", "").strip();
  }
}
