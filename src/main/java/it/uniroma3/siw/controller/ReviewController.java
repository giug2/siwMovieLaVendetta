package it.uniroma3.siw.controller;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.controller.validator.ReviewValidator;
import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import it.uniroma3.siw.model.Review;

@Controller
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewValidator reviewValidator;
    @Autowired
    SessionData sessionData;

    @GetMapping(value = "/admin/removeReview/{reviewId}")
    public String adminRemoveReview(@PathVariable("reviewId")Long reviewId){
        Review review = this.reviewService.findReview(reviewId);
        Movie movie = review.getMovie();
        this.reviewService.deleteReview(review);
        return "redirect:/movie/" + movie.getId();
    }

    @GetMapping(value="/removeReview/{reviewId}")
    public String authorRemoveReview(@PathVariable("reviewId")Long reviewId, Model model){
        Review review = this.reviewService.findReview(reviewId);
        Movie movie = review.getMovie();
        //CONTROLLA SE LA CHIAMATA GET NON E' STATA FORMATA MANUALMENTE DA UN ALTRO UTENTE
        if(!this.sessionData.getLoggedUser().equals(review.getAuthor()))
            return "/errors/error404";

        this.reviewService.deleteReview(review);
        return "redirect:/movie/" + movie.getId();
    }
}
