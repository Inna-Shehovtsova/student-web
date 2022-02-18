package ru.student.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import ru.student.entity.Post;
import ru.student.entity.Roles;
import ru.student.entity.User;


import java.util.stream.Collectors;

public class SecurityUtils {
    public static final String ACCESS_DENIED = "Access Denied";

    public static UserDetails getCurrentUserDetails(){
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
        return (UserDetails)principal;
    }

    public static void checkAuthorityOnPost(Post post) {
        if (!hasAuthorityOnPost(post)){
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }

    public static void checkAuthorityOnPostOrUserIsAdmin(Post post){
        if (!hasAuthorityOnPost(post) && !hasRole(Roles.ADMIN)){
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }

    public static boolean hasAuthorityOnPost(Post post){
        String username = SecurityUtils.getCurrentUserDetails().getUsername();
        return post.getUser().getUsername().equals(username);
    }

    public static boolean hasRole(String role) {
        return getCurrentUserDetails().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet())
                .contains(User.ROLE + role);
    }
}

