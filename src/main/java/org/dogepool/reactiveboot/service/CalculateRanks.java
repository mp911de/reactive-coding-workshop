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
package org.dogepool.reactiveboot.service;

import java.util.concurrent.atomic.AtomicLong;

import com.mongodb.client.result.UpdateResult;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.dogepool.reactiveboot.domain.UserStat;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author Mark Paluch
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CalculateRanks {

	ReactiveMongoTemplate mongoTemplate;

	public Mono<Long> recalculateRanks() {
		return Flux.merge(updateRankByHash(), updateRankByCoins()).count();
	}

	private Flux<UpdateResult> updateRankByCoins() {

		Query query = new Query().with(new Sort(Direction.DESC, "totalCoinsMined"));
		AtomicLong atomicLong = new AtomicLong();

		return mongoTemplate
				.find(query, UserStat.class)
				.publishOn(Schedulers.single())
				.flatMap(
						userStat -> {

							long rank = atomicLong.incrementAndGet();

							Query userStatQuery = new Query(Criteria.where("id").is(
									userStat.getId()));

							return mongoTemplate.updateFirst(userStatQuery,
									new Update().set("rankByCoins", rank), UserStat.class);
						});
	}

	private Flux<UpdateResult> updateRankByHash() {

		Query query = new Query().with(new Sort(Direction.DESC, "hashrate"));
		AtomicLong atomicLong = new AtomicLong();

		return mongoTemplate
				.find(query, UserStat.class)
				.publishOn(Schedulers.single())
				.flatMap(
						userStat -> {

							long rank = atomicLong.incrementAndGet();

							Query userStatQuery = new Query(Criteria.where("id").is(
									userStat.getId()));

							return mongoTemplate.updateFirst(userStatQuery,
									new Update().set("rankByHash", rank), UserStat.class);
						});
	}
}
