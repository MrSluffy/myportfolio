package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistoryRepository extends JpaRepository<History, Integer> {
}