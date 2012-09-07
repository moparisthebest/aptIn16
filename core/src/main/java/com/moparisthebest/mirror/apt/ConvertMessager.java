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

package com.moparisthebest.mirror.apt;

import com.sun.mirror.util.SourcePosition;
import com.moparisthebest.mirror.log.Debug;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class ConvertMessager implements com.sun.mirror.apt.Messager {

	private final javax.annotation.processing.Messager internal;

	public ConvertMessager(Messager internal) {
		this.internal = internal;
	}

	@Override
	public void printError(String msg) {
		Debug.implemented(void.class);
		internal.printMessage(Diagnostic.Kind.ERROR, msg);
	}

	@Override
	public void printWarning(String msg) {
		Debug.implemented(void.class);
		internal.printMessage(Diagnostic.Kind.WARNING, msg);
	}

	@Override
	public void printNotice(String msg) {
		Debug.implemented(void.class);
		internal.printMessage(Diagnostic.Kind.NOTE, msg);
	}

	@Override
	public void printError(SourcePosition pos, String msg) {
		Debug.implemented(void.class);
		printError(msg); // ignore pos
	}

	@Override
	public void printWarning(SourcePosition pos, String msg) {
		Debug.implemented(void.class);
		printWarning(msg); // ignore pos
	}

	@Override
	public void printNotice(SourcePosition pos, String msg) {
		Debug.implemented(void.class);
		printNotice(msg); // ignore pos
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof javax.annotation.processing.Messager) return internal.equals(o);
		if (o == null || getClass() != o.getClass()) return false;

		ConvertMessager that = (ConvertMessager) o;

		return internal.equals(that.internal);
	}

	@Override
	public int hashCode() {
		return internal.hashCode();
	}

	@Override
	public String toString() {
		return internal.toString();
	}
}
