package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Movie;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

	List<Movie> findByYear(int year);

	boolean existsByTitleAndYear(String title, int year);

	@Query(value="SELECT * FROM movie WHERE director_id <> :artistId OR director_id IS NULL", nativeQuery = true)
	Iterable<Movie> findMoviesNotDirectedByArtist(@Param("artistId") Long id);

	@Query(value="SELECT * FROM movie WHERE movie.id NOT IN (SELECT starred_movies_id FROM movie_actors WHERE actors_id = :artistId)", nativeQuery = true)
	Iterable<Movie> findMoviesNotStarredByArtist(@Param("artistId") Long id);
	@Query(value="SELECT picture_id FROM movie WHERE picture_id IS NOT NULL", nativeQuery = true)
	Set<Long> findAllMovieImage();

	List<Movie> findByTitleContainingIgnoreCase(String movie);

	@Query(value = "SELECT picture_id FROM movie WHERE picture_id IS NOT NULL ORDER BY random() LIMIT 5", nativeQuery = true)
	Set<Long> findLast5MovieImages();
}