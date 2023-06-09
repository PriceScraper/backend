package be.xplore.pricescraper.exceptions;

import be.xplore.pricescraper.matchers.Matcher;

/**
 * Exception thrown when a {@link Matcher} is used before it has been initialized.
 */
public class MatcherNotInitializedException extends RuntimeException {
}
