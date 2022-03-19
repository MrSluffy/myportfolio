package my.portfolio.prjkt.data.services.impl;

import my.portfolio.prjkt.data.entities.FlashCard;
import my.portfolio.prjkt.data.repository.IFlashCardRepository;
import my.portfolio.prjkt.data.services.IFlashCardService;
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
    public void save(String title, String detail, String reference, String answer, String question) {
        FlashCard flashCard = new FlashCard();
        flashCard.setCardTitle(title);
        flashCard.setCardDetail(detail);
        flashCard.setCardReference(reference);
        flashCard.setCardDate(LocalDateTime.now().toLocalDate().toString());

        flashCard.setCardAnswer(answer);
        flashCard.setCarqQuestion(question);

        flashCardRepository.save(flashCard);

    }

    @Transactional
    @Override
    public void update(Integer id, String title, String detail, String reference) {
        FlashCard flashCard = getById(id);
        flashCard.setCardTitle(title);
        flashCard.setCardDetail(detail);
        flashCard.setCardReference(reference);
        flashCardRepository.save(flashCard);
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
}
