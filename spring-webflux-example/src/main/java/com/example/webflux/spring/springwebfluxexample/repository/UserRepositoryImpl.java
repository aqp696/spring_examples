package com.example.webflux.spring.springwebfluxexample.repository;

import com.example.webflux.spring.springwebfluxexample.controller.WebfluxUserController.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        users.put(1L, new User(1, "Alice", 20));
        users.put(2L, new User(2, "Bobby", 32));
        users.put(3L, new User(3, "Cindy", 41));
    }

    @Override
    public Mono<User> getById(long id) {
        return Mono.just(users.get(id));
    }

    @Override
    public Flux<User> getAll() {
        return Flux.fromIterable(users.values());
    }

    @Override
    public Mono<Void> save(Mono<User> user) {
        System.out.println("saved.");
        // return user.doOnNext(u -> users.put(u.getId(), u)).then();
        return user.map(u -> {
            users.put(u.getId(), u);
            return u;
        }).then();
    }

    @Override
    public Mono<Void> saveAll(Flux<User> users) {
        // return users.doOnNext(u -> this.users.put(u.getId(), u)).then();
        return users.map(u -> {
            this.users.put(u.getId(), u);
            return u;
        }).then();
    }

    @Override
    public Mono<User> update(long id, Mono<User> user) {
        return user.doOnNext(u -> users.put(id, u));
    }

    @Override
    public Mono<Void> delete(long id) {
        users.remove(id);
        return Mono.empty();
    }
}
