package my.portfolio.prjkt.data.services;

import com.vaadin.flow.component.html.Image;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.entities.TypePrjkt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IProjectService {

    Image generateImage(Project project);

    List<Project> getAllProject();

    List<TypePrjkt> getAllProjectType();


    long getAllProjectCount();


    void saveNewProject(byte[] imageBytes, String title, TypePrjkt typePrjkt, LocalDate date, String description, String url) throws IOException;

    Optional<Project> findProjectBy(String title);
}
