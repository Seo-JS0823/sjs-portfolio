package com.jubu.pofol.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jubu.pofol.config.AboutMeProperties;
import com.jubu.pofol.service.CareerService;
import com.jubu.pofol.service.LinkCardService;
import com.jubu.pofol.service.PageReader;
import com.jubu.pofol.service.PostService;
import com.jubu.pofol.service.TagService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final PageReader pageService;
	private final AboutMeProperties aboutMe;
	private final LinkCardService lcService;
	private final TagService tagService;
	private final PostService postService;
	private final CareerService careerService;

	@GetMapping
	public String index(Model model) {
		model.addAttribute("about", aboutMe);
		model.addAttribute("links", lcService.getList().lcs());
		model.addAttribute("tagGroups", tagService.getTagSummary().tags());
		model.addAttribute("posts", postService.getList().posts());
		model.addAttribute("careers", careerService.getList().careers());

		return "index";
	}
}
