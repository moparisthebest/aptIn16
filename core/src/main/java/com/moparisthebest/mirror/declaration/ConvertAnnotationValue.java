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

import com.moparisthebest.mirror.convert.Convertable;
import com.moparisthebest.mirror.util.ConvertSourcePosition;
import com.sun.mirror.util.SourcePosition;
import com.moparisthebest.mirror.log.Debug;
import com.moparisthebest.mirror.type.ConvertTypeMirror;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class ConvertAnnotationValue extends Convertable<AnnotationValue, com.sun.mirror.declaration.AnnotationValue> implements com.sun.mirror.declaration.AnnotationValue {
	private final javax.lang.model.element.AnnotationValue internal;

	protected ConvertAnnotationValue(AnnotationValue internal) {
		this.internal = internal;
	}

	private ConvertAnnotationValue() {
		this.internal = null;
	}

	public static Convertable<javax.lang.model.element.AnnotationValue, com.sun.mirror.declaration.AnnotationValue> getConvertable() {
		return new ConvertAnnotationValue();
	}

	public static <F extends javax.lang.model.element.AnnotationValue> com.sun.mirror.declaration.AnnotationValue convert(F from) {
		return convert(from, ConvertAnnotationValue.class);
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends com.sun.mirror.declaration.AnnotationValue, F extends javax.lang.model.element.AnnotationValue> T convert(F from, Class<T> thisClass) {
		return (T) new ConvertAnnotationValue(from);
	}

	@Override
	public com.sun.mirror.declaration.AnnotationValue convertToType(AnnotationValue from) {
		return convert(from);
	}

	@Override
	public AnnotationValue unwrap() {
		return internal;
	}

	/**
	 * Returns the value.
	 * The result has one of the following types:
	 * <ul><li> a wrapper class (such as {@link Integer}) for a primitive type
	 * <li> {@code String}
	 * <li> {@code TypeMirror}
	 * <li> {@code EnumConstantDeclaration}
	 * <li> {@code AnnotationMirror}
	 * <li> {@code Collection<AnnotationValue>}
	 * (representing the elements, in order, if the value is an array)
	 * <p/>
	 * internal.getValue returns:
	 * * <ul><li> a wrapper class (such as {@link Integer}) for a primitive type
	 * <li> {@code String}
	 * <li> {@code TypeMirror}
	 * <li> {@code VariableElement} (representing an enum constant)
	 * <li> {@code AnnotationMirror}
	 * <li> {@code List<? extends AnnotationValue>}
	 * (representing the elements, in declared order, if the value is an array)
	 * </ul>
	 *
	 * @return the value
	 */
	@Override
	@SuppressWarnings({"unchecked"})
	public Object getValue() {
		Debug.implemented("Object");
		Object ret = internal.getValue();
		if (ret instanceof VariableElement && ((VariableElement) ret).getKind() == ElementKind.ENUM_CONSTANT)
			ret = ConvertDeclaration.convert((VariableElement) ret);
		else if (ret instanceof javax.lang.model.element.AnnotationMirror)
			ret = ConvertAnnotationMirror.convert((javax.lang.model.element.AnnotationMirror) ret);
		else if (ret instanceof javax.lang.model.type.TypeMirror)
			ret = ConvertTypeMirror.convert((javax.lang.model.type.TypeMirror) ret);
		else if (ret instanceof List)
			ret = convert((List<? extends AnnotationValue>) ret);
		// else, we are hoping it's a String or wrapper for a primitive type, just return it
		if (Debug.debug)
			System.err.println("!!!!!!!!!!!!ret: " + ret);
		return ret;
	}

	@Override
	public SourcePosition getPosition() {
		Debug.implemented("SourcePosition");
		return ConvertSourcePosition.convert(internal);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof javax.lang.model.element.AnnotationValue) return internal.equals(o);
		if (o == null || getClass() != o.getClass()) return false;

		ConvertAnnotationValue that = (ConvertAnnotationValue) o;

		return internal.equals(that.internal);
	}

	@Override
	public int hashCode() {
		return internal.hashCode();
	}

	@Override
	public String toString() {
		return internal.toString();
	}
}
