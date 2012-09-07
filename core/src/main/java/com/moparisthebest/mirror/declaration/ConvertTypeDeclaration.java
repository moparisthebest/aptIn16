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
import com.sun.mirror.type.InterfaceType;
import com.moparisthebest.mirror.log.Debug;
import com.moparisthebest.mirror.type.ConvertTypeMirror;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import java.util.Collection;

public class ConvertTypeDeclaration extends ConvertMemberDeclaration implements com.sun.mirror.declaration.TypeDeclaration {

	protected final TypeElement internalTypeElement;

	protected ConvertTypeDeclaration(TypeElement internalTypeElement) {
		super(internalTypeElement);
		this.internalTypeElement = internalTypeElement;
	}

	@Override
	public String getQualifiedName() {
		return internalTypeElement.getQualifiedName().toString();
	}

	@Override
	public PackageDeclaration getPackage() {
		Debug.implemented("PackageDeclaration");
		return new ConvertPackageDeclaration(elements.getPackageOf(internalTypeElement));
	}

	@Override
	public Collection<TypeParameterDeclaration> getFormalTypeParameters() {
		Debug.implemented("Collection<TypeParameterDeclaration>");
		return convert(internalTypeElement.getTypeParameters(), TypeParameterDeclaration.class);
	}

	@Override
	public Collection<InterfaceType> getSuperinterfaces() {
		Debug.implemented("Collection<InterfaceType>");
		return ConvertTypeMirror.getConvertable().convert(internalTypeElement.getInterfaces(), InterfaceType.class);
	}

	@Override
	public Collection<FieldDeclaration> getFields() {
		Debug.implemented("Collection<FieldDeclaration>");
		return sort(convert(ElementFilter.fieldsIn(internalTypeElement.getEnclosedElements()), FieldDeclaration.class));
	}

	@Override
	public Collection<? extends MethodDeclaration> getMethods() {
		Debug.implemented("Collection<? extends MethodDeclaration>");
		//return convert(ElementFilter.methodsIn(internalTypeElement.getEnclosedElements()), MethodDeclaration.class);
		//return convert(com.moparisthebest.mirror.convert.ElementFilter.listFilter(internalTypeElement.getEnclosedElements(), Element.class, ElementKind.METHOD, ElementKind.ANNOTATION_TYPE), MethodDeclaration.class);
		return sort(convert(com.moparisthebest.mirror.convert.ElementFilter.listFilter(internalTypeElement.getEnclosedElements(), Element.class, ElementKind.METHOD), MethodDeclaration.class));
	}

	@Override
	public Collection<TypeDeclaration> getNestedTypes() {
		Debug.implemented("Collection<TypeDeclaration>");
		return convert(ElementFilter.typesIn(internalTypeElement.getEnclosedElements()), TypeDeclaration.class);
	}
}