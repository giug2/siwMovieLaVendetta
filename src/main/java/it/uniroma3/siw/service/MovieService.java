package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ArtistService artistService;
    @Autowired
    private ImageService imageService;

    @Transactional
    public Movie saveMovie(Movie movie, MultipartFile image) throws IOException {
        if(!image.isEmpty()) this.addMovieImage(movie, image);
        return this.movieRepository.save(movie);
    }

    @Transactional
    public Movie saveMovie(Movie movie){
        return this.movieRepository.save(movie);
    }
    @Transactional
    public Movie findMovie(Long id) {
        return this.movieRepository.findById(id).orElse(null);
    }
    public Iterable<Movie> findMovies(int year){
        return this.movieRepository.findByYear(year);
    }
    @Transactional
    public Iterable<Movie> getAllMovies() {
        return this.movieRepository.findAll();
    }
    @Transactional
    public boolean exists(Movie movie){
        return movie.getTitle() != null && this.movieRepository.existsByTitleAndYear(movie.getTitle(), movie.getYear());
    }
    @Transactional
    public Iterable<Movie> findMoviesNotDirectedByArtist(Long id) {
        return this.movieRepository.findMoviesNotDirectedByArtist(id);
    }
    @Transactional
    public Iterable<Movie> findMoviesNotStarredByArtist(Long id) {
        return this.movieRepository.findMoviesNotStarredByArtist(id);
    }
    @Transactional
    public void removeActorFromMovie(Long movieId, Long actorId) {
        Movie movie = this.findMovie(movieId);
        Artist actor = this.artistService.findArtist(actorId);
        movie.getActors().remove(actor);
        actor.getStarredMovies().remove(movie);
        this.saveMovie(movie);
        this.artistService.saveArtist(actor);
    }
    @Transactional
    public void updateTitle(String title, Long id) {
        Movie movie = this.findMovie(id);
        movie.setTitle(title);
        this.saveMovie(movie);
    }
    @Transactional
    public void addActorToMovie(Long movieId, Long actorId) {
        Movie movie = this.findMovie(movieId);
        Artist actor = this.artistService.findArtist(actorId);
        actor.getStarredMovies().add(movie);
        movie.getActors().add(actor);
        this.saveMovie(movie);
        this.artistService.saveArtist(actor);
    }
    @Transactional
    public void removeDirector(Long movieId) {
        Movie movie = this.findMovie(movieId);
        Artist director = movie.getDirector();
        movie.setDirector(null);
        director.getDirectedMovies().remove(movie);
        this.saveMovie(movie);
        this.artistService.saveArtist(director);
    }
    @Transactional
    public void setDirectorToMovie(Long movieId, Long directorId) {
        Movie movie = this.findMovie(movieId);
        Artist director = this.artistService.findArtist(directorId);
        movie.setDirector(director);
        director.getDirectedMovies().add(movie);
        this.saveMovie(movie);
        this.artistService.saveArtist(director);
    }
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = this.findMovie(id);
        Artist director = movie.getDirector();
        if(director != null)
            director.getDirectedMovies().remove(movie);      //rimuovo l'associazione con il regista

        for(Artist a : movie.getActors())
            a.getStarredMovies().remove(movie);             //rimuovo il film da ogni collezione di movie in actor

        movie.getActors().clear();                          //svuoto la collezione di attori nel film

        this.movieRepository.delete(movie);
    }
    @Transactional
    public void addMovieImage(Movie movie, MultipartFile image) throws IOException {
        movie.setPicture(this.imageService.save(image));
    }
    @Transactional
    public Set<Long> getAllMovieImages() {
        return this.movieRepository.findAllMovieImage();
    }

    @Transactional
    public List<Movie> searchMovie(String movie) {
        return this.movieRepository.findByTitleContainingIgnoreCase(movie);
    }

    @Transactional
    public Set<Long> getLast5movie() {
        return this.movieRepository.findLast5MovieImages();
    }
}
