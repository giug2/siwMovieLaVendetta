package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    
	boolean existsByMovieAndAuthor(Movie movie, User author);
    
	Set<Review> findAllByAuthor(User author);
    
	Iterable<Review> findAllByMovie(Movie movie);
    
	@Query(value = "SELECT AVG(mark) FROM review WHERE movie_id = :movieId", nativeQuery = true)
    Double getAverageRatingByMovie(@Param("movieId")Long id);
   
	// Iterable<Review> findByIdMovie(Long id);
}
