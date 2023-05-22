package be.xplore.pricescraper.search;

import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

/**
 * Analyzer for indexed item name.
 */
public class ItemNameAnalyzer implements LuceneAnalysisConfigurer {
  @Override
  public void configure(LuceneAnalysisConfigurationContext context) {
    context.analyzer("itemName").custom()
        .tokenizer("whitespace")
        .tokenFilter("lowercase")
        .tokenFilter("edgeNGram").param("maxGramSize", "15").param("minGramSize", "1");

    context.analyzer("itemNameQuery").custom()
        .tokenizer("whitespace")
        .tokenFilter("lowercase")
        .tokenFilter("snowballPorter").param("language", "Dutch");
  }
}
