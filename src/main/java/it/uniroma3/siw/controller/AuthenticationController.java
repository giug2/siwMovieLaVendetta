package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.ImageValidator;
import it.uniroma3.siw.controller.validator.UserValidator;
import it.uniroma3.siw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class AuthenticationController {
	@Autowired
	private CredentialsService credentialsService;
    @Autowired
	private UserService userService;
	@Autowired
	private SessionData sessionData;
	@Autowired
	private UserValidator userValidator;
	@Autowired
	private CredentialsValidator credentialsValidator;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MovieService movieService;
	@Autowired
	private ArtistService artistService;
	@Autowired
	private ImageValidator imageValidator;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("userData", new User());
		model.addAttribute("userCredentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		model.addAttribute("movieImages", this.movieService.getLast5movie());
		model.addAttribute("artistImages", this.artistService.getAllArtistImages());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index";
		}
		else if (this.sessionData.getLoggedCredentials().getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin";
			}
        return "index";
	}

	@PostMapping(value = "/register" )
    public String registerUser(@Valid @ModelAttribute("userData") User user, BindingResult userBindingResult,
			     @Valid @ModelAttribute("userCredentials") Credentials credentials, BindingResult credentialsBindingResult,
				 @Valid @ModelAttribute MultipartFile file, BindingResult fileBindingResult, Model model) {
		this.userValidator.validate(user, userBindingResult);
		this.credentialsValidator.validate(credentials, credentialsBindingResult);
		if(!file.isEmpty()) this.imageValidator.validate(file, fileBindingResult);
		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors() && !fileBindingResult.hasErrors()) {
			try{
				User u = userService.saveUser(user, file);
				credentials.setUser(u);
				credentialsService.saveCredentials(credentials);
				model.addAttribute("user", u);
				return "redirect:/profile";
			}catch (IOException e){
				model.addAttribute("fileUploadError", "errore imprevisto nell'upload!");
			}
        }
        return "formRegisterUser";
    }

	@GetMapping(value = "/profile")
	public String profile(Model model){
		if(this.sessionData.getLoggedUser() == null)
			return "redirect:/login";

		User loggedUser = this.userService.getUser(this.sessionData.getLoggedUser().getId());
		model.addAttribute("userReviews", this.reviewService.findAllReviewsWrittenByUser(loggedUser));
		//model.addAttribute("userProfile", loggedUser);
		return "profile";
	}

	@GetMapping(value="/user/formUpdateProfile")
	public String formUpdateProfilo() {
		return "/user/formUpdateProfile";
	}

	@PostMapping (value="/user/updateProfile")
	public String updateProfilo(@Valid @ModelAttribute("userDetails") User user, BindingResult userBindingResult,
								@Valid @ModelAttribute MultipartFile file, BindingResult fileBindingResult,
								Model model) {
		//this.autoreValidator.validate(autore, autoreBindingResult);
		if(!file.isEmpty()) this.imageValidator.validate(file, fileBindingResult);
		if(!fileBindingResult.hasErrors()){
			try {
				this.userService.saveUser(user, file);
				return "redirect:/profile";
			} catch(IOException e) {
				model.addAttribute("fileUploadError", "errore imprevisto nell'upload!");
			}
		}
		return "/user/formUpdateProfile";
	}

	@GetMapping(value="/user/deleteProfilePicture")
	public String deleteProfilePicture() {
		this.userService.deletePicture(this.sessionData.getLoggedUser());
		return "redirect:/user/formUpdateProfile";
	}
}