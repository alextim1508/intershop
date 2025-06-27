package com.alextim.intershop.utils;


import com.alextim.intershop.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    public static Optional<Long> extractUserId(User user) {
        return Optional.ofNullable(user == null ? null : user.getId());
    }
}
