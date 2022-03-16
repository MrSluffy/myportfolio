package my.portfolio.prjkt.data.entities;

import my.portfolio.prjkt.data.entities.abstracts.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Project extends AbstractEntity {

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] profilePicture;

    private String titlePrjkt;
    private String descriptionPrjkt;

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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
}
