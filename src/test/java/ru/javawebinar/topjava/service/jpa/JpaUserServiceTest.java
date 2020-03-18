package ru.javawebinar.topjava.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaUserServiceTest extends AbstractUserServiceTest {
    @Autowired
    protected JpaUtil jpaUtil;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        jpaUtil.clear2ndLevelHibernateCache();
    }
}