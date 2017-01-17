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
package org.dogepool.reactiveboot.config;

import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.ReactiveMongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.ReactiveMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration class. The extensive {@code @Import} declares our test slice as there's
 * no reactive test support in Spring Boot yet.
 *
 * @author Mark Paluch
 */
@Import({ EmbeddedMongoAutoConfiguration.class, MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class, EmbeddedMongoAutoConfiguration.class,
		ReactiveMongoAutoConfiguration.class, ReactiveMongoDataAutoConfiguration.class })
@Configuration
public class MongoTestConfiguration {
}
