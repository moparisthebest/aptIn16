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

import com.sun.mirror.declaration.*;
import com.sun.mirror.type.ReferenceType;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.TypeParameterElement;
import java.util.Collection;

public class ConvertTypeParameterDeclaration extends ConvertDeclaration implements TypeParameterDeclaration {

	protected final TypeParameterElement internalTypeParameterElement;

	protected ConvertTypeParameterDeclaration(TypeParameterElement internalTypeParameterElement) {
		super(internalTypeParameterElement);
		this.internalTypeParameterElement = internalTypeParameterElement;
	}

	@Override
	public Declaration getOwner() {
		return new ConvertDeclaration(internalTypeParameterElement.getGenericElement());
	}

	@Override
	public Collection<ReferenceType> getBounds() {
		Debug.notimplemented("Collection<ReferenceType>"); //todo: implement ConvertTypeParameterDeclaration.getBounds
		return null;
	}
}
