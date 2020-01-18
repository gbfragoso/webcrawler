package ai.overmind.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MovieController {
	
	private final MovieService movieService;
	
	@Autowired
	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@RequestMapping("/")
	public String index(Model model) {
		model.addAttribute("movieList", movieService.parse());
		return "index";
	}
}
