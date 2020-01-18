package ai.overmind.movie;

import java.io.Serializable;
import java.math.BigDecimal;

public class Movie implements Serializable{
	
	private static final long serialVersionUID = 1852492003640832342L;
	
	private final String id;
	private String title;
	private String director;
	private String poster;
	private String cast;
	private String review;
	private String summary;
	private BigDecimal rating;
	
	public Movie(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
}
