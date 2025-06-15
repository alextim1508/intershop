package com.alextim.intershop.service;

import com.alextim.intershop.entity.User;
import com.alextim.intershop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> save(User user) {
        log.info("save user: {}", user);

        return userRepository.save(user)
                .doOnNext(savedUser -> log.info("saved user: {}", savedUser));
    }

    @Override
    public Mono<User> lock(long id) {
        log.info("block user with ID {}", id);

        return userRepository.findById(id)
                .map(user -> {
                    user.setAccountNonLocked(false);
                    return user;
                })
                .flatMap(userRepository::save)
                .doOnNext(savedUser -> log.info("saved user: {}", savedUser));
    }

    @Override
    public Mono<User> addRole(long id, String role) {
        log.info("add role {} to user with ID {}", role, id);

        return userRepository.findById(id)
                .map(user -> {
                    List<String> roles = user.getRoles();
                    if(!roles.contains(role))
                        roles.add(role);
                    return user;
                })
                .flatMap(userRepository::save)
                .doOnNext(savedUser -> log.info("saved user: {}", savedUser));
    }

    @Override
    public Mono<User> removeRole(long id, String role) {
        log.info("add role {} to user with ID {}", role, id);

        return userRepository.findById(id)
                .map(user -> {
                    List<String> roles = user.getRoles();
                    if(roles.contains(role))
                        roles.remove(role);
                    return user;
                })
                .flatMap(userRepository::save)
                .doOnNext(savedUser -> log.info("saved user: {}", savedUser));
    }
}
