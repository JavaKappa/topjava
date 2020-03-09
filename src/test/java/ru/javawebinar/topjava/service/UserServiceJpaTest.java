package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Капу пк
 * 09.03.2020
 */
@ActiveProfiles(value = {Profiles.POSTGRES_DB, Profiles.JPA}, inheritProfiles = false)
public class UserServiceJpaTest extends UserServiceTest {
}
