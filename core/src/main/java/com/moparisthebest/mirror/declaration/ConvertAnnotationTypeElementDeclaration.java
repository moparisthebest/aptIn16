/*
 * aptIn16 - Apt implementation with Java 6 annotation processors.
 * Copyright (C) 2012 Travis Burtrum (moparisthebest)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published y
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.moparisthebest.mirror.declaration;

import com.moparisthebest.mirror.log.Debug;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;

import javax.lang.model.element.ExecutableElement;

public class ConvertAnnotationTypeElementDeclaration extends ConvertMethodDeclaration implements com.sun.mirror.declaration.AnnotationTypeElementDeclaration {

	protected ConvertAnnotationTypeElementDeclaration(ExecutableElement internalExecutableElement) {
		super(internalExecutableElement);
	}

	@Override
	public AnnotationValue getDefaultValue() {
		Debug.implemented("AnnotationValue");
		return new ConvertAnnotationValue(internalExecutableElement.getDefaultValue());
	}

	@Override
	public AnnotationTypeDeclaration getDeclaringType() {
		Debug.implemented("AnnotationTypeDeclaration");
		ConvertTypeDeclaration ret = (ConvertTypeDeclaration) super.getDeclaringType();
		if (ret instanceof AnnotationTypeDeclaration)
			return (AnnotationTypeDeclaration) ret;
		return new ConvertAnnotationTypeDeclaration(ret.internalTypeElement);// todo: no idea why I have to do this, returns ClassTypeDeclaration
	}
}