package my.portfolio.prjkt.data.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.entities.TypePrjkt;
import my.portfolio.prjkt.data.repository.IProjectRepository;
import my.portfolio.prjkt.data.repository.ITypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {

    private List<Project> projectList;

    private ExampleDataGenerator<Project> projectExampleDataGenerator;

    @Bean
    CommandLineRunner loadData(IProjectRepository projectRepository,
                               ITypeRepository prjktRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (projectRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            List<TypePrjkt> prjkts = prjktRepository.saveAll(Stream.of("Android Apps", "WebApp", "Spring")
                            .map(TypePrjkt::new).collect(Collectors.toList()));

            projectExampleDataGenerator = new ExampleDataGenerator<>(Project.class, LocalDateTime.now());
            projectExampleDataGenerator.setData(Project::setTitlePrjkt, DataType.COMPANY_NAME);
            projectExampleDataGenerator.setData(Project::setUrlPrjkt, DataType.PROFILE_PICTURE_URL);
            projectExampleDataGenerator.setData(Project::setDescriptionPrjkt, DataType.SENTENCE);

            projectList = projectExampleDataGenerator.create(7, seed);


            Random r = new Random(seed);

            projectList.stream().map(project ->{
                project.setTitlePrjkt(project.getTitlePrjkt());
                project.setTypePrjkt(prjkts.get(r.nextInt(prjkts.size())));
                project.setUrlPrjkt(project.getUrlPrjkt());
                project.setDate(LocalDateTime.now().toLocalDate().toString());
                return project;
            }).collect(Collectors.toList());

            prjktRepository.saveAll(prjkts);

//            projectRepository.saveAll(projectList);



            logger.info("Generated demo data");
        };
    }


}
