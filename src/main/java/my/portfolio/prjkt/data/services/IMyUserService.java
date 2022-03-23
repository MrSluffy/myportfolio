package my.portfolio.prjkt.data.services;

import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.exceptions.AuthException;

import java.util.Optional;

public interface IMyUserService {
    Optional<MyUser> getUserName(String username);

    void register(String username, String password1);

    void guest();

    void authenticate(String username, String password) throws AuthException;

    MyUser getByUserName(String username);

}
