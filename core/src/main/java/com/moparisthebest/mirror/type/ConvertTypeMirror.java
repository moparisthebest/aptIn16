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

import com.moparisthebest.mirror.convert.Convertable;
import com.sun.mirror.util.TypeVisitor;
import com.moparisthebest.mirror.log.Debug;

import javax.annotation.processing.Messager;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class ConvertTypeMirror extends Convertable<TypeMirror, com.sun.mirror.type.TypeMirror> implements com.sun.mirror.type.TypeMirror {
	protected final javax.lang.model.type.TypeMirror internalTypeMirror;

	public static Messager messager = null;
	public static Types types = null;

	protected ConvertTypeMirror(javax.lang.model.type.TypeMirror internalTypeMirror) {
		this.internalTypeMirror = internalTypeMirror;
	}

	private ConvertTypeMirror() {
		internalTypeMirror = null;
	}

	public static Convertable<javax.lang.model.type.TypeMirror, com.sun.mirror.type.TypeMirror> getConvertable() {
		return new ConvertTypeMirror();
	}

	public static <F extends javax.lang.model.type.TypeMirror> com.sun.mirror.type.TypeMirror convert(F from) {
		return convert(from, ConvertTypeMirror.class);
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends com.sun.mirror.type.TypeMirror, F extends javax.lang.model.type.TypeMirror> T convert(F from, Class<T> thisClass) {
		//Debug.implemented("kind: "+from.getKind()+" class: "+from.getClass());
		/*
		 why not instanceof ? in short, it doesn't work all the time
		 straight from the javadocs:

		To implement operations based on the class of an TypeMirror object,
		either use a visitor or use the result of the getKind() method.
		Using instanceof is not necessarily a reliable idiom for determining
		the effective class of an object in this modeling hierarchy since an
		implementation may choose to have a single object implement multiple
		TypeMirror subinterfaces.
		*/
		if (from == null)
			return null;

		// PrimitiveType
		if (from.getKind().isPrimitive())
			return (T) new ConvertPrimitiveType((javax.lang.model.type.PrimitiveType) from);
		switch (from.getKind()) {
			// VoidType
			case VOID:
				return (T) new ConvertVoidType((javax.lang.model.type.NoType) from);

			// WildcardType
			case WILDCARD:
				return (T) new ConvertWildcardType((javax.lang.model.type.WildcardType) from);

			// ReferenceType
			// ArrayType
			case ARRAY:
				return (T) new ConvertArrayType((javax.lang.model.type.ArrayType) from);
			// TypeVariable
			case TYPEVAR:
				return (T) new ConvertTypeVariable((javax.lang.model.type.TypeVariable) from);
			// DeclaredType
			case DECLARED:
				// could be ClassType, EnumType, InterfaceType, or AnnotationType...
				javax.lang.model.type.DeclaredType dt = (javax.lang.model.type.DeclaredType) from;
				switch (dt.asElement().getKind()) {
					// ClassType
					case CLASS:
						return (T) new ConvertClassType(dt);
					// EnumType
					case ENUM:
						return (T) new ConvertEnumType(dt);
					// InterfaceType
					case INTERFACE:
						return (T) new ConvertInterfaceType(dt);
					// AnnotationType
					case ANNOTATION_TYPE:
						return (T) new ConvertAnnotationType(dt);
				}
			//case ERROR:
			//	return (T) new ConvertDeclaredType((javax.lang.model.type.ErrorType)from);
			case NONE:
			case NULL:
				return null;
		}
		// shouldn't ever get here
		messager.printMessage(Diagnostic.Kind.ERROR, "ConvertTypeMirror reached default for kind: " + from.getKind(), types.asElement(from));
		return (T) new ConvertTypeMirror(from);
	}

	@Override
	public com.sun.mirror.type.TypeMirror convertToType(TypeMirror from) {
		return convert(from);
	}

	@Override
	public TypeMirror unwrap() {
		return internalTypeMirror;
	}

	@Override
	public void accept(TypeVisitor v) {
		Debug.notimplemented("void"); //todo: implement ConvertTypeMirror.accept
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof javax.lang.model.type.TypeMirror) return internalTypeMirror.equals(o);
		if (o == null || !(o instanceof ConvertTypeMirror)) return false;

		ConvertTypeMirror that = (ConvertTypeMirror) o;

		return internalTypeMirror.equals(that.internalTypeMirror);
	}

	@Override
	public int hashCode() {
		return internalTypeMirror.hashCode();
	}

	@Override
	public String toString() {
		return internalTypeMirror.toString();
	}
}
