package be.xplore.pricescraper.utils;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class serves a model mapper Bean.
 * The mapping of lazy loaded collections is skipped.
 */
@Component
public class ModelMapperUtil {
  /**
   * Creates ModelMapper.
   */
  @Bean
  @Scope("singleton")
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setPropertyCondition(
        context -> (!(context.getSource() instanceof PersistentCollection persistentCollection) ||
            ((PersistentCollection) context.getSource()).wasInitialized())
    );
    return mapper;
  }
}
