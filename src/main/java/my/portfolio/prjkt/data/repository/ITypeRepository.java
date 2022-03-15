package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.TypePrjkt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeRepository extends JpaRepository<TypePrjkt, Integer> {
}
