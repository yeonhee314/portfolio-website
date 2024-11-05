package com.yeonhee.portfolio.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.yeonhee.portfolio.service.MovieService;


@Controller
public class HomeController {
	@Autowired
	private MovieService movieService;
	
	// 메인 페이지
	@GetMapping(value={"/", "/index"})
	public String index() {
			
		return "index";
	}
	
	@GetMapping("/movie")
	public String movieMain(Model model) {
		List<Map<String, String>> dailyRankList = movieService.getMovieRanking();	 // 영화진흥위원회 API
		List<Map<String, String>> kmdbRankList = movieService.getKMDBMovieRanking(); // KMDB API
		model.addAttribute("dailyRankList", dailyRankList);
		model.addAttribute("kmdbRankList", kmdbRankList);
		
		return "Movie/movie";
	}
}
