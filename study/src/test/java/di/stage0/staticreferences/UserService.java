package di.stage0.staticreferences;

import di.User;

class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User join(User user) {
        userDao.insert(user);
        return user;
    }
}
