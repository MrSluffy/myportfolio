package my.portfolio.prjkt.data.services;

import my.portfolio.prjkt.data.entities.FlashCard;

import java.util.List;

public interface IFlashCardService {

    void save(String title, String detail, String reference, String answer, String question);

    void update(Integer id, String title, String detail, String reference);

    void delete(Integer id);

    FlashCard getById(Integer id);

    List<FlashCard> findAllCards();
}
