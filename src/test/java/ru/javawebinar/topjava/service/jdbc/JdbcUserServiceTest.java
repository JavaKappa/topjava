package ru.javawebinar.topjava.service.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserServiceTest.class);

//    @Override
//    public void createWithException() throws Exception {
//        log.debug("log is not supported");
//    }
}