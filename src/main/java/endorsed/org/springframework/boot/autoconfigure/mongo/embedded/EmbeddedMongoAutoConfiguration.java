package endorsed.org.springframework.boot.autoconfigure.mongo.embedded;

import com.mongodb.Mongo;
import com.mongodb.reactivestreams.client.MongoClient;
import de.flapdoodle.embed.mongo.MongodStarter;
import endorsed.org.springframework.boot.autoconfigure.data.mongo.ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor;
import endorsed.org.springframework.boot.autoconfigure.mongo.ReactiveMongoAutoConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;

/**
 * @author Mark Paluch
 */
@Configuration
@EnableConfigurationProperties({ MongoProperties.class, EmbeddedMongoProperties.class })
@AutoConfigureBefore({ MongoAutoConfiguration.class, ReactiveMongoAutoConfiguration.class })
@ConditionalOnClass({ Mongo.class, MongoClient.class, MongodStarter.class })
public class EmbeddedMongoAutoConfiguration {

	/**
	 * Additional configuration to ensure that {@link MongoClient} beans depend on the
	 * {@code embeddedMongoServer} bean.
	 */
	@Configuration
	@ConditionalOnClass({ MongoClient.class, ReactiveMongoClientFactoryBean.class })
	protected static class EmbeddedMongoDependencyConfiguration extends
			ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor {

		public EmbeddedMongoDependencyConfiguration() {
			super("embeddedMongoServer");
		}

	}
}
