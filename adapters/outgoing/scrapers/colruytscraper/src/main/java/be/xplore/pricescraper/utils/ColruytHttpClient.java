package be.xplore.pricescraper.utils;

import be.xplore.pricescraper.model.ColruytResponse;
import be.xplore.pricescraper.model.Product;
import java.net.URI;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "colruytClient", url = "https://apip.colruyt.be")
public interface ColruytHttpClient {
  @RequestMapping(method = RequestMethod.GET, value = "/gateway/ictmgmt.emarkecom.cgproductsearchsvc.v2/v1/nl/products?clientCode=CLP&isAvailable=true&page={page}&searchTerm={searchTerm}&size={size}")
  ColruytResponse searchItems(@RequestHeader Map<String, String> headerMap, @PathVariable int page, @PathVariable String searchTerm, @PathVariable int size);
  @RequestMapping(method = RequestMethod.GET)
  Product findItem(@RequestHeader Map<String, String> headerMap, URI baseUri);
}
