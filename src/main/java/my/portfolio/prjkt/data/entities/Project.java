package my.portfolio.prjkt.data.entities;

import my.portfolio.prjkt.data.entities.abstracts.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Project extends AbstractEntity implements Serializable {

    @Basic(fetch = FetchType.LAZY)
    private byte[] profilePicture;

    private String titlePrjkt;
    private String descriptionPrjkt;

    @Column(length = 5200)
    private String urlPrjkt;

    @ManyToOne
    private TypePrjkt typePrjkt;

    private String date;

    public Project(){

    }

    public String getTitlePrjkt() {
        return titlePrjkt;
    }

    public void setTitlePrjkt(String titlePrjkt) {
        this.titlePrjkt = titlePrjkt;
    }

    public String getDescriptionPrjkt() {
        return descriptionPrjkt;
    }

    public void setDescriptionPrjkt(String descriptionPrjkt) {
        this.descriptionPrjkt = descriptionPrjkt;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public String getUrlPrjkt() {
        return urlPrjkt;
    }

    public void setUrlPrjkt(String urlPrjkt) {
        this.urlPrjkt = urlPrjkt;
    }

    public TypePrjkt getTypePrjkt() {
        return typePrjkt;
    }

    public void setTypePrjkt(TypePrjkt typePrjkt) {
        this.typePrjkt = typePrjkt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
