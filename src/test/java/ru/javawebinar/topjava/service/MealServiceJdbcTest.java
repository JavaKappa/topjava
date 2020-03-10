package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;


@ActiveProfiles(value = {Profiles.HSQL_DB, Profiles.JDBC}, inheritProfiles = false)
public class MealServiceJdbcTest extends MealServiceTest {

}
