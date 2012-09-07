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
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.util.SourcePosition;
import com.moparisthebest.mirror.log.Debug;
import com.moparisthebest.mirror.type.ConvertTypeMirror;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import java.util.HashMap;
import java.util.Map;

public class ConvertAnnotationMirror extends Convertable<AnnotationMirror, com.sun.mirror.declaration.AnnotationMirror> implements com.sun.mirror.declaration.AnnotationMirror {

	private final javax.lang.model.element.AnnotationMirror internal;

	protected ConvertAnnotationMirror(AnnotationMirror internal) {
		this.internal = internal;
	}

	private ConvertAnnotationMirror() {
		this.internal = null;
	}

	public static Convertable<javax.lang.model.element.AnnotationMirror, com.sun.mirror.declaration.AnnotationMirror> getConvertable() {
		return new ConvertAnnotationMirror();
	}

	public static <F extends javax.lang.model.element.AnnotationMirror> com.sun.mirror.declaration.AnnotationMirror convert(F from) {
		return convert(from, ConvertAnnotationMirror.class);
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends com.sun.mirror.declaration.AnnotationMirror, F extends javax.lang.model.element.AnnotationMirror> T convert(F from, Class<T> thisClass) {
		return (T) new ConvertAnnotationMirror(from);
	}


	@Override
	public com.sun.mirror.declaration.AnnotationMirror convertToType(AnnotationMirror from) {
		return new ConvertAnnotationMirror(from);
	}

	@Override
	public AnnotationMirror unwrap() {
		return internal;
	}

	@Override
	public AnnotationType getAnnotationType() {
		return ConvertTypeMirror.convert(internal.getAnnotationType(), AnnotationType.class);
	}

	@Override
	public Map<AnnotationTypeElementDeclaration, AnnotationValue> getElementValues() {
		Debug.implemented("Map<AnnotationTypeElementDeclaration, AnnotationValue>");
		Map<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> internalValues = internal.getElementValues();
		Map<AnnotationTypeElementDeclaration, AnnotationValue> ret = new HashMap<AnnotationTypeElementDeclaration, AnnotationValue>(internalValues.size());
		for (Map.Entry<? extends ExecutableElement, ? extends javax.lang.model.element.AnnotationValue> internalEntry : internalValues.entrySet())
			ret.put(new ConvertAnnotationTypeElementDeclaration(internalEntry.getKey()), new ConvertAnnotationValue(internalEntry.getValue()));
		if (Debug.debug)
			System.err.println("elementValues: " + ret);
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
		if (o instanceof javax.lang.model.element.AnnotationMirror) return internal.equals(o);
		if (o == null || getClass() != o.getClass()) return false;

		ConvertAnnotationMirror that = (ConvertAnnotationMirror) o;

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
