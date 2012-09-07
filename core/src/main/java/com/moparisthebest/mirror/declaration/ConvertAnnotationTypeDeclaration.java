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

import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConvertAnnotationTypeDeclaration extends ConvertInterfaceDeclaration
		implements com.sun.mirror.declaration.AnnotationTypeDeclaration
		//	, ConvertableIface<javax.lang.model.element.TypeElement, com.sun.mirror.declaration.AnnotationTypeDeclaration>
{

	protected ConvertAnnotationTypeDeclaration(TypeElement internalTypeElement) {
		super(internalTypeElement);
	}

	@Override
	@SuppressWarnings({"unchecked"})
	public Collection<AnnotationTypeElementDeclaration> getMethods() {
		Debug.implemented("Collection<AnnotationTypeElementDeclaration>");
		Collection<? extends ConvertMethodDeclaration> superMethods = (Collection<? extends ConvertMethodDeclaration>) super.getMethods();
		List<AnnotationTypeElementDeclaration> ret = new ArrayList<AnnotationTypeElementDeclaration>(superMethods.size());
		for (ConvertMethodDeclaration item : superMethods)
			if (item instanceof AnnotationTypeElementDeclaration)
				ret.add((AnnotationTypeElementDeclaration) item);
			else
				ret.add(new ConvertAnnotationTypeElementDeclaration(item.internalExecutableElement));
		//org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction
		if (Debug.debug) {
			System.err.println("TypeElement: " + internalTypeElement);
			System.err.println("ret: " + ret);
			System.err.println("unfiltered: " + superMethods);
			//System.err.println("filtered: "+InstanceFilter.listFilter(superMethods, AnnotationTypeElementDeclaration.class));
		}
		return sort(ret);// or not to sort?
		//return InstanceFilter.listFilter(superMethods, AnnotationTypeElementDeclaration.class);
		//return (Collection<AnnotationTypeElementDeclaration>)superMethods;
	}
}
