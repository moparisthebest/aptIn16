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
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.ReferenceType;

import javax.lang.model.element.ExecutableElement;
import java.util.Collection;

public class ConvertExecutableDeclaration extends ConvertMemberDeclaration implements com.sun.mirror.declaration.ExecutableDeclaration {

	protected final ExecutableElement internalExecutableElement;

	protected ConvertExecutableDeclaration(ExecutableElement internalExecutableElement) {
		super(internalExecutableElement);
		this.internalExecutableElement = internalExecutableElement;
	}

	@Override
	public boolean isVarArgs() {
		return internalExecutableElement.isVarArgs();
	}

	@Override
	public Collection<TypeParameterDeclaration> getFormalTypeParameters() {
		Debug.implemented("Collection<TypeParameterDeclaration>");
		return convert(internalExecutableElement.getTypeParameters(), TypeParameterDeclaration.class);
	}

	@Override
	public Collection<ParameterDeclaration> getParameters() {
		Debug.implemented("Collection<ParameterDeclaration>");
		return convert(internalExecutableElement.getParameters(), ParameterDeclaration.class);
	}

	@Override
	public Collection<ReferenceType> getThrownTypes() {
		Debug.implemented("Collection<ReferenceType>");
		return ConvertTypeMirror.getConvertable().convert(internalExecutableElement.getThrownTypes(), ReferenceType.class);
	}
}
