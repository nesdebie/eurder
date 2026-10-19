package org.example.eurder.repository;

import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.Role;
import org.example.eurder.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends GenericRepository<User> {

    public UserRepository() {
        super(User.class);
        User admin = new User(
                "admin",
                "admin",
                "admin@admin.com",
                "420",
                new Address(
                        "4DM",
                        "1N",
                        "ADMINCITY",
                        "ADMINLAND"
                )
        );
        admin.setRole(Role.ADMIN);
        this.save(admin);
    }

    public User save(User user) { return saveWithId(user.getId(), user); }

    public boolean isEmailAvailable(String email) {
        return getOptional(user -> user.getEmail().equals(email)).isEmpty();
    }

    public Optional<User> getUserWithEmail(String email) {
        return getOptional(user -> user.getEmail().equals(email));
    }
}
