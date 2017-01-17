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
import org.dogepool.reactiveboot.domain.UserRepository;
import org.dogepool.reactiveboot.domain.UserStatRepository;
import reactor.core.publisher.Mono;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Mark Paluch
 */
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MinerController {

	UserRepository userRepository;
	UserStatRepository userStatRepository;
	WebClient webClient = WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector()).build()
			.filter(userAgent());

	@GetMapping("/miner/{id}")
	String getMiner(@PathVariable String id, Model model) {

		// Mono<MinerModel> compositeModel = …;
		model.addAttribute("model", Mono.empty());

		return "miner";
	}

	private ExchangeFilterFunction userAgent() {

		return (clientRequest, exchangeFunction) -> {
			ClientRequest newRequest = ClientRequest.from(clientRequest)
					.header("User-Agent", "Spring Framework WebClient").build();
			return exchangeFunction.exchange(newRequest);
		};
	}
}
