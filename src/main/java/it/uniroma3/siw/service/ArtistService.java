package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Artist;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@Transactional
public class ArtistService{
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    ImageService imageService;

    @Transactional
    public Artist saveArtist(Artist artist) {
        return this.artistRepository.save(artist);
    }
    @Transactional
    public Artist saveArtist(Artist artist, MultipartFile file) throws IOException {
        if(!file.isEmpty()) artist.setPicture(this.imageService.save(file));
        return this.artistRepository.save(artist);
    }
    @Transactional
    public Artist findArtist(Long id) {
        return this.artistRepository.findById(id).orElse(null);
    }
    @Transactional
    public Iterable<Artist> findAll() {
        return this.artistRepository.findAll();
    }

    @Transactional
    public boolean exists(Artist artist) {
        return this.artistRepository.existsByNameAndSurname(artist.getName(), artist.getSurname());
    }
    @Transactional
    public Iterable<Artist> findAllDirectorsNotInMovie(Long id) {
        return this.artistRepository.findAllDirectorsNotInMovie(id);
    }
    @Transactional
    public Iterable<Artist> findActorsNotInMovie(Long id) {
        return this.artistRepository.findActorsNotInMovie(id);
    }

    @Transactional
    public void deleteArtist(Long id) {
        Artist artist = this.findArtist(id);

        for(Movie m : artist.getDirectedMovies())       //cancello associazione regista
            m.setDirector(null);

        for(Movie m : artist.getStarredMovies())       //cancello associazione attore
            m.getActors().remove(artist);

        this.artistRepository.delete(artist);
    }

    @Transactional
    public Set<Long> getAllArtistImages() {
        return this.artistRepository.findAllArtistImage();

    }
}
