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
import com.moparisthebest.mirror.declaration.ConvertDeclaration;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.moparisthebest.mirror.log.Debug;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

public class ConvertAnnotationProcessorFactory implements Processor {

	public static final boolean debug = false;

	static {
		String command = System.getProperty("sun.java.command");
		// hack to stop IDEA from permanently freezing when processing annotations
		// todo: please tell me why, for the life of me I can't figure it out...
		if (command != null && command.startsWith("com.intellij.rt.compiler")) {
			// command looks like: com.intellij.rt.compiler.JavacRunner java version 1.6.0_30 com.sun.tools.javac.Main [options]
			System.setOut(System.err);
			System.err.println("INFO: activated IDEA hack of re-directing System.out to System.err");
		}

		PrintStream oldOut = System.out;

		if ((debug || System.getProperty("aptin16.debug") != null))
			try {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream("/aptin16.log");
				} catch (Exception e) {
					fos = new FileOutputStream(System.getProperty("user.home") + "/aptin16.log");
				}
				PrintStream out = new PrintStream(fos, true);
				System.setOut(out);
				System.setErr(out);
				System.out.println(new Date() + ": log successfully set!");
				System.out.printf("oldOut: '%s' class: '%s'\n", oldOut.toString(), oldOut.getClass().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	private final AnnotationProcessorFactory internal;

	// at least for now, all of the ProcessingEnvironment's multiple instances get sent
	// are the EXACT same object, so we might as well make this static and save some time
	protected static ConvertAnnotationProcessorEnvironment env = null;

	public ConvertAnnotationProcessorFactory(String annotationProcessorFactoryName) {
		AnnotationProcessorFactory internal = null;
		try {
			internal = (AnnotationProcessorFactory) Class.forName(annotationProcessorFactoryName).newInstance();
			System.out.println("ConvertAnnotationProcessorFactory running " + internal.getClass().getName());
		} catch (Throwable e) {
			System.out.printf("Not running AnnotationProcessorFactory '%s' because of error!  Not on classpath?\n", annotationProcessorFactoryName);
			if(Debug.debug)
				e.printStackTrace();
		}
		this.internal = internal;
	}

	public ConvertAnnotationProcessorFactory(AnnotationProcessorFactory internal) {
		System.out.println("ConvertAnnotationProcessorFactory running " + (internal == null ? "null" : internal.getClass().getName()));
		this.internal = internal;
	}

	public static Set<String> cleanOptions(Collection<String> input) {
		Set<String> ret = new LinkedHashSet<String>(input.size());
		for (String option : input)
			ret.add(option.replaceFirst("^-A", "")); // have to strip -A from beginning of options...
		if (Debug.debug)
			System.out.println("supportedOptions: " + ret);
		return ret;
	}

	@Override
	public Set<String> getSupportedOptions() {
		if(internal == null)
			return Collections.emptySet();
		return cleanOptions(internal.supportedOptions());
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		if(internal == null)
			return Collections.emptySet();
		if (Debug.debug)
			System.out.printf("factory: '%s' supportedAnnotationTypes: '%s'\n", internal.getClass().getName(), internal.supportedAnnotationTypes());
		return Convertable.toSet(internal.supportedAnnotationTypes());
	}

	private static synchronized void staticInit(ProcessingEnvironment processingEnv) {
		if (env != null)
			return;
		env = new ConvertAnnotationProcessorEnvironment(processingEnv);
		/*
				  // the following helps us debug, since I know no way of debugging in javac itself
				  System.setOut(
						  new PrintStream(System.out) {


							  public static final byte debugLevel = 90;

							  public void printInfo() {
								  StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
								  int x = 2; // start at 3
								  printStackTrace(stackTrace[x], "", "");
								  int level = x + debugLevel;
								  StringBuilder tabs = new StringBuilder();
								  while (++x < level && x < stackTrace.length)
									  printStackTrace(stackTrace[x], "Called", tabs.append('\t').toString());
							  }

							  private void printStackTrace(StackTraceElement ste, String msg, String returnType) {
								  super.printf("%s%s.%s(%s:%d) %s\n",
										  returnType,
										  //ste.getClassName().replaceFirst(".*\\.", "")
										  ste.getClassName()
										  , ste.getMethodName(),
										  ste.getFileName(), ste.getLineNumber(),
										  msg);
							  }

							  @Override
							  public void println(String x) {
								  printInfo();
								  super.println(x);
							  }
						  }
				  );
				  System.setErr(System.out);
				  //System.err.println("woohoo! quitting!");
				  //System.exit(0);
				  */
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		//env = new ConvertAnnotationProcessorEnvironment(processingEnv);  // if we change it back to an instance variable
		staticInit(processingEnv);
	}

	/**
	 * @param annotations
	 * @param roundEnv
	 * @return
	 */
	@Override
	public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if(internal == null)
			return false;
		env.getFiler().setModified(false);
		env.setRoundEnv(roundEnv);
		internal.getProcessorFor(ConvertDeclaration.getConvertable().convertToSet(annotations, AnnotationTypeDeclaration.class), env).process();
		return env.getFiler().isModified();  //todo: don't know what to return here
		//return true;
		//return false; // apt didn't return anything or have a notion of 'claiming' annotations, but false breaks beehive...
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
		return Collections.emptyList();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof AnnotationProcessorFactory) return o.equals(internal);
		if (o == null || getClass() != o.getClass()) return false;

		ConvertAnnotationProcessorFactory that = (ConvertAnnotationProcessorFactory) o;

		return internal == that.internal || (internal != null && internal.equals(that.internal));
	}

	@Override
	public int hashCode() {
		return internal == null ? 0 : internal.hashCode();
	}

	@Override
	public String toString() {
		return internal == null ? "null" : internal.toString();
	}
}
