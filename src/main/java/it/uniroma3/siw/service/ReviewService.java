package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Movie;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    MovieService movieService;

    @Transactional
    public Review saveReview(Review review, Movie movie, User author){
        movie.addReview(review);
        review.setMovie(movie);
        review.setAuthor(author);

        return this.reviewRepository.save(review);
    }
    @Transactional
    public boolean exists(Review review) {
        return this.reviewRepository.existsByMovieAndAuthor(review.getMovie(), review.getAuthor());
    }

    @Transactional
    public Double getAverageRatingByMovie(Long movieId){
        return this.reviewRepository.getAverageRatingByMovie(movieId);
    }

    @Transactional
    public void deleteReview(Review review) {
        this.reviewRepository.delete(review);
    }
    @Transactional
    public Review findReview(Long reviewId) {
        return this.reviewRepository.findById(reviewId).orElse(null);
    }

    @Transactional
    public Set<Review> findAllReviewsWrittenByUser(User loggedUser) {
        return this.reviewRepository.findAllByAuthor(loggedUser);
    }
}
