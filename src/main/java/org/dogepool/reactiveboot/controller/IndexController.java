/*
 * Copyright 2017 the original author or authors.
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
package org.dogepool.reactiveboot.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.dogepool.reactiveboot.config.DogeProperties;
import org.dogepool.reactiveboot.domain.UserStatRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Mark Paluch
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Controller
public class IndexController {

	UserStatRepository userStatRepository;
	DogeProperties dogeProperties;

	@GetMapping(path = "/")
	String getIndex(Model model, @RequestParam(required = false) Integer hashrateFilter) {

		model.addAttribute("poolName", dogeProperties.getPoolName());

		model.addAttribute(
				"hashLadder",
				userStatRepository.findTop10ByOrderByHashrateDesc().filter(
						userStat -> hashrateFilter == null
								|| userStat.getHashrate() > hashrateFilter));

		model.addAttribute(
				"coinsLadder",
				userStatRepository.findTop10ByOrderByTotalCoinsMinedDesc().filter(
						userStat -> hashrateFilter == null
								|| userStat.getHashrate() > hashrateFilter));

		// TODO: Calculate the overall gigaHashrate and miningUserCount taking
		// a reactive approach
		model.addAttribute("gigaHashrate", -1d);
		model.addAttribute("miningUserCount", -1);

		model.addAttribute("hashrateFilter", hashrateFilter != null ? hashrateFilter : "");

		return "index";
	}
}
