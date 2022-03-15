package my.portfolio.prjkt.data.entities;

import my.portfolio.prjkt.data.entities.abstracts.AbstractEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class User extends AbstractEntity implements Serializable {

    private String userName;
    private String passwordSalt;
    private String passwordHash;

    private Role role;

    public User(String userName, String password, Role role){
        this.userName = userName;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);

        this.role = role;
    }

    public User() {
    }

    public boolean verifyPassword(String password){
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

