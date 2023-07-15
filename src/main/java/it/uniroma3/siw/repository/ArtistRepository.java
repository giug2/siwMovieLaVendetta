package it.uniroma3.siw.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Artist;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Long> {

	boolean existsByNameAndSurname(String name, String surname);

	@Query(value="select * "
			+ "from artist a "
			+ "where a.id not in "
			+ "(select actors_id "
			+ "from movie_actors "
			+ "where movie_actors.starred_movies_id = :movieId)", nativeQuery=true)
	Iterable<Artist> findActorsNotInMovie(@Param("movieId") Long id);

	@Query(value="SELECT * FROM artist WHERE artist.id NOT IN (SELECT director_id FROM movie WHERE movie.id = :movieId AND director_id IS NOT NULL)", nativeQuery = true)
	Iterable<Artist> findAllDirectorsNotInMovie(@Param("movieId") Long id);

	@Query(value="SELECT picture_id FROM artist WHERE picture_id IS NOT NULL", nativeQuery = true)
	Set<Long> findAllArtistImage();
}