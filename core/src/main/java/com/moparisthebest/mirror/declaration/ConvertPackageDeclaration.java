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
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConvertPackageDeclaration extends ConvertDeclaration implements com.sun.mirror.declaration.PackageDeclaration {

	protected final PackageElement internalPackageElement;

	protected ConvertPackageDeclaration(PackageElement internalPackageElement) {
		super(internalPackageElement);
		this.internalPackageElement = internalPackageElement;
	}

	@Override
	public String getQualifiedName() {
		return internalPackageElement.getQualifiedName().toString();
	}

	private <T extends TypeDeclaration> Collection<T> getTypeDeclarations(Class<T> type, ElementKind kind) {
		// ElementFilter.typesIn(internalPackageElement.getEnclosedElements())
		// only filters on types in general, we need finer granularity
		List<T> ret = new ArrayList<T>();
		for (Element e : internalPackageElement.getEnclosedElements())
			if (e.getKind() == kind)
				ret.add(convert(e, type));
		return ret;
	}

	@Override
	public Collection<ClassDeclaration> getClasses() {
		Debug.implemented("Collection<ClassDeclaration>");
		return getTypeDeclarations(ClassDeclaration.class, ElementKind.CLASS);
	}

	@Override
	public Collection<EnumDeclaration> getEnums() {
		Debug.implemented("Collection<EnumDeclaration>");
		return getTypeDeclarations(EnumDeclaration.class, ElementKind.ENUM);
	}

	@Override
	public Collection<InterfaceDeclaration> getInterfaces() {
		Debug.implemented("Collection<InterfaceDeclaration>");
		return getTypeDeclarations(InterfaceDeclaration.class, ElementKind.INTERFACE);
	}

	@Override
	public Collection<AnnotationTypeDeclaration> getAnnotationTypes() {
		Debug.implemented("Collection<AnnotationTypeDeclaration>");
		return getTypeDeclarations(AnnotationTypeDeclaration.class, ElementKind.ANNOTATION_TYPE);
	}
}
