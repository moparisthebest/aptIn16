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

import com.moparisthebest.mirror.convert.Convertable;
import com.sun.mirror.declaration.MemberDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;

public class ConvertDeclarations implements com.sun.mirror.util.Declarations {
	private final Elements internal;

	public ConvertDeclarations(Elements internal) {
		this.internal = internal;
	}

	@Override
	public boolean hides(MemberDeclaration sub, MemberDeclaration sup) {
		return internal.hides(Convertable.unwrapClass(sub, Element.class), Convertable.unwrapClass(sup, Element.class));
	}

	@Override
	public boolean overrides(MethodDeclaration sub, MethodDeclaration sup) {
		ExecutableElement overrider = Convertable.unwrapClass(sub, ExecutableElement.class);
		return internal.overrides(overrider,
				Convertable.unwrapClass(sup, ExecutableElement.class),
				internal.getTypeElement(overrider.getSimpleName())); // todo: getting type element this way is wrong
	}
}
