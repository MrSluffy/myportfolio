package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IFlashCardRepository extends JpaRepository<FlashCard, Integer> {

    Optional<FlashCard> findByCardTitle(String name);
}
