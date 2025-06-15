package com.alextim.intershop.utils;


import com.alextim.intershop.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static Optional<Long> extractUserIdToOptional(UserDetails user) {
        return Optional.ofNullable(user == null ? null : ((User) user).getId());
    }

    public static long extractUserId(UserDetails user) {
        return ((User) user).getId();
    }
}
