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

package com.moparisthebest.mirror.log;

import com.moparisthebest.mirror.util.ConvertTypes;

public class Debug {

	public static final byte debugLevel = 0;//6; // set to 0 to disable debugging
	public static final boolean debug = (debugLevel > 0);

	public static boolean implemented = false;
	public static boolean notimplemented = true;

	public static final String IMPLEMENTED = "implemented!";
	public static final String NOT_IMPLEMENTED = "NOT implemented!!!!!!!!!!!!!!!!!!!!";

	// don't touch
	private static final String thisClassName = Debug.class.getName();

	private static void printInfo(String msg, String returnType) {
		printInfo(msg, returnType, debugLevel);
	}

	public static void printInfo(String msg, String returnType, byte debugLevel) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		//todo increment until out of this class
		int x = 2; // start at 3
		while (thisClassName.equals(stackTrace[++x].getClassName())) ;
		printStackTrace(stackTrace[x], msg, returnType);
		int level = x + debugLevel;
		StringBuilder tabs = new StringBuilder();
		while (++x < level && x < stackTrace.length)
			printStackTrace(stackTrace[x], "Called", tabs.append('\t').toString());
		//printStackTrace(stackTrace[x+3], "Called", "\t\t");
		//System.exit(5);
	}

	private static void printStackTrace(StackTraceElement ste, String msg, String returnType) {
		System.err.printf("%s %s.%s(%s:%d) %s\n",
				returnType,
				ste.getClassName().replaceFirst(".*\\.", "")
				, ste.getMethodName(),
				ste.getFileName(), ste.getLineNumber(),
				msg);
	}

	public static void implemented() {
		implemented((String) null);
	}

	public static void notimplemented() {
		notimplemented((String) null);
	}

	public static void implemented(Class returnType) {
		implemented(returnType.getSimpleName());
	}

	public static void notimplemented(Class returnType) {
		notimplemented(returnType.getSimpleName());
	}

	public static void implemented(String returnType) {
		if (debug && implemented)
			printInfo(IMPLEMENTED, returnType);
	}

	public static void notimplemented(String returnType) {
		if (debug && notimplemented)
			printInfo(NOT_IMPLEMENTED, returnType);
	}

	public static void main(String[] args) {
		new ConvertTypes(null).getTypeVariable(null);
	}
}
