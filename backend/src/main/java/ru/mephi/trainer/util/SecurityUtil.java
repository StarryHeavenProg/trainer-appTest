package ru.mephi.trainer.util;

import ru.mephi.trainer.entity.enums.UserRole;

public class SecurityUtil {

    private SecurityUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String ADMIN = "admin";
    public static final String EXPERT = "expert";
    public static final String STUDENT = "student";

    public static String toSecurityRole(UserRole userRole) {
        return switch (userRole) {
            case APP_USER -> STUDENT;
            case APP_EXPERT -> EXPERT;
            case APP_ADMIN -> ADMIN;
            case null -> throw new IllegalArgumentException("userRole can not be null");
        };
    }
}
