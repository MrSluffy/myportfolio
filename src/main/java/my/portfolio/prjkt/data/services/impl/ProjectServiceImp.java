package my.portfolio.prjkt.data.services.impl;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.entities.TypePrjkt;
import my.portfolio.prjkt.data.repository.IProjectRepository;
import my.portfolio.prjkt.data.repository.ITypeRepository;
import my.portfolio.prjkt.data.services.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImp implements IProjectService {

    private final IProjectRepository projectRepository;
    private final ITypeRepository typeRepository;

    @Autowired
    public ProjectServiceImp(IProjectRepository projectRepository,
                             ITypeRepository typeRepository) {
        this.projectRepository = projectRepository;
        this.typeRepository = typeRepository;
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
    public List<TypePrjkt> getAllProjectType() {
        return typeRepository.findAll();
    }

    @Override
    public long getAllProjectCount(){
        return projectRepository.count();
    }

    @Override
    public void saveNewProject(byte[] imageBytes,
                               String title,
                               TypePrjkt typePrjkt,
                               LocalDate date,
                               String description,
                               String url) {
        Project project = new Project();
        project.setTitlePrjkt(title);
        project.setProfilePicture(imageBytes);
        project.setTypePrjkt(typePrjkt);
        project.setDate(date.toString());
        project.setDescriptionPrjkt(description);
        project.setUrlPrjkt(url);
        if(projectRepository.findProjectByTitlePrjkt(title).isPresent()){
            throw new IllegalStateException(title +" is already exist");
        } else {
            projectRepository.save(project);
        }

    }

    @Override
    public Optional<Project> findProjectBy(String title) {
        return projectRepository.findProjectByTitlePrjkt(title);
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
