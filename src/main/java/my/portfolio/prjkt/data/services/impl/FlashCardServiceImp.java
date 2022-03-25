package my.portfolio.prjkt.data.services.impl;

import com.vaadin.flow.server.VaadinSession;
import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.data.repository.IFlashCardRepository;
import my.portfolio.prjkt.data.services.IFlashCardService;
import my.portfolio.prjkt.exceptions.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlashCardServiceImp implements IFlashCardService {


    private final IFlashCardRepository flashCardRepository;

    @Autowired
    public FlashCardServiceImp(IFlashCardRepository flashCardRepository) {
        this.flashCardRepository = flashCardRepository;
    }


    @Override
    public void save(String title, String detail, String reference, String answer, String question) throws AuthException {
        MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);
        FlashCard flashCard = new FlashCard();
        int flashCardNumber = 0;

        if (user != null) {
            for(int i = 0; i <= findAllCards().size(); i++){
                flashCardNumber = i + 1;
            }
            flashCard.setCardTitle(title);
            flashCard.setCardDetail(detail);
            flashCard.setCardReference(reference);
            flashCard.setCardDate(LocalDateTime.now().toLocalDate().toString());
            flashCard.getAddedByMyUser().add(user);
            flashCard.setMyUserInFlashCard(user);
            flashCard.setCardNumber(flashCardNumber);
            flashCard.setCardAnswer(answer);
            flashCard.setCorrect(false);
            flashCard.setCarqQuestion(question);
            flashCardRepository.save(flashCard);
        } else {
            throw new AuthException();
        }
    }

    @Transactional
    @Override
    public void update(Integer id, String title, String detail, String reference, String question, String answer, boolean isCorrect) throws AuthException {
        MyUser user = VaadinSession.getCurrent().getAttribute(MyUser.class);
        if(user != null){
            FlashCard flashCard = getById(id);
            flashCard.setCardTitle(title);
            flashCard.setCardDetail(detail);
            flashCard.setCardReference(reference);
            flashCard.setCardAnswer(answer);
            flashCard.setCorrect(isCorrect);
            flashCard.setCarqQuestion(question);
            flashCard.setMyUserInFlashCard(user);
            flashCardRepository.save(flashCard);
        }

    }

    @Override
    public void delete(Integer id) {
        FlashCard flashCard = getById(id);
        flashCardRepository.delete(flashCard);
    }

    @Override
    public FlashCard getById(Integer id) {
        return flashCardRepository.getById(id);
    }

    @Override
    public List<FlashCard> findAllCards() {
        return flashCardRepository.findAll();
    }

    @Override
    public void submitAnswer(Integer id, boolean isCorrect) {
        FlashCard flashCard = getById(id);
        flashCard.setCorrect(isCorrect);
        flashCardRepository.saveAndFlush(flashCard);
    }
}
