package my.portfolio.prjkt.data.entities;

import my.portfolio.prjkt.data.entities.abstracts.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class TypePrjkt extends AbstractEntity {

    private String name;

    public TypePrjkt(){
    }

    public TypePrjkt(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
