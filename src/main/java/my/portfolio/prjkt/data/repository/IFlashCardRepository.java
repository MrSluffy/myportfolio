package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IFlashCardRepository extends JpaRepository<FlashCard, Integer> {

    Optional<FlashCard> findByCardTitle(String name);

    @Query("select c from FlashCard c " +
            "where lower(c.myUserInFlashCard.userName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.cardTitle) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.cardNumber) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.cardDetail) like lower(concat('%', :searchTerm, '%')) ")
    List<FlashCard> search(@Param("searchTerm") String searchTerm);
}
