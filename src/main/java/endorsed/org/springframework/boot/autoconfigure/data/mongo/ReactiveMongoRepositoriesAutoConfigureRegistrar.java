package endorsed.org.springframework.boot.autoconfigure.data.mongo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

/**
 * {@link ImportBeanDefinitionRegistrar} used to auto-configure Spring Data Mongo Reactive
 * Repositories.
 *
 * @author Mark Paluch
 */
class ReactiveMongoRepositoriesAutoConfigureRegistrar extends
		AbstractRepositoryConfigurationSourceSupport {

	@Override
	protected Class<? extends Annotation> getAnnotation() {
		return EnableReactiveMongoRepositories.class;
	}

	@Override
	protected Class<?> getConfiguration() {
		return EnableReactiveMongoRepositoriesConfiguration.class;
	}

	@Override
	protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
		try {
			// TODO: Make ReactiveMongoRepositoryConfigurationExtension a public type

			Class<?> extensionClass = Class
					.forName("org.springframework.data.mongodb.repository.config.ReactiveMongoRepositoryConfigurationExtension");
			Constructor<?> declaredConstructor = extensionClass.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			return (RepositoryConfigurationExtension) declaredConstructor.newInstance();

		}
		catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@EnableReactiveMongoRepositories
	private static class EnableReactiveMongoRepositoriesConfiguration {

	}

}
