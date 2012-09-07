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

import com.sun.mirror.declaration.TypeDeclaration;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class ConvertMemberDeclaration extends ConvertDeclaration implements com.sun.mirror.declaration.MemberDeclaration {

	public ConvertMemberDeclaration(Element internal) {
		super(internal);
	}

	@Override
	public TypeDeclaration getDeclaringType() {
		Debug.implemented("TypeDeclaration");
		Element enclosingElement = internalElement.getEnclosingElement();
		if (enclosingElement == null || enclosingElement.getKind() == ElementKind.PACKAGE)
			return null;
		if (Debug.debug) {
			System.err.printf("this: '%s' declaring type: '%s'\n", this.getClass(), convert(enclosingElement, TypeDeclaration.class).getClass());
			System.err.printf("this: '%s' declaring type: '%s'\n", this, convert(enclosingElement, TypeDeclaration.class)); // todo: make sure this returns correctly
		}
		//return null;
		return convert(enclosingElement, TypeDeclaration.class);
	}
}
