package it.uniroma3.siw.controller;


import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.controller.validator.ImageValidator;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.service.ArtistService;
import it.uniroma3.siw.service.MovieService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import it.uniroma3.siw.controller.validator.MovieValidator;
import it.uniroma3.siw.controller.validator.ReviewValidator;

import it.uniroma3.siw.model.Movie;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class MovieController {
	@Autowired
	private MovieService movieService;
	@Autowired
	private MovieValidator movieValidator;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewValidator reviewValidator;
	@Autowired
	private ArtistService artistService;
	@Autowired
	private UserService userService;
	@Autowired
	private SessionData sessionData;
	@Autowired
	private ImageValidator imageValidator;


	@GetMapping(value = "/admin/formNewMovie")
	public String formNewMovie(Model model) {
		model.addAttribute("movie", new Movie());
		return "admin/formNewMovie";
	}
	@PostMapping("/admin/movie")
	public String newMovie(@Valid @ModelAttribute("movie") Movie movie, BindingResult movieBindingResult,
						   @Valid @ModelAttribute MultipartFile file, BindingResult fileBindingResult, Model model, RedirectAttributes redirectAttributes){
		this.movieValidator.validate(movie, movieBindingResult);
		if(!file.isEmpty()) this.imageValidator.validate(file, fileBindingResult);
		if (!movieBindingResult.hasErrors() && !fileBindingResult.hasErrors()) {
			try{
				model.addAttribute("movie", this.movieService.saveMovie(movie, file));
				return "redirect:/admin/formUpdateMovie/" + movie.getId();
			} catch (IOException e){
				redirectAttributes.addFlashAttribute("fileUploadError", "errore imprevisto nell'upload");
				return "admin/formNewMovie";
			}
		}
		return "admin/formNewMovie";
	}

	@GetMapping(value = "/admin/formUpdateMovie/{id}")
	public String formUpdateMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovie(id));
		model.addAttribute("directorsList", this.artistService.findAllDirectorsNotInMovie(id));
		model.addAttribute("actorsList", this.artistService.findActorsNotInMovie(id));

		return "admin/formUpdateMovie";
	}

	@GetMapping(value = "/admin/manageMovies")
	public String manageMovies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "admin/manageMovies";
	}

	@GetMapping(value = "/admin/setDirectorToMovie/{directorId}/{movieId}")
	public String setDirectorToMovie(@PathVariable("directorId") Long directorId, @PathVariable("movieId") Long movieId) {
		this.movieService.setDirectorToMovie(movieId, directorId);
		return "redirect:/admin/formUpdateMovie/" + movieId;
	}

	@GetMapping(value = "/admin/removeDirector/{id}")
	public String removeDirector(@PathVariable("id") Long id){
		this.movieService.removeDirector(id);
		return "redirect:/admin/formUpdateMovie/" + id;
	}

	@GetMapping(value="/movie/{id}")
	public String getMovie(@PathVariable("id") Long id, Model model) {
		model.addAttribute("movie", this.movieService.findMovie(id));
		model.addAttribute("averageRating", this.reviewService.getAverageRatingByMovie(id));
		if(this.sessionData.getLoggedUser() != null) model.addAttribute("newReview", new Review());
		model.addAttribute("reviewAuthorSet",  this.userService.getAllMovieReviewsAuthors(id));
		return "/movie/movie";
	}
	@GetMapping(value="/movie")
	public String getMovies(Model model) {
		model.addAttribute("movies", this.movieService.getAllMovies());
		return "movie/movies";
	}

	@PostMapping(value = "/find")
	public String cercaFilm(Model model, @RequestParam String searchKeyword){
		model.addAttribute("movies", this.movieService.searchMovie(searchKeyword));
		return "/movie/foundMovies";
	}

	@GetMapping(value = "/admin/addActorToMovie/{actorId}/{movieId}")
	public String addActorToMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId) {
		this.movieService.addActorToMovie(movieId, actorId);
		return "redirect:/admin/formUpdateMovie/" + movieId;
	}

	@GetMapping(value = "/admin/removeActorFromMovie/{actorId}/{movieId}")
	public String removeActorFromMovie(@PathVariable("actorId") Long actorId, @PathVariable("movieId") Long movieId) {
		this.movieService.removeActorFromMovie(movieId, actorId);
		return "redirect:/admin/formUpdateMovie/" + movieId;
	}

	@PostMapping(value="/admin/updateTitle/{movieId}")
	public String updateTitle(@RequestParam("title") String title, @PathVariable("movieId") Long id){
		this.movieService.updateTitle(title, id);
		return "redirect:/admin/formUpdateMovie/" + id;
	}

	@PostMapping(value = "/admin/updateMovieImage/{id}")
	public String updateImage(@PathVariable("id") Long id , @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
		Movie movie = this.movieService.findMovie(id);

		//andrebbe validata!
		try {
			this.movieService.saveMovie(movie, file);
		} catch(IOException e) {
			redirectAttributes.addFlashAttribute("fileUploadError", "errore imprevisto nell'upload!");
		}

		return "redirect:/admin/formUpdateMovie/"+id;
	}


	@PostMapping(value = "/movie/addNewReviewToMovie/{movieId}")
	public String newReview(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult,
							@PathVariable("movieId") Long movieId, Model model){
		this.reviewValidator.validate(review, bindingResult);
		if(!bindingResult.hasErrors()) {
			this.reviewService.saveReview(review, this.movieService.findMovie(movieId), this.sessionData.getLoggedUser());
			model.addAttribute("averageRating", this.reviewService.getAverageRatingByMovie(movieId));
		}
		model.addAttribute("movie", this.movieService.findMovie(movieId));
		model.addAttribute("averageRating", this.reviewService.getAverageRatingByMovie(movieId));
		model.addAttribute("reviewAuthorSet",  this.userService.getAllMovieReviewsAuthors(movieId));
		return "/movie/movie";
	}

	@GetMapping(value = "/admin/removeMovie/{movieId}")
	public String removeMovie(@PathVariable("movieId")Long id){
		this.movieService.deleteMovie(id);
		return "redirect:/admin/manageMovies";
	}
}


