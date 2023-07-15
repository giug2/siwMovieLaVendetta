package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.validator.ArtistValidator;
import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.model.Artist;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class ArtistController {
	 @Autowired
	 private ArtistService artistService;
	 @Autowired
	 private MovieService movieService;
	 @Autowired
	 private ArtistValidator artistValidator;

	@GetMapping(value="/admin/formNewArtist")
	public String formNewArtist(Model model) {
		model.addAttribute("artist", new Artist());
		return "admin/formNewArtist";
	}

	@PostMapping("/admin/artist")
	public String newArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult, Model model) {
		this.artistValidator.validate(artist, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.artistService.saveArtist(artist);
			model.addAttribute("artist", artist);
			return "redirect:/admin/formUpdateArtist/" + artist.getId();
		}
		return "admin/formNewArtist";
	}

	@GetMapping("/artist/{id}")
	public String getArtist(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findArtist(id));
		return "artist/artist";
	}

	@GetMapping("/artist")
	public String getArtists(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "artist/artists";
	}

	@GetMapping(value = "/admin/manageArtists")
	public String manageMovies(Model model) {
		model.addAttribute("artists", this.artistService.findAll());
		return "admin/manageArtists";
	}

	@GetMapping(value = "/admin/removeMovieFromStarredMovies/{actorId}/{movieId}")
	public String removeMovieFromStarredMovies(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM){
		this.movieService.removeActorFromMovie(idM, idA);
		return "redirect:/admin/formUpdateArtist/" + idA;
	}

	@GetMapping(value = "/admin/removeMovieFromDirectedMovies/{actorId}/{movieId}")
	public String removeMovieFromDirectedMovies(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM){
		this.movieService.removeDirector(idM);
		return "redirect:/admin/formUpdateArtist/" + idA;
	}

	@GetMapping(value = "/admin/addMovieToStarredMovies/{actorId}/{movieId}")
	public String addMovieToStarredMovies(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM) {
		this.movieService.addActorToMovie(idM, idA);
		return "redirect:/admin/formUpdateArtist/" + idA;
	}

	@GetMapping(value = "/admin/addMovieToDirectedMovies/{actorId}/{movieId}")
	public String addMovieToDirectedMovies(@PathVariable("actorId") Long idA, @PathVariable("movieId") Long idM) {
		this.movieService.setDirectorToMovie(idM, idA);
		return "redirect:/admin/formUpdateArtist/" + idA;
	}

	@GetMapping(value = "/admin/removeArtist/{id}")
	public String removeArtist(@PathVariable("id")Long id){
		this.artistService.deleteArtist(id);
		return "redirect:/admin/manageArtists";
	}
	@GetMapping(value = "/admin/formUpdateArtist/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("artist", this.artistService.findArtist(id));
		model.addAttribute("directedMoviesList", this.movieService.findMoviesNotDirectedByArtist(id));
		model.addAttribute("starredMoviesList", this.movieService.findMoviesNotStarredByArtist(id));
		return "admin/formUpdateArtist";
	}
	@PostMapping(value = "/admin/updateArtistImage/{id}")
	public String updateImage(@PathVariable("id") Long id , @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
		Artist artist = this.artistService.findArtist(id);

		//andrebbe validata!
		try {
			this.artistService.saveArtist(artist, file);
		} catch(IOException e) {
			redirectAttributes.addFlashAttribute("fileUploadError", "errore imprevisto nell'upload!");
		}

		return "redirect:/admin/formUpdateArtist/"+id;
	}
}
