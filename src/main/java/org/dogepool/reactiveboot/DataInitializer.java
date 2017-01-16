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

		List<UserStat> userStats = objectMapper.readValue(
				getClass().getResource("/user-stats.json"),
				new TypeReference<List<UserStat>>() {
				});

		mongoTemplate.insertAll(userStats);

		List<User> users = objectMapper.readValue(getClass().getResource("/users.json"),
				new TypeReference<List<User>>() {
				});

		mongoTemplate.insertAll(users);
	}
}
