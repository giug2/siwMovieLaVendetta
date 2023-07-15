package it.uniroma3.siw.model;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @Min(1900)
    @Max(2023)
    private Integer year;
    @OneToOne(cascade = CascadeType.ALL)
    private Image picture;
    @ManyToOne
    private Artist director;
    @ManyToMany
    private Set<Artist> actors;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Review> reviews;

    public Movie(){
        this.actors = new HashSet<>();
        this.reviews = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear( Integer year) {
        this.year = year;
    }

    public Image getPicture() {
        return picture;
    }

    public void setPicture(Image image) {
        this.picture = image;
    }

    public Artist getDirector() {
        return director;
    }

    public void setDirector(Artist director) {
        this.director = director;
    }

    public Set<Artist> getActors() {
        return actors;
    }

    public void setActors(Set<Artist> actors) {
        this.actors = actors;
    }

    public List<Review> getReviews() { return reviews; }

    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public void addReview(Review review) { this.reviews.add(review); }

    @Override
    public int hashCode() {
        return Objects.hash(title, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movie other = (Movie) obj;
        return Objects.equals(this.title, other.title) && this.year.equals(other.year);
    }
}
