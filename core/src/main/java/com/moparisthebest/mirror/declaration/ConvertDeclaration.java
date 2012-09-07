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

import com.moparisthebest.mirror.convert.Convertable;
import com.moparisthebest.mirror.util.ConvertSourcePosition;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.util.DeclarationVisitor;
import com.sun.mirror.util.SourcePosition;
import com.moparisthebest.mirror.log.Debug;

import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.lang.annotation.Annotation;
import java.util.*;

public class ConvertDeclaration extends Convertable<Element, Declaration> implements com.sun.mirror.declaration.Declaration {

	public static Elements elements = null;
	protected final javax.lang.model.element.Element internalElement;

	protected ConvertDeclaration(Element internalElement) {
		this.internalElement = internalElement;
	}

	private ConvertDeclaration() {
		this.internalElement = null;
	}

	public static Convertable<javax.lang.model.element.Element, com.sun.mirror.declaration.Declaration> getConvertable() {
		return new ConvertDeclaration();
	}

	public static <F extends Element> com.sun.mirror.declaration.Declaration convert(F from) {
		return convert(from, ConvertDeclaration.class);
	}

	@SuppressWarnings({"unchecked"})
	public static <T extends com.sun.mirror.declaration.Declaration, F extends Element> T convert(F from, Class<T> thisClass) {
		//Debug.implemented("kind: "+from.getKind()+" class: "+from.getClass());
		/*
		 why not instanceof ? in short, it doesn't work all the time
		 straight from the javadocs:

		To implement operations based on the class of an Element object,
		either use a visitor or use the result of the getKind() method.
		Using instanceof is not necessarily a reliable idiom for determining
		the effective class of an object in this modeling hierarchy since an
		implementation may choose to have a single object implement multiple
		Element subinterfaces.
		*/
		if (from == null)
			return null;
		switch (from.getKind()) {
			// TypeElement
			case ENUM:
				return (T) new ConvertEnumDeclaration((TypeElement) from);
			case CLASS:
				return (T) new ConvertClassDeclaration((TypeElement) from);
			case INTERFACE:
				return (T) new ConvertInterfaceDeclaration((TypeElement) from);

			// ExecutableElement
			case CONSTRUCTOR:
				return (T) new ConvertConstructorDeclaration((ExecutableElement) from);
			case METHOD:
				return (T) new ConvertMethodDeclaration((ExecutableElement) from);

			// TypeElement OR ExecutableElement
			case ANNOTATION_TYPE:
				//System.err.println("KIND: "+from.getKind());
				if (from instanceof TypeElement)
					return (T) new ConvertAnnotationTypeDeclaration((TypeElement) from);
				else if (from instanceof ExecutableElement)
					return (T) new ConvertAnnotationTypeElementDeclaration((ExecutableElement) from);

				//PackageElement
			case PACKAGE:
				return (T) new ConvertPackageDeclaration((PackageElement) from);

			// TypeParameterElement
			case TYPE_PARAMETER:
				return (T) new ConvertTypeParameterDeclaration((TypeParameterElement) from);

			// VariableElement
			case ENUM_CONSTANT:
				return (T) new ConvertEnumConstantDeclaration((VariableElement) from);
			case PARAMETER:
				return (T) new ConvertParameterDeclaration((VariableElement) from);
			case FIELD:
				return (T) new ConvertFieldDeclaration((VariableElement) from);
			default:
				System.err.println("FATAL ERROR, REACHED DEFAULT!");
				System.exit(1);
				//throw new RuntimeException("FATAL ERROR, REACHED DEFAULT!");
		}
		// shouldn't ever get here
		return (T) new ConvertDeclaration(from);

	}

	@Override
	public Declaration convertToType(Element from) {
		return convert(from);
	}

	@Override
	public Element unwrap() {
		return internalElement;
	}

	@Override
	public void accept(DeclarationVisitor v) {
		Debug.notimplemented("void"); //todo: implement ConvertDeclaration.accept
	}

	@Override
	public Collection<AnnotationMirror> getAnnotationMirrors() {
		return ConvertAnnotationMirror.getConvertable().convert(internalElement.getAnnotationMirrors());
	}

	@Override
	public Collection<com.sun.mirror.declaration.Modifier> getModifiers() {
		return Convertable.convertEnums(internalElement.getModifiers(), com.sun.mirror.declaration.Modifier.class);
	}

	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
		return internalElement.getAnnotation(annotationType);
	}

	@Override
	public String getSimpleName() {
		return internalElement.getSimpleName().toString();
	}

	@Override
	public String getDocComment() {
		return null;  // not supported
	}

	@Override
	public SourcePosition getPosition() {
		Debug.implemented("SourcePosition");
		return ConvertSourcePosition.convert(internalElement);
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof javax.lang.model.element.Element) return internalElement.equals(o);
		if (o == null || !(o instanceof ConvertDeclaration)) return false;

		ConvertDeclaration that = (ConvertDeclaration) o;

		return internalElement.equals(that.internalElement);
	}

	@Override
	public int hashCode() {
		return internalElement.hashCode();
	}

	@Override
	public String toString() {
		return internalElement.toString();
	}
}
