package endorsed.org.springframework.boot.autoconfigure.data.mongo;

import java.util.Collections;

import com.mongodb.reactivestreams.client.MongoClient;
import endorsed.org.springframework.boot.autoconfigure.mongo.ReactiveMongoAutoConfiguration;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Spring Data's reactive mongo
 * support.
 * <p>
 * Registers a {@link ReactiveMongoTemplate} bean if no other bean of the same type is
 * configured.
 * <P>
 * Honors the {@literal spring.data.mongodb.database} property if set, otherwise connects
 * to the {@literal test} database.
 *
 * @author Mark Paluch
 */
@Configuration
@ConditionalOnClass({ MongoClient.class, ReactiveMongoTemplate.class })
@EnableConfigurationProperties(MongoProperties.class)
@AutoConfigureAfter(ReactiveMongoAutoConfiguration.class)
public class ReactiveMongoDataAutoConfiguration {

	private final ApplicationContext applicationContext;

	private final MongoProperties properties;

	public ReactiveMongoDataAutoConfiguration(ApplicationContext applicationContext,
			MongoProperties properties) {
		this.applicationContext = applicationContext;
		this.properties = properties;
	}

	@Bean
	@ConditionalOnMissingBean(ReactiveMongoDatabaseFactory.class)
	public SimpleReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(
			MongoClient mongo) throws Exception {
		String database = this.properties.getMongoClientDatabase();
		return new SimpleReactiveMongoDatabaseFactory(mongo, database);
	}

	@Bean
	@ConditionalOnMissingBean
	public ReactiveMongoTemplate reactiveMongoTemplate(
			ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory,
			MongoConverter converter) {
		return new ReactiveMongoTemplate(reactiveMongoDatabaseFactory, converter);
	}

	@Bean
	@ConditionalOnMissingBean(MongoConverter.class)
	public MappingMongoConverter mappingMongoConverter(
			ReactiveMongoDatabaseFactory factory, MongoMappingContext context,
			BeanFactory beanFactory, CustomConversions conversions) {
		MappingMongoConverter mappingConverter = new MappingMongoConverter(
				ReactiveMongoTemplate.NO_OP_REF_RESOLVER, context);
		mappingConverter.setCustomConversions(conversions);
		return mappingConverter;
	}

	@Bean
	@ConditionalOnMissingBean
	public MongoMappingContext mongoMappingContext(BeanFactory beanFactory,
			CustomConversions conversions) throws ClassNotFoundException {
		MongoMappingContext context = new MongoMappingContext();
		context.setInitialEntitySet(new EntityScanner(this.applicationContext).scan(
				Document.class, Persistent.class));
		Class<?> strategyClass = this.properties.getFieldNamingStrategy();
		if (strategyClass != null) {
			context.setFieldNamingStrategy((FieldNamingStrategy) BeanUtils
					.instantiateClass(strategyClass));
		}
		context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
		return context;
	}

	@Bean
	@ConditionalOnMissingBean
	public CustomConversions customConversions() {
		return new CustomConversions(Collections.emptyList());
	}

}
