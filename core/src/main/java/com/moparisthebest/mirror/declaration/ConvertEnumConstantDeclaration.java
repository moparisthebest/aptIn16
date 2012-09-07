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

import com.sun.mirror.declaration.EnumDeclaration;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.VariableElement;

public class ConvertEnumConstantDeclaration extends ConvertFieldDeclaration implements com.sun.mirror.declaration.EnumConstantDeclaration {

	protected ConvertEnumConstantDeclaration(VariableElement internalVariableElement) {
		super(internalVariableElement);
	}

	@Override
	public EnumDeclaration getDeclaringType() {
		Debug.notimplemented("EnumDeclaration"); //todo: implement ConvertEnumConstantDeclaration.getDeclaringType
		return null;
	}
}
