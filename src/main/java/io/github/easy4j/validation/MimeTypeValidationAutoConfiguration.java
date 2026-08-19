package io.github.easy4j.validation;

import io.github.easy4j.validation.provider.FileContentCheckProvider;
import io.github.easy4j.validation.provider.FileContentCheckStrategy;
import org.apache.tika.Tika;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * <p>Auto-configuration for MIME type-based file content validation using Apache Tika.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@AutoConfiguration
@ConditionalOnClass(Tika.class)
public class MimeTypeValidationAutoConfiguration {

    /**
     * <p>Aggregates all declared file content check providers into a unified strategy.</p>
     *
     * @param providers the content check providers
     * @return a {@link FileContentCheckStrategy} aggregating all providers
     */
    @Bean
    @ConditionalOnMissingBean
    public FileContentCheckStrategy fileContentCheckStrategy(
            ObjectProvider<FileContentCheckProvider> providers) {
        List<FileContentCheckProvider> contentCheckProviders = providers.orderedStream().toList();
        return new FileContentCheckStrategy(contentCheckProviders);
    }
}
