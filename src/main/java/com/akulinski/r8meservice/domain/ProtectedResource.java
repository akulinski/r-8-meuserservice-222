package com.akulinski.r8meservice.domain;

/**
 * Resource that can be modified only by owner Or amdin
 * Is used by {@link com.akulinski.r8meservice.aop.security.SecurityAspect}
 * Classes that implement this interface have to return id of owner (profile not user)
 * {@link UserProfile}
 */
public interface ProtectedResource {
    long getOwner();
}
