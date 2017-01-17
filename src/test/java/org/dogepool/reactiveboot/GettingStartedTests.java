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

		// TODO: Replace this line with an implementation creates a Mono from the value
		// "Hello, World"
		Mono<String> mono = Mono.empty();

		mono.subscribe(s -> System.out.println(s));
	}

	@Test
	public void monoShouldMapValueToHelloWorld() {

		Mono<String> mono = Mono.just("Hello");

		// TODO: Map the Mono value to "Hello, World" by appending ", World" to the
		// emitted value

		mono.subscribe(System.out::println);
	}

	@Test
	public void monoShouldFlatMapValueToHelloWorld() {

		Mono<String> mono = Mono.just("Hello");

		// TODO: Map the Mono value to "Hello, World" by appending ", World" to the
		// emitted value by using the flatMap operator

		mono.subscribe(System.out::println);
	}

	@Test
	public void monoShouldDeferCallable() {

		Callable<String> myValue = () -> "Hello, World";

		// TODO: Replace this line by creating a Mono from the Callable above
		Mono<String> mono = Mono.empty();

		mono.subscribe(System.out::println);
	}

	@Test
	public void monoShouldExecuteOnADifferentThread() {

		Scheduler elastic = Schedulers.elastic();

		// TODO: Replace this line by creating a Mono that publishes its value on a
		// different thread applying the Scheduler from above
		Mono<String> mono = Mono.just("Hello, World");

		mono.subscribe(System.out::println);
		// Wait, why don't I see here anything?
	}

	@Test
	public void fluxShouldCreateScalarValues() {

		// TODO: Replace this line with an implementation creates a Flux from the values
		// "Hello", "World"
		Flux<String> flux = Flux.empty();

		flux.subscribe(System.out::println);
	}

	@Test
	public void fluxShouldCreateHelloWorldFromIterable() {

		List<String> strings = Arrays.asList("Hello", "World");

		// TODO: Replace this line with an implementation creates a Flux from an Iterable
		Flux<String> flux = Flux.empty();

		flux.subscribe(System.out::println);
	}

	@Test
	public void fluxShouldConcatTwoMonos() {

		Mono<String> hello = Mono.just("Hello");
		Mono<String> world = Mono.just("World");

		// TODO: Replace this line with an implementation that concatenates two Publishers
		// into a Flux
		Flux<String> flux = Flux.empty();

		flux.subscribe(System.out::println);
	}

	@Test
	public void monoShouldEmitIndividualCharactersAsString() {

		Mono<String> mono = Mono.just("Hello, World");

		Flux<String> flux = Flux.empty();

		flux.subscribe(System.out::println);
	}
}
