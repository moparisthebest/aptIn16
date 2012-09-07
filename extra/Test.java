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

import java.io.*;

/**
 * This is one of the ways I tested this before creating netui-compiler-test, but it can still be useful to point to your
 * own real-life projects to see if it causes anything additional to break.
 */
public class Test {

	//todo: should use something like this: http://hg.netbeans.org/core-main/raw-file/default/openide.util.lookup/test/unit/src/org/openide/util/test/AnnotationProcessorTestUtils.java in a real unit test some day
	public static final String bash = "C:/cygwin/bin/bash.exe ";

	public static void main(String[] args) throws Exception {
		String javac = bash + "C:/Devel/aptIn16/javactest/compile.sh" + " " + (args.length > 0 ? args[0] : "true");
		javac = bash + "C:/Devel/aptIn16/maven.sh";

		Process proc = Runtime.getRuntime().exec(javac, null);
		new StreamGobbler(proc.getInputStream());
		new StreamGobbler(proc.getErrorStream(), System.err);
		int exitVal = proc.waitFor();
		System.out.println("ExitValue: " + exitVal);
	}

	private static class StreamGobbler extends Thread {
		final InputStream is;
		final OutputStream os;

		StreamGobbler(InputStream is) {
			this(is, null);
		}

		StreamGobbler(InputStream is, OutputStream os) {
			this.is = is;
			this.os = os == null ? System.out : os;
			this.start();
		}

		public void run() {
			try {
				byte[] buffer = new byte[1024];
				int numBytes = -1;
				while ((numBytes = is.read(buffer)) != -1)
					os.write(buffer, 0, numBytes);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} finally {
				try {
					if (os != null && os != System.out && os != System.err) os.close();
				} catch (Exception e) {
				}
				try {
					if (is != null) is.close();
				} catch (Exception e) {
				}
			}
		}
	}
}
