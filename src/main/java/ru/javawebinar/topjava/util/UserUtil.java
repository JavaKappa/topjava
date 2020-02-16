package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UserUtil {
    public static List<User> USERS = Arrays.asList(
            new User(1, "TOM", "gmail.com", "8888", 2000, true, Collections.singleton(Role.ROLE_USER)),
            new User(2, "Jerry", "gmail.com", "8888", 2000, true, Collections.singleton(Role.ROLE_ADMIN))
            );
}
