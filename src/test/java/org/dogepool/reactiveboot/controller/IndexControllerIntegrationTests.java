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

import java.util.List;
import java.util.stream.Collectors;

import org.dogepool.reactiveboot.DataInitializer;
import org.dogepool.reactiveboot.config.DogeIntegrationTestConfiguration;
import org.dogepool.reactiveboot.domain.UserStat;
import org.dogepool.reactiveboot.domain.UserStatRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link IndexController}.
 *
 * @author Mark Paluch
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = IndexControllerIntegrationTests.Config.class)
@TestPropertySource(properties = { "dogecoin.pool-name=Integration test",
		"spring.mongo.port=0" })
public class IndexControllerIntegrationTests {

	/**
	 * Configuration class. The extensive {@code @Import} declares our test slice as
	 * there's no reactive test support in Spring Boot yet.
	 */
	@Import({ DogeIntegrationTestConfiguration.class, IndexController.class,
			DataInitializer.class })
	@Configuration
	@EnableReactiveMongoRepositories(basePackageClasses = UserStatRepository.class)
	static class Config {
	}

	@Autowired
	IndexController indexController;

	@Test
	public void indexModelShouldResolveView() {

		String view = indexController.getIndex(new ExtendedModelMap());

		assertThat(view).isEqualTo("index");
	}

	@Test
	public void indexModelShouldProvideModelAttributes() {

		ExtendedModelMap model = new ExtendedModelMap();

		indexController.getIndex(model);

		assertThat(model).containsKeys("poolName", "hashLadder", "coinsLadder");

		List<Object> publishers = model.asMap().values().stream()
				.filter(Publisher.class::isInstance).collect(Collectors.toList());
		publishers.forEach(o -> {

			if (o instanceof Mono) {
				((Mono) o).block();
			}

			if (o instanceof Flux) {
				((Flux) o).blockLast();
			}
		});

		assertThat((Number) model.get("gigaHashrate")) //
				.isNotNull() //
				.matches(number -> number.longValue() > 0);

		assertThat((Number) model.get("miningUserCount")) //
				.isNotNull() //
				.matches(number -> number.longValue() > 0);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void indexModelHashLadderShouldResolveToUsers() {

		ExtendedModelMap model = new ExtendedModelMap();
		indexController.getIndex(model);

		Flux<UserStat> flux = (Flux<UserStat>) model.get("hashLadder");

		StepVerifier.create(flux) //
				.expectNextCount(10) //
				.verifyComplete();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void indexModelCoinsLadderShouldResolveToUsers() {

		ExtendedModelMap model = new ExtendedModelMap();
		indexController.getIndex(model);

		Flux<UserStat> flux = (Flux<UserStat>) model.get("coinsLadder");

		StepVerifier.create(flux) //
				.expectNextCount(10) //
				.verifyComplete();
	}
}
