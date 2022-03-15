package my.portfolio.prjkt.data.services;

import com.vaadin.flow.component.html.Image;
import my.portfolio.prjkt.data.entities.Project;

import java.util.List;

public interface IProjectService {

    Image generateImage(Project project);

    List<Project> getAllProject();

    long getAllProjectCount();


}
