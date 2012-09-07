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

package com.moparisthebest.mirror.util;

import com.moparisthebest.mirror.type.ConvertTypeMirror;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.*;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.TypeVariable;
import com.sun.mirror.type.WildcardType;
import com.moparisthebest.mirror.convert.Convertable;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.type.*;
import java.util.Collection;

public class ConvertTypes implements com.sun.mirror.util.Types {
	private final javax.lang.model.util.Types internal;

	public ConvertTypes(javax.lang.model.util.Types internal) {
		this.internal = internal;
	}

	private javax.lang.model.type.TypeMirror unwrapMe(TypeMirror convertTypeMirror) {
		return Convertable.unwrapClass(convertTypeMirror, javax.lang.model.type.TypeMirror.class);
	}

	@Override
	public boolean isSubtype(TypeMirror t1, TypeMirror t2) {
		return internal.isSubtype(unwrapMe(t1), unwrapMe(t2));
	}

	@Override
	public boolean isAssignable(TypeMirror t1, TypeMirror t2) {
		return internal.isAssignable(unwrapMe(t1), unwrapMe(t2));
	}

	@Override
	public TypeMirror getErasure(TypeMirror t) {
		return ConvertTypeMirror.convert(internal.erasure(unwrapMe(t)));
	}

	@Override
	public PrimitiveType getPrimitiveType(PrimitiveType.Kind kind) {
		return ConvertTypeMirror.convert(internal.getPrimitiveType(Convertable.convertEnum(kind, TypeKind.class)), PrimitiveType.class);
	}

	@Override
	public VoidType getVoidType() {
		return ConvertTypeMirror.convert(internal.getNoType(TypeKind.VOID), VoidType.class);
	}

	@Override
	public ArrayType getArrayType(TypeMirror componentType) {
		return ConvertTypeMirror.convert(internal.getArrayType(unwrapMe(componentType)), ArrayType.class);
	}

	@Override
	public DeclaredType getDeclaredType(TypeDeclaration decl, TypeMirror... typeArgs) {
		return getDeclaredType(null, decl, typeArgs);
	}

	@Override
	public DeclaredType getDeclaredType(DeclaredType containing, TypeDeclaration decl, TypeMirror... typeArgs) {
		return ConvertTypeMirror.convert(
				internal.getDeclaredType(
						Convertable.unwrapClass(containing, javax.lang.model.type.DeclaredType.class),
						Convertable.unwrapClass(decl, javax.lang.model.element.TypeElement.class),
						Convertable.unwrapClass(typeArgs, new javax.lang.model.type.TypeMirror[typeArgs.length]
						)), DeclaredType.class);
	}

	@Override
	public TypeVariable getTypeVariable(TypeParameterDeclaration tparam) {
		Debug.implemented("TypeVariable");
		return ConvertTypeMirror.convert(
				Convertable.unwrapClass(tparam, javax.lang.model.element.TypeParameterElement.class).asType(),
				TypeVariable.class
		);
	}

	@Override
	public WildcardType getWildcardType(Collection<ReferenceType> upperBounds, Collection<ReferenceType> lowerBounds) {
		Debug.notimplemented("WildcardType"); //todo: implement ConvertTypes.getWildcardType
		return null;
	}
}
