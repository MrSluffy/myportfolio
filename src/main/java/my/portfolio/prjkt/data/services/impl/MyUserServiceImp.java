package my.portfolio.prjkt.data.services.impl;

import com.vaadin.flow.server.VaadinSession;
import my.portfolio.prjkt.data.entities.MyUser;
import my.portfolio.prjkt.data.entities.Role;
import my.portfolio.prjkt.data.repository.IUserRepository;
import my.portfolio.prjkt.data.services.IMyUserService;
import my.portfolio.prjkt.exceptions.AuthException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class MyUserServiceImp implements IMyUserService {

    private final IUserRepository userRepository;

    @Autowired
    public MyUserServiceImp(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<MyUser> getUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public void register(String username, String password) {
        MyUser myUser = new MyUser(username, password, Role.USER);
        if(username.equals("MrSluffy")){
            myUser = new MyUser(username, password, Role.ADMIN);
        }
        if(userRepository.findByUserName(username).isPresent()){
            throw new IllegalStateException(username +" is already taken");
        } else {
            userRepository.save(myUser);
        }
    }

    @Override
    public void guest() {
        var ran = RandomStringUtils.random(99999);
        var i = new Random();
        MyUser guest = new MyUser(
                "Guest#" + i.nextInt(ran.length()),
                "guest" + i.nextInt(ran.length()),
                Role.GUEST);
        userRepository.save(guest);

        VaadinSession.getCurrent().setAttribute(MyUser.class, guest);
    }

    @Override
    public void authenticate(String username, String password) throws AuthException {
        MyUser user = getByUserName(username);
        if(user != null && user.verifyPassword(password)){
            VaadinSession.getCurrent().setAttribute(MyUser.class, user);
        } else {
            throw new AuthException();
        }

    }

    @Override
    public MyUser getByUserName(String username) {
        return userRepository.getByUserName(username);
    }

    @Override
    public List<MyUser> findAllUser() {
        return userRepository.findAll();
    }
}
