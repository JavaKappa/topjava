package ru.javawebinar.topjava.service.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
    private static final Logger log = LoggerFactory.getLogger(JdbcMealServiceTest.class);

    @Override
    public void createWithException() throws Exception {
        log.debug("Method is not supported");
    }
}