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
package com.github.vindell.validation.internal.constraintvalidators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.github.vindell.validation.constraints.MimeTypeCheck;


public class FileMimeTypeValidator implements ConstraintValidator<MimeTypeCheck, MultipartFile>{
	
	private static final String ANY = "*";
	private String[] mimeTypes;
	
	@Override
	public void initialize(MimeTypeCheck annotation) {
		this.mimeTypes = annotation.mimeTypes();
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		for (String mimeType : mimeTypes) {
			if(ANY.equals(mimeType) || value.getContentType().startsWith(mimeType)) {
				return true;
			}
		}
		
		return false;
	}
	
}
