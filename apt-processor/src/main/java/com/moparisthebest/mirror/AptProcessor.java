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

package com.moparisthebest.mirror;

import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.moparisthebest.mirror.apt.ConvertAnnotationProcessorEnvironment;
import com.moparisthebest.mirror.apt.ConvertAnnotationProcessorFactory;
import com.moparisthebest.mirror.declaration.ConvertDeclaration;

import javax.annotation.processing.Completion;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AptProcessor implements Processor {

	final List<AnnotationProcessorFactory> factories;
	final Set<String> supportedOptions;
	final Set<String> supportedAnnotationTypes;

	protected ConvertAnnotationProcessorEnvironment env;

	public AptProcessor() throws IOException {
		System.out.println("AptProcessor starting!");
		Enumeration<URL> resources = getClass().getClassLoader().getResources("META-INF/services/com.sun.mirror.apt.AnnotationProcessorFactory");
		Set<String> apfSet = new HashSet<String>();
		String line;
		while (resources.hasMoreElements()) {
			try {
				Scanner scan = new Scanner(resources.nextElement().openStream());
				while (scan.hasNextLine()) {
					line = scan.nextLine().trim();
					// if line is empty or it's a comment (whitespace leading a hashtag), then skip
					if (line.isEmpty() || line.matches("^\\s*#.*"))
						continue;
					apfSet.add(line);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<AnnotationProcessorFactory> factoryList = new ArrayList<AnnotationProcessorFactory>(apfSet.size());
		Collection<String> options = new ArrayList<String>();
		Set<String> annotationTypes = new HashSet<String>();
		for (String apfName : apfSet) {
			try {
				AnnotationProcessorFactory apf = (AnnotationProcessorFactory) Class.forName(apfName).newInstance();
				System.out.println("AptProcessor running " + apfName);
				factoryList.add(apf);
				annotationTypes.addAll(apf.supportedAnnotationTypes());
				options.addAll(apf.supportedOptions());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.factories = Collections.unmodifiableList(factoryList);
		this.supportedOptions = Collections.unmodifiableSet(ConvertAnnotationProcessorFactory.cleanOptions(options));
		this.supportedAnnotationTypes = Collections.unmodifiableSet(annotationTypes);
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		//env.getFiler().setModified(false); // don't care about modified here I guess, just always return true
		env.setRoundEnv(roundEnv);
		final Set<AnnotationTypeDeclaration> annotationTypes =
				Collections.unmodifiableSet(
						ConvertDeclaration.getConvertable().convertToSet(annotations, AnnotationTypeDeclaration.class));
		for (AnnotationProcessorFactory factory : factories) {
			// sending in all instead of just the ones they handle *sometimes* causes exceptions, so I guess we will
			// sort them out...
			factory.getProcessorFor(annotationTypes, env).process();
			/*
			Set<AnnotationTypeDeclaration> atds = new HashSet<AnnotationTypeDeclaration>();
			Collection<String> supportedTypes = factory.supportedAnnotationTypes();
			for(AnnotationTypeDeclaration atd : annotationTypes)
				if(supportedTypes.contains(atd.))
					atd.
			if(!atds.isEmpty())
				factory.getProcessorFor(annotationTypes, env).process();
			*/
		}
		//return env.getFiler().isModified();
		return true;
	}

	@Override
	public Set<String> getSupportedOptions() {
		return supportedOptions;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return supportedAnnotationTypes;
	}

	@Override
	public void init(ProcessingEnvironment processingEnv) {
		env = new ConvertAnnotationProcessorEnvironment(processingEnv);
		/*
		if (!(processingEnv instanceof com.sun.tools.javac.processing.JavacProcessingEnvironment)) {
			return;
		}
		try {
			System.out.println(processingEnv.getClass().getName());
			com.sun.tools.javac.processing.JavacProcessingEnvironment pe = (com.sun.tools.javac.processing.JavacProcessingEnvironment) processingEnv;
			Field discoveredProcs = pe.getClass().getDeclaredField("discoveredProcs");
			com.sun.tools.javac.processing.JavacProcessingEnvironment.DiscoveredProcessors  discoveredProcs.
		} catch (Exception e) {

		}
		System.exit(0);
		*/
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	@Override
	public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotation, ExecutableElement member, String userText) {
		return Collections.emptyList();
	}
}
