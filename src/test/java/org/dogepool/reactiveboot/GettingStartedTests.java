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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * Tests to get comfortable with {@link reactor.core.publisher.Mono} and
 * {@link reactor.core.publisher.Flux}.
 *
 * @author Mark Paluch
 */
public class GettingStartedTests {

	@Test
	public void monoShouldCreateScalarValue() {

		Mono<String> mono = Mono.just("Hello, World");

		mono.subscribe(s -> System.out.println(s));
	}

	@Test
	public void monoShouldMapValueToHelloWorld() {

		Mono<String> mono = Mono.just("Hello");

		mono = mono.map(s -> String.format("%s, World", s));

		mono.subscribe(System.out::println);
	}

	@Test
	public void monoShouldFlatMapValueToHelloWorld() {

		Mono<String> mono = Mono.just("Hello");

		mono = mono.then(s -> Mono.just(String.format("%s, World", s)));

		mono.subscribe(System.out::println);
	}

	@Test
	public void monoShouldDeferCallable() {

		Callable<String> myValue = () -> "Hello, World";

		Mono<String> mono = Mono.fromCallable(myValue);

		mono.subscribe(System.out::println);
	}

	@Test
	public void monoShouldExecuteOnADifferentThread() {

		Scheduler elastic = Schedulers.elastic();

		Mono<String> mono = Mono.just("Hello, World");

		mono = mono.subscribeOn(elastic).publishOn(elastic);

		mono.doOnNext(s -> System.out.println(s)).subscribe().block();
	}

	@Test
	public void fluxShouldCreateScalarValues() {

		Flux<String> flux = Flux.just("Hello", "World");

		flux.subscribe(System.out::println);
	}

	@Test
	public void fluxShouldCreateHelloWorldFromIterable() {

		List<String> strings = Arrays.asList("Hello", "World");

		Flux<String> flux = Flux.fromIterable(strings);

		flux.subscribe(System.out::println);
	}

	@Test
	public void fluxShouldConcatTwoMonos() {

		Mono<String> hello = Mono.just("Hello");
		Mono<String> world = Mono.just("World");

		Flux<String> flux = Flux.concat(hello, world);

		flux.subscribe(System.out::println);
	}

	@Test
	public void monoShouldEmitIndividualCharactersAsString() {

		Mono<String> mono = Mono.just("Hello, World");

		Flux<String> flux = mono.map(s -> s.split("")).flatMap(Flux::fromArray);

		flux.subscribe(System.out::println);
	}
}
