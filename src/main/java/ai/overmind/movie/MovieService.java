package ai.overmind.movie;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
	
	public final String baseUrl = "https://www.imdb.com";
	
	public List<Movie> parse() {
		StringBuffer url = new StringBuffer();
		url.append("https://www.imdb.com/chart/bottom?");
		url.append("pf_rd_m=A2FGELUUNOQJNL");
		url.append("&pf_rd_p=4da9d9a5-d299-43f2-9c53-f0efa18182cd");
		url.append("&pf_rd_r=QP0SVQJY4TSP3NK3B117");
		url.append("&pf_rd_s=right-4");
		url.append("&pf_rd_t=15506");
		url.append("&pf_rd_i=top");
		url.append("&ref_=chttp_ql_8");
		
		Document imdbPage = getDocumentForm(url.toString());
		Elements lowestRatingMovies = getLowestRatingMovies(imdbPage);
		return getMovieInformationFrom(lowestRatingMovies);
	}
	
	private Document getDocumentForm(String url) {
		try {
			return Jsoup.connect(url).header("Accept-Language", "en-US").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Document(baseUrl);
	}
	
	private Elements getLowestRatingMovies(Document imdbPage) {
		Element movieList = imdbPage.getElementsByAttributeValue("class","lister-list").first();
		return movieList.getElementsByClass("titleColumn");
	}
	
	private List<Movie> getMovieInformationFrom(Elements movieList) {
		List<Movie> movies = new ArrayList<>();
		int maxSize = (movieList.size() > 10) ? 9 : movieList.size();
		
		for (int i = maxSize; i >= 0; i--) {
			Element item = movieList.get(i);
			Document moviePage = getDocumentForm(baseUrl + getMovieUrl(item));
			
			String movieId = getMovieId(item);
			
			Movie movie = new Movie(movieId);
			movie.setTitle(getMovieTitle(moviePage));
			movie.setDirector(getMovieDirectors(moviePage));
			movie.setPoster(getMoviePoster(moviePage));
			movie.setCast(getMovieCast(moviePage));
			movie.setRating(getMovieRating(moviePage));
			movie.setReview(getMovieReviews(movieId));
			movie.setSummary(getMovieSummary(moviePage));
			movies.add(movie);
		}
		
		movies.sort((Movie o1, Movie o2) -> o2.getRating().compareTo(o1.getRating()));
		return movies;
	}

	private String getMovieUrl(Element movie) {
		return movie.getElementsByAttribute("href").attr("href");
	}
	
	private String getMovieId(Element movie) {
		return getMovieUrl(movie).split("/")[2];
	}
	
	private String getMovieTitle(Document moviePage) {
		String originalTitle = moviePage.getElementsByAttributeValue("class","originalTitle").text();
		return (originalTitle == null || originalTitle.isEmpty()) ? 
				moviePage.select("div.title_wrapper > h1").first().text() :
				originalTitle;
	}
	
	private String getMovieDirectors(Document moviePage) {
		Element directors = moviePage.getElementsByAttributeValue("class","credit_summary_item").first();
		return directors.text();
	}
	
	private String getMoviePoster(Document moviePage) {
		Element div = moviePage.selectFirst("div.poster");
		String link = div.getElementsByTag("img").attr("src");
		return link.substring(0, link.indexOf("_V1_")+4) + ".jpg";
	}

	private String getMovieCast(Document moviePage) {
		Element titleCast = moviePage.getElementById("titleCast");
		Elements cast = titleCast.select("td:not([class])");
		return cast.stream().map(Element::text).collect(Collectors.joining(", "));
	}
	
	private BigDecimal getMovieRating(Document moviePage) {
		return new BigDecimal(moviePage.getElementsByAttributeValue("itemprop", "ratingValue").text());
	}
	
	private String getMovieSummary(Document moviePage) {
		return moviePage.selectFirst("div.summary_text").text();
	}

	private String getMovieReviews(String movieId) {
		StringBuffer url = new StringBuffer();
		url.append(baseUrl);
		url.append("/title/");
		url.append(movieId);
		url.append("/reviews?sort=userRating&dir=desc&ratingFilter=0");
		Document reviewPage = getDocumentForm(url.toString());
		
		Elements reviews = reviewPage.getElementsByClass("review-container");
		for (Element review :  reviews) {
			Element nota = review.select("span.rating-other-user-rating").first();
			if(nota != null && Integer.valueOf(nota.text().split("/")[0]) >= 5) {
				StringBuffer text = new StringBuffer();
				text.append(review.selectFirst("a.title").text());
				text.append("\n");
				text.append(review.selectFirst("div.show-more__control").text());
				return text.toString();
			}
		}
		
		return "";
	}
}
