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
import com.moparisthebest.mirror.type.ConvertTypeMirror;
import com.sun.mirror.type.TypeMirror;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class ConvertFieldDeclaration extends ConvertMemberDeclaration implements com.sun.mirror.declaration.FieldDeclaration {

	protected final VariableElement internalVariableElement;

	protected ConvertFieldDeclaration(VariableElement internalVariableElement) {
		super(internalVariableElement);
		this.internalVariableElement = internalVariableElement;
	}

	@Override
	public TypeMirror getType() {
		return ConvertTypeMirror.convert(internalVariableElement.asType());
	}

	@Override
	public Object getConstantValue() {
		return internalVariableElement.getConstantValue();
	}

	@Override
	public String getConstantExpression() {
		Debug.notimplemented("String"); //todo: implement ConvertFieldDeclaration.getConstantExpression
		if (getConstantValue() == null)
			return null;
		// otherwise it is a compile-time constant and we should be able to derive this
		// hint: http://stackoverflow.com/questions/6373145/accessing-source-code-from-java-annotation-processor
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof ExecutableElement) return internalVariableElement.equals(o);
		if (o == null || !(o instanceof ConvertFieldDeclaration)) return false;

		ConvertFieldDeclaration that = (ConvertFieldDeclaration) o;

		return internalVariableElement.equals(that.internalVariableElement);
	}

	@Override
	public int hashCode() {
		return internalVariableElement.hashCode();
	}

	@Override
	public String toString() {
		return internalVariableElement.toString();
	}
}
