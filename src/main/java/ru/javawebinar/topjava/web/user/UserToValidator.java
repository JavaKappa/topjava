package ru.javawebinar.topjava.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

@Component
public class UserToValidator implements Validator {

    private UserService service;
    @Autowired
    public UserToValidator(UserService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserTo.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        checkEmail(o, errors);
    }

    private void checkEmail(Object o, Errors errors) {
        UserTo user = (UserTo) o;
        User userFromDB = null;
        try {
            userFromDB = service.get(SecurityUtil.authUserId());
        } catch (Exception ignored) {

        }

        if (userFromDB == null) {
            User userFromDbWithSameEmail = null;
            try {
                userFromDbWithSameEmail = service.getByEmail(user.getEmail());
            } catch (Exception ignored) {

            }
            if (userFromDbWithSameEmail != null) {
                errors.rejectValue("email", "user.emailInUse");
            }
        }else {
            try {
                if (!user.getEmail().equalsIgnoreCase(userFromDB.getEmail())) {
                    errors.rejectValue("email", "user.emailInUse");
                }
            } catch (Exception ignored) {

            }
        }
    }
}
