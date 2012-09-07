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

import com.moparisthebest.mirror.log.Debug;

import javax.tools.StandardLocation;
import java.io.*;

public class ConvertFiler implements com.sun.mirror.apt.Filer {

	private final javax.annotation.processing.Filer internal;
	private boolean modified = false;

	public ConvertFiler(javax.annotation.processing.Filer internal) {
		this.internal = internal;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	@Override
	public PrintWriter createSourceFile(String name) throws IOException {
		Debug.implemented(PrintWriter.class);
		modified = true;
		return new PrintWriter(internal.createSourceFile(name).openWriter());
	}

	@Override
	public OutputStream createClassFile(String name) throws IOException {
		Debug.implemented(OutputStream.class);
		modified = true;
		return internal.createClassFile(name).openOutputStream();
	}

	public static StandardLocation convert(Location location) {
		switch (location) {
			case CLASS_TREE:
				return StandardLocation.CLASS_OUTPUT;
			case SOURCE_TREE:
				return StandardLocation.SOURCE_OUTPUT;
			default:
				return null;  // should NEVER get here (those are the only two options for Location)
		}
	}

	@Override
	public PrintWriter createTextFile(Location loc, String pkg, File relPath, String charsetName) throws IOException {
		Debug.implemented(PrintWriter.class);
		try {
			OutputStream os = createBinaryFile(loc, pkg, relPath);
			// since charsetName can be null, we need to use a different constructor
			return new PrintWriter(charsetName == null ? new OutputStreamWriter(os) : new OutputStreamWriter(os, charsetName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public OutputStream createBinaryFile(Location loc, String pkg, File relPath) throws IOException {
		Debug.implemented(OutputStream.class);
		String relativeName = relPath.getPath();
		//relativeName = relPath.getName();
		relativeName = relativeName.replaceAll("\\\\", "/"); // on windows the relPath sometimes contains \ instead of /, which messes everything up
		//System.out.printf("loc: '%s' pkg: '%s' relPath: '%s' relativeName: '%s'\n", loc, pkg, relPath.toString(), relativeName);
		modified = true;
		try {
			return internal.createResource(convert(loc), pkg, relativeName).openOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean equals(Object o) {
		Debug.implemented();
		if (this == o) return true;
		if (o instanceof javax.annotation.processing.Filer) return internal.equals(o);
		if (o == null || getClass() != o.getClass()) return false;

		ConvertFiler that = (ConvertFiler) o;

		return internal.equals(that.internal);
	}

	@Override
	public int hashCode() {
		Debug.implemented(int.class);
		return internal.hashCode();
	}

	@Override
	public String toString() {
		Debug.implemented(String.class);
		return internal.toString();
	}
}
