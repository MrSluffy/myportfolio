package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHistoryRepository extends JpaRepository<History, Integer> {

    @Query("select c from History c " +
            "where c.flashCard.id = :#{#flashcard} ")
    List<History> findByFlashCard(@Param("flashcard") Integer flashcard);
}
