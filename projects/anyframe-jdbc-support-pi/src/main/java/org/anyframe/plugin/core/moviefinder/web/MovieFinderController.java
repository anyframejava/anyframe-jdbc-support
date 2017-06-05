/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.anyframe.plugin.core.moviefinder.web;

import javax.inject.Inject;
import javax.inject.Named;

import org.anyframe.plugin.core.moviefinder.service.MovieFinder;
import org.anyframe.plugin.core.domain.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.anyframe.pagination.Page;

/**
 * This MovieFinderController class is a Controller class to provide movie list
 * functionality.
 * 
 * @author Sooyeon Park
 */
@Controller("coreMovieFinderController")
@RequestMapping("/coreMovieFinder.do")
public class MovieFinderController {

	@Inject
	@Named("coreMovieFinder")
	private MovieFinder movieFinder;

	@RequestMapping(params = "method=list")
	public String list(
			@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
			Movie movie, BindingResult result, Model model) throws Exception {

		Page resultPage = movieFinder.getPagingList(movie, pageIndex);

		model.addAttribute("movie", movie);
		model.addAttribute("movies", resultPage.getList());
		model.addAttribute("resultPage", resultPage);

		return "core/moviefinder/movie/list";
	}
}
