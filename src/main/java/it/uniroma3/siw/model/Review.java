package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer mark;
    private String text;
    @ManyToOne
    private User author;
    @ManyToOne
    private Movie movie;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public @NotNull Integer getMark() {
        return mark;
    }

    public void setMark(@NotNull Integer mark) {
        this.mark = mark;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User writer) {
        this.author = writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review review)) return false;

        if (!author.equals(review.author)) return false;
        return movie.equals(review.movie);
    }

    @Override
    public int hashCode() {
        int result = author.hashCode();
        result = 31 * result + movie.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", mark=" + mark +
                ", text='" + text + '\'' +
                ", writer=" + author +
                ", movie=" + movie +
                '}';
    }
}
