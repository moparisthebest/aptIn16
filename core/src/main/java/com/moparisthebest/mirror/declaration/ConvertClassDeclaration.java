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

import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.ClassType;
import com.moparisthebest.mirror.log.Debug;
import com.moparisthebest.mirror.type.ConvertTypeMirror;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Collection;

public class ConvertClassDeclaration extends ConvertTypeDeclaration implements com.sun.mirror.declaration.ClassDeclaration {

	protected ConvertClassDeclaration(TypeElement internalTypeElement) {
		super(internalTypeElement);
	}

	@Override
	public ClassType getSuperclass() {
		Debug.implemented("ClassType");
		if (Debug.debug) {
			System.err.println("!!!!!!this" + this);
			System.err.println("!!!!!!!!!!" + ConvertTypeMirror.convert(internalTypeElement.getSuperclass(), ClassType.class));
		}
		//if(true) return null;
		return ConvertTypeMirror.convert(internalTypeElement.getSuperclass(), ClassType.class);
	}

	@Override
	public Collection<ConstructorDeclaration> getConstructors() {
		Debug.implemented("Collection<ConstructorDeclaration>");
		return convert(ElementFilter.constructorsIn(internalTypeElement.getEnclosedElements()), ConstructorDeclaration.class);
	}

	@Override
	@SuppressWarnings({"unchecked"})
	public Collection<MethodDeclaration> getMethods() {
		Debug.implemented("Collection<MethodDeclaration>");
		return (Collection<MethodDeclaration>) super.getMethods();
	}
}
