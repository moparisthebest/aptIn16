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

import com.sun.mirror.declaration.EnumConstantDeclaration;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.TypeElement;
import java.util.Collection;

public class ConvertEnumDeclaration extends ConvertClassDeclaration implements com.sun.mirror.declaration.EnumDeclaration {

	protected ConvertEnumDeclaration(TypeElement internalTypeElement) {
		super(internalTypeElement);
	}

	@Override
	public Collection<EnumConstantDeclaration> getEnumConstants() {
		Debug.notimplemented("Collection<EnumConstantDeclaration>"); //todo: implement ConvertEnumDeclaration.getEnumConstants
		return null;
	}
}
