package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectRepository extends JpaRepository<Project, Integer> {
    /*
     * Loads an entity with lazy property loaded from a database
     */
    @EntityGraph(attributePaths={"profilePicture"})
    Project findWithPropertyPictureAttachedById(Integer id);
}
