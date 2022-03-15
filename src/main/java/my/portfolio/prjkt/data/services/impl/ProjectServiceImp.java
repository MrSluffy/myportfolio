package my.portfolio.prjkt.data.services.impl;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.repository.IProjectRepository;
import my.portfolio.prjkt.data.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class ProjectServiceImp implements IProjectService {

    private final IProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImp(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Image generateImage(Project project) {
        Integer id = project.getId();
        StreamResource sr = new StreamResource("user", () ->  {
            Project attached = projectRepository.findWithPropertyPictureAttachedById(id);
            return new ByteArrayInputStream(attached.getProfilePicture());
        });
        sr.setContentType("image/png");
        return new Image(sr, "profile-picture");

    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    @Override
    public long getAllProjectCount(){
        return projectRepository.count();
    }

    /**
     * Read file into byte array
     *
     * @param imagePath
     *     path to a file
     * @return byte array out of file
     * @throws IOException
     *     File not found or could not be read
     */
    public static byte[] getBytesFromFile(String imagePath) throws IOException {
        File file = new File(imagePath);
        return Files.readAllBytes(file.toPath());
    }

}
