package io.github.easy4j.validation;

import io.github.easy4j.validation.constraints.FileNotEmpty;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.multipart.MultipartFile;
import io.github.easy4j.validation.provider.FileContentCheckStrategy;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNotEmptyIntegrationTest {

    private final Validator validator = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ParameterMessageInterpolator())
            .buildValidatorFactory()
            .getValidator();

    @Test
    void shouldValidateSpringMultipartFileThroughCommonConstraint() {
        UploadCommand valid = new UploadCommand(file("report.pdf", "%PDF-1.7\n%%EOF"));
        UploadCommand renamedExecutable = new UploadCommand(file("report.pdf", "MZ executable"));

        assertTrue(validator.validate(valid).isEmpty());
        assertEquals(1, validator.validate(renamedExecutable).size());
    }

    @Test
    void shouldAutoConfigureCommonContentCheckStrategy() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(MimeTypeValidationAutoConfiguration.class))
                .run(context -> assertTrue(context.containsBean("fileContentCheckStrategy")
                        && context.getBean(FileContentCheckStrategy.class) != null));
    }

    private MultipartFile file(String name, String content) {
        return new MockMultipartFile("file", name, "application/pdf",
                content.getBytes(StandardCharsets.US_ASCII));
    }

    private static final class UploadCommand {

        @FileNotEmpty(extensions = "pdf", mimeTypes = "application/pdf", strict = true)
        private final MultipartFile file;

        private UploadCommand(MultipartFile file) {
            this.file = file;
        }
    }
}
