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

import com.moparisthebest.mirror.convert.Convertable;
import com.moparisthebest.mirror.util.ConvertTypes;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorListener;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.Declarations;
import com.sun.mirror.util.Types;
import com.moparisthebest.mirror.declaration.ConvertDeclaration;
import com.moparisthebest.mirror.log.Debug;
import com.moparisthebest.mirror.type.ConvertTypeMirror;
import com.moparisthebest.mirror.util.ConvertDeclarations;
import com.moparisthebest.mirror.util.ConvertSourcePosition;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.util.*;

public class ConvertAnnotationProcessorEnvironment implements AnnotationProcessorEnvironment {

	private static final Object lock = new Object();
	private boolean deriveSourcePath;
	private final Map<String, String> options;

	private final ProcessingEnvironment internal;
	private final ConvertFiler filer;
	private final ConvertMessager messager;

	private RoundEnvironment roundEnv = null;

	public static Elements elements = null;

	private static final boolean debugOptions = ConvertAnnotationProcessorFactory.debug;

	public ConvertAnnotationProcessorEnvironment(ProcessingEnvironment internal) {
		//System.out.printf("internal: '%s' hash: '%s'\n", internal.toString(), internal.hashCode());
		this.internal = internal;
		filer = new ConvertFiler(internal.getFiler());
		messager = new ConvertMessager(internal.getMessager());
		ConvertSourcePosition.setProcessingEnvironment(internal, this);
		// now set some static variables around, this could be nicer...
		elements = internal.getElementUtils();
		ConvertDeclaration.elements = elements;
		ConvertTypeMirror.types = internal.getTypeUtils();
		ConvertDeclaration.messager = ConvertTypeMirror.messager = internal.getMessager();

		// now calculate options once, since they are sort-of expensive
		// real apt passes them back like so:
		//{-d=./aptgen, save-parameter-names=null, -s=./aptgen, -Aweb.content.root=./aptgen=null, -classpath=.;../netui16compiler/target/classes;../netui/target/classes;../controls/target/classes;../target/netui16compiler.jar, -nocompile=null, -factory=org.apache.beehive.netui.compiler.apt.PageFlowAnnotationProcessorFactory, -sourcepath=./aptgen}
		// whereas internal.getOptions passes them like this:
		// {web.content.root=./aptgen}
		final Map<String, String> ret = new HashMap<String, String>();
		final Map<String, String> internalOptions = internal.getOptions();
		if (debugOptions) {
			final File workingDir = new File(".");
			System.out.println("working dir: " + workingDir.getAbsolutePath());
			System.err.println("@@@@options: " + internalOptions);
		}
		// first put -A in front of all options
		for (Map.Entry<String, String> internalOption : internalOptions.entrySet())
			ret.put("-A" + internalOption.getKey(), internalOption.getValue());
		// now set classpath
		// some annotation processors (I'm looking at you beehive...) request '-classpath' and ignore '-cp',
		// so put it under both :rolleyes:
		ret.put("-cp", System.getProperty("java.class.path"));
		ret.put("-classpath", System.getProperty("java.class.path"));
		if (System.getProperty("sun.boot.class.path") != null)
			ret.put("-bootclasspath", System.getProperty("sun.boot.class.path"));
		if (debugOptions) {
			System.out.println("System properties: " + System.getProperties());
			System.out.println("java.class.path: " + System.getProperty("java.class.path"));
			System.out.println("java.source.path: " + System.getProperty("java.source.path"));
			System.out.println("sun.boot.class.path: " + System.getProperty("sun.boot.class.path"));
			System.err.println("@@@@partialoptions: " + ret);
		}
		// now the hard part, get the rest of the options sent to javac...

		// this will probably only work on sun compilers, but I'm out of options
		String command = System.getProperty("sun.java.command");
		// can't split by space because it isn't escaped or quoted...
		//command: com.sun.tools.javac.Main -processor org.apache.beehive.netui.compiler.java6.PageFlowAnnotationProcessor -proc:only -Aweb.content.root=./apt gen -d ./apt gen -s ./apt gen -sourcepath ./apt gen -cp .;../netui16compiler/target/classes;../netui/target/classes;../controls/target/classes;C:\Users\burtrutj\.m2\repository\org\apache\beehive\beehive-netui-compiler\1.0.2\beehive-netui-compiler-1.0.2.jar;C:\Users\burtrutj\.m2\repository\org\apache\beehive\beehive-controls\1.0.2\beehive-controls-1.0.2.jar;../target/netui16compiler.jar backingControls/BackingControlsController.java backingControls/TestControl.java backingControls/page1.java
		if (debugOptions)
			System.err.println("command: " + command);
		if (command != null)
			try {
				String[] options = command.split(" -"); // this should only break if a single argument has the literal string ' -' in it, hopefully it won't happen often
				if (debugOptions)
					System.out.println("options length: " + options.length);
				// skip index one because it's the name of the program, ie com.sun.tools.javac.Main
				for (int i = 1; i < options.length - 1; ++i) {
					if (debugOptions)
						System.err.println("option: " + options[i]);
					if (options[i].startsWith("A") || options[i].startsWith("cp") || options[i].startsWith("classpath"))
						continue; // it's an option we already have from above
					if (debugOptions)
						System.err.println("processing option: " + options[i]);
					String[] item = options[i].split(" ", 2);
					if (item.length == 0)
						continue; // shouldn't ever be here
					if (item[0].equals("bootclasspath") && item[1].trim().startsWith("@") && ret.containsKey("-bootclasspath"))
						continue;
					ret.put("-" + item[0], item.length < 2 ? null : item[1].trim());
				}
				// we saved the last option for last, because it has all the <source files> appended to it, what to do...
				// all items in <source files> have to exist on the filesystem before we get here, so starting from the front
				// grab the longest string that is a complete file, discard, and continue doing that until the file doesn't end
				// with .java or we are out of files.
				if (debugOptions)
					System.err.println("last option: " + options[options.length - 1]);
				String[] item = options[options.length - 1].split(" ", 2);
				if (item.length < 2)
					ret.put(item[0], null);
				else {
					// I thought there had to be an item at 1, or no files would have been passed to javac
					// turns out things like maven have their own launchers...
					final String valueAndFiles = item[1];
					int stopIncluding = valueAndFiles.length();
					// match the biggest file possible in the string, starting from the beginning
					outer:
					for (int i = 0; i < stopIncluding; ++i) {
						do {
							String fileName = valueAndFiles.substring(i, stopIncluding);
							//System.err.println("fileName: "+fileName);
							if (new File(fileName).isFile()) {
								//System.err.println("success! it's a file!");
								if (!fileName.trim().toLowerCase().endsWith(".java")) {
									//System.err.println("breaking because filename doesn't end with .java: "+fileName.toLowerCase());
									break outer;
								}
								stopIncluding = i;
								i = 0;
								continue outer;
							}
						} while (++i < stopIncluding);
					}
					String value = valueAndFiles.substring(0, stopIncluding).trim();
					if (debugOptions)
						System.err.printf("last option: '%s':'%s'\n", item[0], value.isEmpty() ? null : value);
					ret.put(item[0], value.isEmpty() ? null : value);
				}

				// now make sure -sourcepath is in there, some annotation processors (again, I'm looking at you, flipping beehive)
				// require it be specified, if it isn't, try to hack it out...
				//if(false)
				if (ret.get("-sourcepath") == null || ret.get("-sourcepath").isEmpty())
					try {
						File path = null;
						try {
							FileObject sourcePath = internal.getFiler().getResource(StandardLocation.SOURCE_PATH, "", "trash");
							//FileObject sourcePath = internal.getFiler().getResource(StandardLocation.SOURCE_PATH, "", "com/moparisthebest/controls/studysearch/studySearchCtrl.java");

							//FileObject sourcePath = internal.getFiler().createResource(StandardLocation.SOURCE_PATH, "", "trash");
							path = new File(sourcePath.toUri());
							path = path.getParentFile().getCanonicalFile();
						} catch (Exception e) {
							//todo: this fails on jdk7, but works fine on jdk6, figure out a better way, for now, deriveSourcePath is called
							if (debugOptions)
								e.printStackTrace();
							//Iterable<? extends File> spi = ToolProvider.getSystemJavaCompiler().getStandardFileManager(null, null, null).getLocation(StandardLocation.SOURCE_PATH);
							//Iterator<? extends File> sp = spi.iterator();
							//while(sp.hasNext())
							//    System.out.println(sp.next());
						}
						if (debugOptions)
							System.err.println("guessed sourcepath: " + path);
						if (path != null)
							ret.put("-sourcepath", path.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
			} catch (Throwable e) {
				System.out.println("Exception parsing command line options:");
				e.printStackTrace();
				System.out.println("successfully parsed options are: " + ret);
			}
		// try to derive it if it still doesn't exist and it's possible with ConvertSourcePosition
		deriveSourcePath = ConvertSourcePosition.supported() && (ret.get("-sourcepath") == null || ret.get("-sourcepath").isEmpty());
		//System.err.println("@@@@fixedoptions: " + ret);
		//System.exit(0);
		this.options = deriveSourcePath ? ret : Collections.unmodifiableMap(ret);
		if (debugOptions)
			System.out.println("options: " + this.options);
	}

	public void deriveSourcePath(File file, String packageName) {
		if (!deriveSourcePath)
			return;
		try {
			File sourcePath = file;
			int subDirs = packageName.split("\\.").length;
			while (subDirs-- >= 0)
				sourcePath = sourcePath.getParentFile();
			//System.out.println("sourcePath: "+sourcePath);
			// todo: we should really concatenate different sourcepath's with the platform's
			// todo: path seperator, but for jdk6 we just specify one source path,
			// todo: so we might as well do the same here for now
			deriveSourcePath = false;
			this.options.put("-sourcepath", sourcePath.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRoundEnv(RoundEnvironment roundEnv) {
		this.roundEnv = roundEnv;
	}

	@Override
	public Map<String, String> getOptions() {
		Debug.implemented("Map<String, String>");
		return options;
	}

	@Override
	public Messager getMessager() {
		return messager;
	}

	@Override
	public ConvertFiler getFiler() {
		return filer;
	}

	@Override
	public Declarations getDeclarationUtils() {
		return new ConvertDeclarations(internal.getElementUtils());
	}

	@Override
	public Types getTypeUtils() {
		return new ConvertTypes(internal.getTypeUtils());
	}

	@Override
	public Collection<TypeDeclaration> getSpecifiedTypeDeclarations() {
		Debug.implemented("Collection<TypeDeclaration>");
		return Collections.emptyList(); //todo: probably not right?
	}

	@Override
	public Collection<TypeDeclaration> getTypeDeclarations() {
		Debug.implemented("Collection<TypeDeclaration>");
		return Collections.emptyList(); //todo: probably not right?
	}

	@Override
	public PackageDeclaration getPackage(String name) {
		Debug.implemented("PackageDeclaration");
		return ConvertDeclaration.convert(internal.getElementUtils().getPackageElement(name), PackageDeclaration.class);
	}

	@Override
	public TypeDeclaration getTypeDeclaration(String name) {
		Debug.implemented("TypeDeclaration");
		return ConvertDeclaration.convert(internal.getElementUtils().getTypeElement(name), TypeDeclaration.class);
	}

	@Override
	public Collection<Declaration> getDeclarationsAnnotatedWith(AnnotationTypeDeclaration a) {
		Debug.implemented("Collection<Declaration>");
		//System.err.println("AnnotationTypeDeclaration: "+a);
		//System.err.println("TypeElement: "+Convertable.unwrapClass(a, TypeElement.class));
		List<Declaration> ret = ConvertDeclaration.getConvertable().convert(roundEnv.getElementsAnnotatedWith(Convertable.unwrapClass(a, TypeElement.class)));
		//System.err.println("retnoconvert: "+roundEnv.getElementsAnnotatedWith(Convertable.unwrapClass(a, TypeElement.class)));
		//System.err.println("ret: "+ret);
		//System.err.println("retclass: "+ret.toArray()[0].getClass());
		return ret;
		//return Convertable.sort(ret);
	}

	@Override
	public void addListener(AnnotationProcessorListener listener) {
		// not supported
	}

	@Override
	public void removeListener(AnnotationProcessorListener listener) {
		// not supported
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof ProcessingEnvironment) return internal.equals(o);
		if (o == null || getClass() != o.getClass()) return false;

		ConvertAnnotationProcessorEnvironment that = (ConvertAnnotationProcessorEnvironment) o;

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
