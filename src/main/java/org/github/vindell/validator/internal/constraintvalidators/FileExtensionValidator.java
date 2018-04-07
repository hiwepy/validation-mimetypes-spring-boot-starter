/*
 * Copyright (c) 2017, vindell (https://github.com/vindell).
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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.github.vindell.validator.constraints.ExtensionCheck;
import org.springframework.web.multipart.MultipartFile;


public class FileExtensionValidator implements ConstraintValidator<ExtensionCheck, MultipartFile>{
	
	private static final String ANY = "*";
	private String[] extensions;
	
	@Override
	public void initialize(ExtensionCheck annotation) {
		this.extensions = annotation.extensions();
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		String detectExtension = FilenameUtils.getExtension(value.getName());
		
		for (String extension : extensions) {
			if(ANY.equals(extension) || extension.equalsIgnoreCase(detectExtension)) {
				return true;
			}
		}
		
		return false;
	}
	
}
