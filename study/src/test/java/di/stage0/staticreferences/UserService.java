package di.stage0.staticreferences;

import di.User;

class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public static User join(User user) {
        UserDao.insert(user);
        return UserDao.findById(user.getId());
    }
}
