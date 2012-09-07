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

import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.moparisthebest.mirror.log.Debug;

public class ConvertTypeVariable extends ConvertReferenceType implements com.sun.mirror.type.TypeVariable {

	protected final javax.lang.model.type.TypeVariable internalArrayType;

	protected ConvertTypeVariable(javax.lang.model.type.TypeVariable internalArrayType) {
		super(internalArrayType);
		this.internalArrayType = internalArrayType;
	}

	@Override
	public TypeParameterDeclaration getDeclaration() {
		Debug.notimplemented("TypeParameterDeclaration"); //todo: implement ConvertTypeVariable.getDeclaration
		//return new ConvertTypeParameterDeclaration(internalArrayType.asElement());
		return null;
	}
}
