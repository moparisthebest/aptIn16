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

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import com.moparisthebest.mirror.declaration.ConvertDeclaration;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.type.DeclaredType;
import java.util.Collection;

public class ConvertDeclaredType extends ConvertReferenceType implements com.sun.mirror.type.DeclaredType {

	protected final javax.lang.model.type.DeclaredType internalDeclaredType;

	protected ConvertDeclaredType(DeclaredType internalDeclaredType) {
		super(internalDeclaredType);
		this.internalDeclaredType = internalDeclaredType;
	}

	@Override
	public TypeDeclaration getDeclaration() {
		Debug.implemented("TypeDeclaration");
		return ConvertDeclaration.convert(internalDeclaredType.asElement(), TypeDeclaration.class);
	}

	@Override
	public com.sun.mirror.type.DeclaredType getContainingType() {
		Debug.notimplemented("DeclaredType"); //todo: implement ConvertDeclaredType.getContainingType
		return null;
	}

	@Override
	public Collection<TypeMirror> getActualTypeArguments() {
		Debug.implemented("Collection<TypeMirror>");
		return convert(internalDeclaredType.getTypeArguments(), TypeMirror.class);
	}

	@Override
	public Collection<InterfaceType> getSuperinterfaces() {
		Debug.notimplemented("Collection<InterfaceType>"); //todo: implement ConvertDeclaredType.getSuperinterfaces
		return null;
	}
}
