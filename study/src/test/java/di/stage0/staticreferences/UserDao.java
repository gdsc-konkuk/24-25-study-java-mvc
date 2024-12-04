package di.stage0.staticreferences;

import di.User;

interface UserDao {
    void insert(User user);
    User findById(Long id);
}
