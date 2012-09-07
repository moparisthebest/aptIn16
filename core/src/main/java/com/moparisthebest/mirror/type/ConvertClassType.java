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

package com.moparisthebest.mirror.type;

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.type.ClassType;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class ConvertClassType extends ConvertDeclaredType implements com.sun.mirror.type.ClassType {

	protected ConvertClassType(DeclaredType internal) {
		super(internal);
	}

	@Override
	public ClassDeclaration getDeclaration() {
		Debug.implemented("ClassDeclaration");
		return (ClassDeclaration) super.getDeclaration();
	}

	@Override
	public ClassType getSuperclass() {
		Debug.implemented("ClassType");
		List<? extends TypeMirror> superTypes = types.directSupertypes(this.internalDeclaredType);
		if (Debug.debug)
			System.out.println("superTypes: " + superTypes);
		if (superTypes.isEmpty() || superTypes.get(0).getKind() != TypeKind.DECLARED)
			return null;
		return new ConvertClassType((DeclaredType) superTypes.get(0));
	}
}
