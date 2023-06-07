package be.xplore.pricescraper.seed;

import be.xplore.pricescraper.domain.users.User;
import be.xplore.pricescraper.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class UserSeed implements Seed {
  private final UserRepository userRepository;

  @Override
  public void execute() {
    if(userRepository.findByUsernameAndProvider("Bob", "Seed").isPresent()) {
      log.info("User repository has already been seeded.");
      return;
    }
    log.info("Seeding users.");
    var user = new User("Bob", "Seed", "https://i.imgur.com/WLM2Oui.png");
    userRepository.save(user);
  }

  @Override
  public int priority() {
    return -1;
  }
}
