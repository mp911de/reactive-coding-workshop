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
package org.dogepool.reactiveboot.internal;

import java.io.File;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dogepool.reactiveboot.domain.User;
import org.dogepool.reactiveboot.domain.UserProfile;
import org.dogepool.reactiveboot.domain.UserStat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Utility to generate random user data.
 * @author Mark Paluch
 */
@Slf4j
class GenerateUsers {

	public static void main(String[] args) throws Exception {

		log.info("Creating random people data...");

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<RandomUsers> response = restTemplate.getForEntity(
				"https://randomuser.me/api/?format=json&results=10", RandomUsers.class);

		ObjectMapper objectMapper = new ObjectMapper();

		List<User> users = Mono.just(response.getBody())
				.flatMap(randomUsers -> Flux.fromIterable(randomUsers.getResults()))
				.map(randomUser -> {

					UserProfile userProfile = UserProfile.builder() //
							.avatarUrl(randomUser.getPicture().getMedium()) //
							.smallAvatarUrl(randomUser.getPicture().getThumbnail()) //
							.build();

					return User.builder().id(randomUser.getLogin().getUsername()) //
							.nickname(randomUser.getLogin().getUsername()) //
							.displayName(randomUser.getName().getDisplayName()) //
							.userProfile(userProfile) //
							.build();
				}).collectList().block();

		List<UserStat> userStats = Flux
				.fromIterable(users)
				.map(user -> {

					ThreadLocalRandom random = ThreadLocalRandom.current();
					return new UserStat(user.getId(), random.nextDouble(1, 100), random
							.nextLong(1, 1000));
				}).collectList().block();

		objectMapper.writeValue(new File("target/users.json"), users);
		objectMapper.writeValue(new File("target/user-stats.json"), userStats);

		log.info("Generated random data at target/users.json and target/user-stats.json.");
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	static class RandomUsers {

		List<RandomUser> results;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	static class RandomUser {

		Name name;
		Picture picture;
		Login login;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	static class Name {

		String first, last;

		public String getDisplayName() {
			return String.format("%s %s", StringUtils.capitalize(getFirst()),
					StringUtils.capitalize(getLast()));
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	static class Picture {

		String medium, thumbnail;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	static class Login {

		String username;
	}
}
