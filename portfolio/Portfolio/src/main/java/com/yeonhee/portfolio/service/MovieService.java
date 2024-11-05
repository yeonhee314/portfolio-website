package com.yeonhee.portfolio.service;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class MovieService {
	// 하루 전 날짜 구하기
	private String getYesterday() {
		Date date = new Date();
		date = new Date(date.getTime() + (1000* 60 * 60 * 24 * -1));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	// TODO : API 키는 깃허브에 올리면 안된다. 따로 빼서 저장하기
	
	// 영화 순위 
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getMovieRanking(){
		// 영화진흥위원회 API
		// audiAcc : 누적 관객수
		String url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=3a97b57ea0c5210615e13e6fc1ef2a0e&targetDt=" + getYesterday();
		
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);
		
		Gson gson = new Gson(); 
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> resultMap = gson.fromJson(response, type);
		
		Map<String, Object> boxOfficeResult = (Map<String, Object>) resultMap.get("boxOfficeResult");
		List<Map<String, String>> dailyBoxOfficeList = (List<Map<String, String>>)boxOfficeResult.get("dailyBoxOfficeList");
		
		return dailyBoxOfficeList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getKMDBMovieRanking(){
		// KMDB에서는 영화 상세정보, 장르명(genre), 감독명(director), 배우명(actor), 키워드(keyword), 줄거리(plot), 포스터만 불러온다.
		String url = "";
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		List<Map<String, String>> kmdbRankList = new ArrayList<>();
		List<Map<String, String>> dailyRankList = getMovieRanking();
		RestTemplate restTemplate = new RestTemplate();
		
		for(int i = 0; i < dailyRankList.size(); i++) {
			String openDt = dailyRankList.get(i).get("openDt");
			String movieName = dailyRankList.get(i).get("movieNm");
			url = "http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&ServiceKey=08W80P3253E9D86EO1QM&sort=prodYear,1&releaseDts=" + openDt + "&detail=Y&query=" + movieName;
			
			String response = restTemplate.getForObject(url, String.class);
			Map<String, Object> resultMap = gson.fromJson(response, type);
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) resultMap.get("Data");
			List<Map<String, Object>> kmdbResults = (List<Map<String, Object>>) dataList.get(0).get("Result");
			
			
			
			for(Map<String, Object> movie : kmdbResults) {
				 Map<String, String> movieInfo = new HashMap<>();
				// TODO : 만약 포스터가 없을 경우 예외처리가 필요하다.
				String postersUrl = (String)movie.get("posters");
				String[] poster = postersUrl.split("\\|");
				 
				 movieInfo.put("genre"   , (String)movie.get("genre"));
				 movieInfo.put("director", (String)movie.get("director"));
				 movieInfo.put("actor"   , (String)movie.get("actor"));
				 movieInfo.put("keyword" , (String)movie.get("keyword"));
				 movieInfo.put("plot"    , (String)movie.get("plot"));
				 movieInfo.put("poster"  , poster[0]);
				 kmdbRankList.add(movieInfo);
			}
		}
		return kmdbRankList;
	}
	
}
