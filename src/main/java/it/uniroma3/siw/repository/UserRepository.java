package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE users.id IN (SELECT author_id FROM review WHERE movie_id = :movieId)", nativeQuery = true)
    Set<User> findAllReviewAuthorsByMovie(Long movieId);

    boolean existsByEmail(String email);
}