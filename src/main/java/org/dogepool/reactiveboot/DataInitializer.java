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
package org.dogepool.reactiveboot;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.dogepool.reactiveboot.domain.User;
import org.dogepool.reactiveboot.domain.UserStat;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

/**
 * Data initializer to import user and user statistics data from {@code users.json} and
 * {@code user-stats.json}
 *
 * @author Mark Paluch
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer {

	MongoTemplate mongoTemplate;
	ObjectMapper objectMapper = new ObjectMapper();

	@PostConstruct
	private void postConstruct() throws IOException {

		mongoTemplate.remove(new Query(), UserStat.class);
		mongoTemplate.remove(new Query(), User.class);

		List<User> users = objectMapper.readValue(getClass().getResource("/users.json"),
				new TypeReference<List<User>>() {
				});

		mongoTemplate.insertAll(users);

		List<UserStat> userStats = objectMapper.readValue(
				getClass().getResource("/user-stats.json"),
				new TypeReference<List<UserStat>>() {
				});

		mongoTemplate.insertAll(userStats);
	}
}
