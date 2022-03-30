package my.portfolio.prjkt.data.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.entities.Project;
import my.portfolio.prjkt.data.entities.TypePrjkt;
import my.portfolio.prjkt.data.repository.IFlashCardRepository;
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

@SpringComponent
public class DataGenerator {

    private List<Project> projectList;

    private List<FlashCard> flashCardList;

    private ExampleDataGenerator<Project> projectExampleDataGenerator;

    private ExampleDataGenerator<FlashCard> flashCardExampleDataGenerator;

    @Bean
    CommandLineRunner loadData(IProjectRepository projectRepository,
                               ITypeRepository prjktRepository,
                               IFlashCardRepository flashCardRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (projectRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            projectExampleDataGenerator = new ExampleDataGenerator<>(Project.class, LocalDateTime.now());
            projectExampleDataGenerator.setData(Project::setTitlePrjkt, DataType.COMPANY_NAME);
            projectExampleDataGenerator.setData(Project::setUrlPrjkt, DataType.PROFILE_PICTURE_URL);
            projectExampleDataGenerator.setData(Project::setDescriptionPrjkt, DataType.SENTENCE);

            flashCardExampleDataGenerator = new ExampleDataGenerator<>(FlashCard.class, LocalDateTime.now());
            flashCardExampleDataGenerator.setData(FlashCard::setCardTitle, DataType.WORD);
            flashCardExampleDataGenerator.setData(FlashCard::setCardDetail, DataType.SENTENCE);
            flashCardExampleDataGenerator.setData(FlashCard::setCardReference, DataType.PROFILE_PICTURE_URL);
            flashCardExampleDataGenerator.setData(FlashCard::setCardDate, DataType.BOOK_GENRE);

            projectList = projectExampleDataGenerator.create(7, seed);


            flashCardList = flashCardExampleDataGenerator.create(7, seed);


            Random r = new Random(seed);

//            projectList.stream().map(project ->{
//                project.setTitlePrjkt(project.getTitlePrjkt());
//                project.setTypePrjkt(prjkts.get(r.nextInt(prjkts.size())));
//                project.setUrlPrjkt(project.getUrlPrjkt());
//                project.setDate(LocalDateTime.now().toLocalDate().toString());
//                return project;
//            }).collect(Collectors.toList());
//
//            flashCardList.stream().map(flashCard -> {
//               flashCard.setCardTitle(flashCard.getCardTitle());
//               flashCard.setCardDetail(flashCard.getCardDetail());
//               flashCard.setCardReference(flashCard.getCardReference());
//               flashCard.setCardDate(flashCard.getCardDate());
//               return flashCard;
//            });

            TypePrjkt aApps = new TypePrjkt();
            aApps.setName("Android Application");
            if(prjktRepository.findByName("Android Application").isPresent()){
                System.out.println("Already Exist");
            } else {
                prjktRepository.save(aApps);
            }

            TypePrjkt wApps = new TypePrjkt();
            wApps.setName("Web Application");
            if(prjktRepository.findByName("Web Application").isPresent()){
                System.out.println("Already Exist");
            } else {
                prjktRepository.save(wApps);
            }

            TypePrjkt sApps = new TypePrjkt();
            sApps.setName("Spring Application");
            if(prjktRepository.findByName("Spring Application").isPresent()){
                System.out.println("Already Exist");
            } else {
                prjktRepository.save(sApps);
            }

            TypePrjkt mApps = new TypePrjkt();
            mApps.setName("Modified Android Apps");
            if(prjktRepository.findByName("Modified Android Apps").isPresent()){
                System.out.println("Already Exist");
            } else {
                prjktRepository.save(mApps);
            }


//            flashCardRepository.saveAll(flashCardList);

//            projectRepository.saveAll(projectList);



            logger.info("Generated demo data");
        };
    }


}
