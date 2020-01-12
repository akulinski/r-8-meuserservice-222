package com.akulinski.r8meservice.service;

import java.util.function.Supplier;

public class ExceptionUtils {

    public static Supplier<NoUserFoundException> getNoUserFoundExceptionSupplier(String login) {
        return () -> new NoUserFoundException(String.format("No User by login: %s", login), login);
    }

    public static Supplier<NoUserFoundException> getNoUserFoundExceptionSupplier() {
        return () -> new NoUserFoundException(String.format("No User found"), "");
    }
    public static Supplier<NoLoginInContextException> getNoLoginInContextExceptionSupplier() {
        return () -> new NoLoginInContextException("No login in security context");
    }

    public static Supplier<NoProfileConnectedException> getNoProfileConnectedExceptionSupplier(long id) {
        return () -> new NoProfileConnectedException(id);
    }


}
