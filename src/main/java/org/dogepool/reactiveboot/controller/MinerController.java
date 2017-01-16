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

import java.nio.ByteBuffer;
import java.util.Optional;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.dogepool.reactiveboot.domain.User;
import org.dogepool.reactiveboot.domain.UserProfile;
import org.dogepool.reactiveboot.domain.UserRepository;
import org.dogepool.reactiveboot.domain.UserStatRepository;
import org.dogepool.reactiveboot.view.model.MinerModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.client.HttpClient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mark Paluch
 */
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MinerController {

	UserRepository userRepository;
	UserStatRepository userStatRepository;
	HttpClient httpClient = HttpClient.create();

	@GetMapping("/miners/{id}")
	Mono<MinerModel> getMiner(@PathVariable String id) {

		return userRepository.findOne(id)
				.and(user -> userStatRepository.findByUserId(user.getId()))
				.map(tuple -> {

					return MinerModel.of(tuple.getT1(), tuple.getT2());
				});
	}

	@GetMapping("/miners/{id}/avatar")
	Mono<ResponseEntity<Flux<ByteBuffer>>> getAvatar(@PathVariable String id) {
		return fetchAvatar(userRepository.findOne(id).map(User::getUserProfile)
				.map(UserProfile::getAvatarUrl));
	}

	@GetMapping("/miners/{id}/avatar/small")
	Mono<ResponseEntity<Flux<ByteBuffer>>> getSmallAvatar(@PathVariable String id) {
		return fetchAvatar(userRepository.findOne(id).map(User::getUserProfile)
				.map(UserProfile::getSmallAvatarUrl));
	}

	private Mono<ResponseEntity<Flux<ByteBuffer>>> fetchAvatar(Mono<String> map) {

		return map
				.flatMap(httpClient::get)
				.map(resp -> {

					HttpHeaders headers = new HttpHeaders();

					Optional<String> asString = Optional.ofNullable(resp
							.responseHeaders().getAsString(HttpHeaders.CONTENT_TYPE));
					headers.add(HttpHeaders.CONTENT_TYPE,
							asString.orElse(MediaType.IMAGE_JPEG_VALUE));
					return new ResponseEntity<>(resp.receive().map(ByteBuf::nioBuffer),
							headers, HttpStatus.valueOf(resp.status().code()));

				}).next();
	}
}
