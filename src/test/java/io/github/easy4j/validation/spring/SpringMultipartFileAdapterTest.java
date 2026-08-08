/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.easy4j.validation.spring;

import io.github.easy4j.validation.file.UploadFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link SpringMultipartFileAdapter}.
 *
 * <p>Verifies the {@code supports} guard, the {@code adapt} conversion and every
 * delegate method exposed by the resulting {@link UploadFile}, including the
 * IO-throwing accessors and the empty-file path.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("SpringMultipartFileAdapter Tests")
class SpringMultipartFileAdapterTest {

    private static final String CONTENT = "%PDF-1.7\n%%EOF";

    private SpringMultipartFileAdapter adapter;
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        adapter = new SpringMultipartFileAdapter();
        multipartFile = new MockMultipartFile(
                "file", "report.pdf", "application/pdf",
                CONTENT.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    @DisplayName("supports returns true for a MultipartFile")
    void testSupportsMultipartFile() {
        assertThat(adapter.supports(multipartFile)).isTrue();
    }

    @Test
    @DisplayName("supports returns false for a non-MultipartFile value")
    void testSupportsNonMultipartFile() {
        assertThat(adapter.supports("not a multipart")).isFalse();
        assertThat(adapter.supports(null)).isFalse();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    @DisplayName("adapt returns an UploadFile delegating to the underlying MultipartFile")
    void testAdaptDelegates() {
        UploadFile uploadFile = adapter.adapt(multipartFile);
        assertThat(uploadFile).isNotNull();
        assertThat(uploadFile.getName()).isEqualTo("file");
        assertThat(uploadFile.getOriginalFilename()).isEqualTo("report.pdf");
        assertThat(uploadFile.getContentType()).isEqualTo("application/pdf");
        assertThat(uploadFile.isEmpty()).isFalse();
        assertThat(uploadFile.getSize()).isEqualTo(CONTENT.length());
        assertThat(uploadFile.toString()).isNotNull();
    }

    @Test
    @DisplayName("getBytes returns the underlying file content")
    void testGetBytes() throws IOException {
        UploadFile uploadFile = adapter.adapt(multipartFile);
        assertThat(uploadFile.getBytes()).isEqualTo(CONTENT.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    @DisplayName("getInputStream returns a non-null stream that can be read")
    void testGetInputStream() throws IOException {
        UploadFile uploadFile = adapter.adapt(multipartFile);
        byte[] buffer = uploadFile.getInputStream().readAllBytes();
        assertThat(buffer).isEqualTo(CONTENT.getBytes(StandardCharsets.US_ASCII));
    }

    @Test
    @DisplayName("isEmpty returns true for an empty multipart file")
    void testIsEmpty() {
        MultipartFile empty = new MockMultipartFile("file", "empty.pdf", "application/pdf", new byte[0]);
        UploadFile uploadFile = adapter.adapt(empty);
        assertThat(uploadFile.isEmpty()).isTrue();
        assertThat(uploadFile.getSize()).isZero();
    }

    @Test
    @DisplayName("getBytes propagates IOException from the underlying multipart file")
    void testGetBytesPropagatesIOException() throws IOException {
        MultipartFile failing = new MockMultipartFile("file", "x.pdf", "application/pdf", new byte[0]) {
            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException("boom");
            }
        };
        UploadFile uploadFile = adapter.adapt(failing);
        assertThatThrownBy(uploadFile::getBytes)
                .isInstanceOf(IOException.class)
                .hasMessage("boom");
    }

    @Test
    @DisplayName("getInputStream propagates IOException from the underlying multipart file")
    void testGetInputStreamPropagatesIOException() throws IOException {
        MultipartFile failing = new MockMultipartFile("file", "x.pdf", "application/pdf", new byte[0]) {
            @Override
            public java.io.InputStream getInputStream() throws IOException {
                throw new IOException("stream boom");
            }
        };
        UploadFile uploadFile = adapter.adapt(failing);
        assertThatThrownBy(uploadFile::getInputStream)
                .isInstanceOf(IOException.class)
                .hasMessage("stream boom");
    }
}
