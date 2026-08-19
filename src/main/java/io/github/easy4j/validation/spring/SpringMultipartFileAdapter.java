package io.github.easy4j.validation.spring;

import io.github.easy4j.validation.file.UploadFile;
import io.github.easy4j.validation.file.UploadFileAdapter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Adapts a Spring {@link MultipartFile} to the common {@link UploadFile} interface.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public final class SpringMultipartFileAdapter implements UploadFileAdapter {

    /**
     * <p>Checks whether the given value is a {@link MultipartFile}.</p>
     *
     * @param value the value to check
     * @return {@code true} if the value is a {@link MultipartFile}
     */
    @Override
    public boolean supports(Object value) {
        return value instanceof MultipartFile;
    }

    /**
     * <p>Adapts the given {@link MultipartFile} to an {@link UploadFile}.</p>
     *
     * @param value the {@link MultipartFile} to adapt
     * @return the adapted {@link UploadFile}
     */
    @Override
    public UploadFile adapt(Object value) {
        return new SpringUploadFile((MultipartFile) value);
    }

    private static final class SpringUploadFile implements UploadFile {

        private final MultipartFile multipartFile;

        private SpringUploadFile(MultipartFile multipartFile) {
            this.multipartFile = multipartFile;
        }

        /** {@inheritDoc} */ @Override
        public String getName() {
            return multipartFile.getName();
        }

        /** {@inheritDoc} */ @Override
        public String getOriginalFilename() {
            return multipartFile.getOriginalFilename();
        }

        /** {@inheritDoc} */ @Override
        public String getContentType() {
            return multipartFile.getContentType();
        }

        /** {@inheritDoc} */ @Override
        public boolean isEmpty() {
            return multipartFile.isEmpty();
        }

        /** {@inheritDoc} */ @Override
        public long getSize() {
            return multipartFile.getSize();
        }

        /** {@inheritDoc} */ @Override
        public byte[] getBytes() throws IOException {
            return multipartFile.getBytes();
        }

        /** {@inheritDoc} */ @Override
        public InputStream getInputStream() throws IOException {
            return multipartFile.getInputStream();
        }
    }
}
