package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(value = {Profiles.POSTGRES_DB, Profiles.JPA}, inheritProfiles = false)

 public class MealServiceJpaTest extends MealServiceTest {
}
