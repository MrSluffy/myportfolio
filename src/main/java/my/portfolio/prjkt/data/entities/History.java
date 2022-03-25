package my.portfolio.prjkt.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import my.portfolio.prjkt.data.entities.abstracts.AbstractEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class History extends AbstractEntity {

    private String activityName;
    private String activityLastChangeDate;
    private String activityAuthor;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "flashcard_id", referencedColumnName = "id")
    private FlashCard flashCard;

    @OneToMany(mappedBy = "history", orphanRemoval = true)
    private Set<FlashCard> flashCardSet = new HashSet<>();

    public History() {
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityLastChangeDate() {
        return activityLastChangeDate;
    }

    public void setActivityLastChangeDate(String activityLastChangeDate) {
        this.activityLastChangeDate = activityLastChangeDate;
    }

    public String getActivityAuthor() {
        return activityAuthor;
    }

    public void setActivityAuthor(String activityAuthor) {
        this.activityAuthor = activityAuthor;
    }

    public FlashCard getFlashCard() {
        return flashCard;
    }

    public void setFlashCard(FlashCard flashCard) {
        this.flashCard = flashCard;
    }

    public Set<FlashCard> getFlashCardSet() {
        return flashCardSet;
    }

    public void setFlashCardSet(Set<FlashCard> flashCardSet) {
        this.flashCardSet = flashCardSet;
    }
}
