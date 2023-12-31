package com.example.demo.Controller;


import com.example.demo.Config.auth.PrincipalDetails;
import com.example.demo.Domain.Dto.MemberDto;
import com.example.demo.Domain.Dto.MusicDto;
import com.example.demo.Domain.Dto.MylistDto;
import com.example.demo.Domain.Entity.Music;
import com.example.demo.Domain.Entity.Mylist;
import com.example.demo.Domain.Service.MusicService;
import com.example.demo.Domain.Service.MylistService;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class MusicController {

	@Autowired
	MusicService musicService;

	@GetMapping("/song")
	public void song() {
		System.out.println("GET /song");
	}

	@GetMapping("/search")
	public void search(String searchText, String type, MusicDto dto, Model model) {
		log.info("GET /search");
		//search(searchText, type, dto)
		if(type!=null){
			dto.setType(dto.getType());
		}
		if(searchText!=null){
			dto.setSearchText(dto.getSearchText());
		}

		//서비스 실행
		Map<String,Object> map = musicService.GetSearchList(dto);

		List<Music> list = (List<Music>)map.get("list");

		List<MusicDto> searchList = list.stream().map(music -> MusicDto.Of(music)).collect(Collectors.toList());

		model.addAttribute("searchList", searchList);
		model.addAttribute("searchText", dto.getSearchText());
		model.addAttribute("type",dto.getType());

	}


	@GetMapping("/search/like")
	public String like(Long music_code,String searchText, String type) throws UnsupportedEncodingException {
		log.info("GET /search/like " +music_code + " " +type + " " + searchText);

		musicService.likeMusic(music_code);

		return "redirect:/search?type="+type+"&searchText="+ URLEncoder.encode(searchText, "UTF-8");
	}

	@GetMapping("/search/count")
	public String count(Long music_code,String searchText, String type) throws UnsupportedEncodingException {
		log.info("GET /search/count " + music_code + " " +type+" "+ searchText);

		musicService.CountUpMusic(music_code);

		return "redirect:/search?type="+type+"&searchText="+ URLEncoder.encode(searchText, "UTF-8");

		//search html파일 내 td title부분에 th:href="@{/search/count(music_code=${searchDto.music_code},type=${type},searchText=${searchText})}"  넣기!
	}


	@GetMapping("/Top100")
	public void top100(MusicDto dto, Model model){
		log.info("GET /Top100");

		List<Music> list = musicService.Top100();
		List<MusicDto> Top100List = list.stream().map(music -> MusicDto.Of(music)).collect(Collectors.toList());

		model.addAttribute("Top100List", Top100List);
	}

	@GetMapping("/Top100/like")
	public String like(Long music_code) {
		log.info("GET /Top100/like " +music_code);

		musicService.likeMusic(music_code);

		return "redirect:/Top100";
	}

}
