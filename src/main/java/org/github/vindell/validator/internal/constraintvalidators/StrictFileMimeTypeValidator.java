/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
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
package org.github.vindell.validator.internal.constraintvalidators;

import java.io.IOException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.github.vindell.validator.MimeTypeDetectorHolder;
import org.github.vindell.validator.constraints.StrictMimeTypeCheck;
import org.github.vindell.validator.utils.FilemimeUtils;
import org.overviewproject.mime_types.GetBytesException;
import org.springframework.web.multipart.MultipartFile;


public class StrictFileMimeTypeValidator implements ConstraintValidator<StrictMimeTypeCheck, MultipartFile>{
	
	private static final String ANY = "*";
	private String[] mimeTypes;
	
	@Override
	public void initialize(StrictMimeTypeCheck annotation) {
		this.mimeTypes = annotation.mimeTypes();
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		try {
			
			String detectMimeType = MimeTypeDetectorHolder.instance().getDetector().detectMimeType(value.getName(), value.getInputStream());
			for (String mimeType : mimeTypes) {
				if(ANY.equals(mimeType) || detectMimeType.startsWith(mimeType)) {
					return true;
				}
			}
			
			detectMimeType = FilemimeUtils.getFileMimeType(value.getName());
			for (String mimeType : mimeTypes) {
				if(detectMimeType.startsWith(mimeType)) {
					return true;
				}
			}
			
		} catch (GetBytesException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
