package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users") 
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	@Column(nullable = false, unique = true)
	@Email
	@NotBlank
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	private Image picture;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<Review> reviews;

	
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public @NotNull String getName() {
		return name;
	}
	
	public void setName(@NotNull String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public Image getPicture() {
		return picture;
	}

	public void setPicture(Image picture) { 
		this.picture = picture; 
	}

	public List<Review> getReviews() { 
		return reviews;
	}

	public void setReviews(List<Review> reviews) { 
		this.reviews = reviews; 
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(email);
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}