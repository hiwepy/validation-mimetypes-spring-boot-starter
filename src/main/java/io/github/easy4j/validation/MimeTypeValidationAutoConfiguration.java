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
 * 文件类型安全校验自动配置。
 */
@AutoConfiguration
@ConditionalOnClass(Tika.class)
public class MimeTypeValidationAutoConfiguration {

    /**
     * 汇总应用声明的文件内容检查组件。
     *
     * @param providers 内容检查提供者
     * @return 内容检查策略
     */
    @Bean
    @ConditionalOnMissingBean
    public FileContentCheckStrategy fileContentCheckStrategy(
            ObjectProvider<FileContentCheckProvider> providers) {
        List<FileContentCheckProvider> contentCheckProviders = providers.orderedStream().toList();
        return new FileContentCheckStrategy(contentCheckProviders);
    }
}
