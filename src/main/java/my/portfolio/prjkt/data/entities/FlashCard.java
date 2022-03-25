package my.portfolio.prjkt.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import my.portfolio.prjkt.data.entities.abstracts.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class FlashCard extends AbstractEntity {


    private String cardTitle;

    private String cardDetail;

    private String cardReference;

    private String cardDate;

    private String cardAnswer;

    private String carqQuestion;

    private boolean isCorrect;

    private int cardNumber;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", referencedColumnName = "id")
    private History history;

    @OneToMany(mappedBy = "flashcard", orphanRemoval = true)
    private Set<MyUser> addedByMyUser = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_id", referencedColumnName = "id")
    private MyUser myUserInFlashCard;

    @ManyToMany(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private List<MyUser> userListCorrectAnswer = new ArrayList<>();

    public FlashCard(){
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardDetail() {
        return cardDetail;
    }

    public void setCardDetail(String cardDetail) {
        this.cardDetail = cardDetail;
    }

    public String getCardReference() {
        return cardReference;
    }

    public void setCardReference(String cardReference) {
        this.cardReference = cardReference;
    }

    public String getCardDate() {
        return cardDate;
    }

    public void setCardDate(String cardDate) {
        this.cardDate = cardDate;
    }

    public String getCardAnswer() {
        return cardAnswer;
    }

    public void setCardAnswer(String cardAnswer) {
        this.cardAnswer = cardAnswer;
    }

    public String getCarqQuestion() {
        return carqQuestion;
    }

    public void setCarqQuestion(String carqQuestion) {
        this.carqQuestion = carqQuestion;
    }

    public Set<MyUser> getAddedByMyUser() {
        return addedByMyUser;
    }

    public void setAddedByMyUser(Set<MyUser> addedByMyUser) {
        this.addedByMyUser = addedByMyUser;
    }

    public MyUser getMyUserInFlashCard() {
        return myUserInFlashCard;
    }

    public void setMyUserInFlashCard(MyUser myUserInFlashCard) {
        this.myUserInFlashCard = myUserInFlashCard;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public List<MyUser> getUserListCorrectAnswer() {
        return userListCorrectAnswer;
    }

    public void setUserListCorrectAnswer(List<MyUser> userListCorrectAnswer) {
        this.userListCorrectAnswer = userListCorrectAnswer;
    }

    @Override
    public String toString() {
        return "FlashCard{" +
                "cardTitle='" + cardTitle + '\'' +
                ", cardDetail='" + cardDetail + '\'' +
                ", cardReference='" + cardReference + '\'' +
                ", cardDate='" + cardDate + '\'' +
                ", cardAnswer='" + cardAnswer + '\'' +
                ", carqQuestion='" + carqQuestion + '\'' +
                ", isCorrect=" + isCorrect +
                ", cardNumber=" + cardNumber +
                ", history=" + history +
                ", addedByMyUser=" + addedByMyUser +
                ", myUserInFlashCard=" + myUserInFlashCard +
                ", userListCorrectAnswer=" + userListCorrectAnswer +
                '}';
    }
}
