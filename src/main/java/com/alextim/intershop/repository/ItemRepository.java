package com.alextim.intershop.repository;

import com.alextim.intershop.entity.Item;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long> {

    @Query("SELECT * FROM Items i WHERE " +
            "UPPER(i.title) LIKE CONCAT('%',UPPER(:search),'%') OR " +
            "UPPER(i.description) LIKE CONCAT('%',UPPER(:search),'%')")
    Flux<Item> findByTitleOrDescriptionContains(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Items i WHERE " +
            "UPPER(i.title) LIKE CONCAT('%',UPPER(:search),'%') OR " +
            "UPPER(i.description) LIKE CONCAT('%',UPPER(:search),'%')")
    Mono<Long> countByTitleOrDescriptionContains(String search);

    Flux<Item> findAllBy(Pageable pageable);
}
