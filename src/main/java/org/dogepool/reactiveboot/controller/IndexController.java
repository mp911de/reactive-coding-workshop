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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.dogepool.reactiveboot.domain.UserStat;
import org.dogepool.reactiveboot.domain.UserStatRepository;
import org.dogepool.reactiveboot.view.model.IndexModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mark Paluch
 */
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class IndexController {

	UserStatRepository userStatRepository;

	@GetMapping(path = "/")
	Mono<IndexModel> indexModel() {

		Flux<UserStat> top10ByOrderByHashrate = userStatRepository
				.findTop10ByOrderByHashrateDesc();
		Flux<UserStat> top10ByOrderByTotalCoinsMined = userStatRepository
				.findTop10ByOrderByTotalCoinsMinedDesc();

		IndexModel indexModel = new IndexModel();

		Mono<List<UserStat>> list1Mono = top10ByOrderByHashrate.collectList()
				.doOnSuccess(indexModel::setHashLadder);
		Mono<List<UserStat>> list2Mono = top10ByOrderByTotalCoinsMined.collectList()
				.doOnSuccess(indexModel::setCoinsLadder);

		AtomicLong hashrate = new AtomicLong();
		AtomicInteger users = new AtomicInteger();

		Flux<UserStat> userStatFlux = userStatRepository
				.findAll()
				.doOnNext(userStat -> hashrate.addAndGet((long) userStat.getHashrate()))
				.doOnNext(userStat -> users.incrementAndGet())
				.doOnComplete(
						() -> {
							indexModel.setGigaHashrate(hashrate.doubleValue()
									/ (1000d * 1000d * 1000d));
							indexModel.setMiningUserCount(users.get());
						});

		return Flux.merge(list1Mono, list2Mono, userStatFlux).then()
				.then(Mono.just(indexModel));

	}
}
