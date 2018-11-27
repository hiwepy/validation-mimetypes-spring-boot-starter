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

import java.io.IOException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.github.vindell.validation.constraints.StrictExtensionCheck;
import com.github.vindell.validation.utils.FiletypeUtils;


public class StrictFileExtensionValidator implements ConstraintValidator<StrictExtensionCheck, MultipartFile>{
	
	private static final String ANY = "*";
	private String[] extensions;
	
	@Override
	public void initialize(StrictExtensionCheck annotation) {
		this.extensions = annotation.extensions();
	}

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		try {
			// 根据文件头获取文件后缀类型
			String detectExtension = FiletypeUtils.getFileType(value.getBytes());
			for (String extension : extensions) {
				if(ANY.equals(extension) || extension.equalsIgnoreCase(detectExtension)) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
}
