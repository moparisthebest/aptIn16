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

import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AptGenerationTest {

	private static final String aptDir = "./target/generated-sources/apt";
	private static final String javacDir = "./target/generated-sources/javac";
	private static final int aptLength = aptDir.length();
	private static final int javacLength = javacDir.length();

	private final List<File> apt = new ArrayList<File>();
	private final List<File> javac = new ArrayList<File>();

	@Before
	public void setUp() {
		listFiles(new File(aptDir), apt);
		listFiles(new File(javacDir), javac);
	}

	@Test
	public void sameNumberOfFiles() {
		assertEquals(apt.size(), javac.size());
	}

	@Test
	public void sameFiles() {
		for (int x = 0; x < apt.size(); ++x) {
			String aptFile = apt.get(x).getPath().substring(aptLength);
			String javacFile = javac.get(x).getPath().substring(javacLength);
			//System.out.printf("apt: '%s'\njavac: '%s'\n", aptFile, javacFile);
			assertEquals("Missing file: " + aptFile, aptFile, javacFile);
		}
	}

	@Test
	public void directoriesEqual() {
		for (int x = 0; x < apt.size(); ++x)
			assertFileEquals(apt.get(x), javac.get(x), "^  <!-- Generated from .* on .* -->$");
	}

	public static void assertFileEquals(File expected, File actual, String ignoreMatch) {
		assertEquals("File: " + expected.getPath().substring(aptLength), readFile(expected, ignoreMatch), readFile(actual, ignoreMatch));
	}

	public static String readFile(File f, String ignoreMatch) {
		StringBuilder ret = new StringBuilder();
		try {
			BufferedReader r = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = r.readLine()) != null)
				if (!line.matches(ignoreMatch))
					ret.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}


	public static void listFiles(File dir, List<File> fileList) {
		if (dir.isFile()) {
			fileList.add(dir);
			return;
		}
		if (!dir.isDirectory())
			return;
		File[] children = dir.listFiles();
		if (children == null)
			return;
		for (File child : children)
			listFiles(child, fileList);
	}
}
