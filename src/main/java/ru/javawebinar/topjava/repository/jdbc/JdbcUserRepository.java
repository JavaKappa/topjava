package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private static ResultSetExtractor<List<User>> extractor = rs -> {
        Map<Integer, User> map = new TreeMap<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String email = rs.getString("email");
            String password = rs.getString("password");
            Date registered = rs.getTimestamp("registered");
            boolean enabled = rs.getBoolean("enabled");
            int caloriesPerDay = rs.getInt("calories_per_day");

            String role = rs.getString("role");

            map.computeIfAbsent(id, integer -> {
                User user = new User();
                user.setId(id);
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);
                user.setRegistered(registered);
                user.setEnabled(enabled);
                user.setCaloriesPerDay(caloriesPerDay);
                user.setRoles(new TreeSet<>());
                return user;
            });

            if (role != null) {
                Set<Role> roles = map.get(id).getRoles();
                roles.add(Role.valueOf(role));
            }
        }
        return new ArrayList<>(map.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public User save(@NotNull User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            int id = newKey.intValue();
            user.setId(id);
            insertRoles(id, user.getRoles());
        }
//        else if (namedParameterJdbcTemplate.update(
//                "UPDATE users SET name=:name, email=:email, password=:password, " +
//                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
//            return null;
//        }
        else {
            jdbcTemplate.update("UPDATE users SET name=?, email=?, password=?, registered=?, enabled=?, calories_per_day=?  WHERE id=?",
                    user.getName(), user.getEmail(), user.getPassword(), user.getRegistered(), user.isEnabled(), user.getCaloriesPerDay(), user.getId());
            jdbcTemplate.update("DELETE FROM user_roles where user_id = ?", user.getId());
            insertRoles(user.getId(), user.getRoles());
        }
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(@Positive @NotNull int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(@Positive @NotNull int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", extractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(@Email String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", extractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id", extractor);
        if (users != null) {
            users.sort(Comparator.comparing(User::getName).thenComparing(User::getEmail));
        }
        return users;
    }

    private void insertRoles(@Positive @NotNull int userId, @NotNull @NotEmpty Set<Role> roles) {
        roles.forEach(r -> jdbcTemplate.update("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", userId, r.name()));
    }
}
