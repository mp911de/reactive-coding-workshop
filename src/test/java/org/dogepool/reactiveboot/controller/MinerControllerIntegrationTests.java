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

import org.dogepool.reactiveboot.DataInitializer;
import org.dogepool.reactiveboot.config.DogeIntegrationTestConfiguration;
import org.dogepool.reactiveboot.domain.UserStatRepository;
import org.dogepool.reactiveboot.view.model.MinerModel;
import org.junit.Test;
import org.junit.runner.RunWith;
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
 * Integration tests for {@link MinerController}.
 *
 * @author Mark Paluch
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MinerControllerIntegrationTests.Config.class)
@TestPropertySource(properties = { "dogecoin.pool-name=Integration test",
		"spring.mongo.port=0" })
public class MinerControllerIntegrationTests {

	/**
	 * Configuration class. The extensive {@code @Import} declares our test slice as
	 * there's no reactive test support in Spring Boot yet.
	 */
	@Import({ DogeIntegrationTestConfiguration.class, MinerController.class,
			DataInitializer.class })
	@Configuration
	@EnableReactiveMongoRepositories(basePackageClasses = UserStatRepository.class)
	static class Config {
	}

	@Autowired
	MinerController minerController;

	@Test
	public void minerControllerShouldResolveView() {

		String view = minerController.getMiner("blacktiger477", new ExtendedModelMap());

		assertThat(view).isEqualTo("miner");
	}

	@Test
	public void minerModelShouldProvideModelAttributes() {

		ExtendedModelMap model = new ExtendedModelMap();

		minerController.getMiner("blacktiger477", model);

		assertThat(model).containsKeys("model");
	}

	@Test
	@SuppressWarnings("unchecked")
	public void minerModelShouldResolveMinerModel() {

		ExtendedModelMap model = new ExtendedModelMap();
		minerController.getMiner("blacktiger477", model);

		Mono<MinerModel> mono = (Mono<MinerModel>) model.get("model");

		StepVerifier
				.create(mono)
				.assertNext(
						minerModel -> {

							assertThat(minerModel.getDisplayName()).isEqualTo(
									"Maia Green");
							assertThat(minerModel.getNickname()).isEqualTo(
									"blacktiger477");
							assertThat(minerModel.getAvatarUrl())
									.isEqualTo(
											"https://randomuser.me/api/portraits/med/women/75.jpg");
							assertThat(minerModel.getSmallAvatarUrl())
									.isEqualTo(
											"https://randomuser.me/api/portraits/thumb/women/75.jpg");
						}).verifyComplete();
	}
}
