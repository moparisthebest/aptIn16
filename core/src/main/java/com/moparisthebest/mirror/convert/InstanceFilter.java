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

package com.moparisthebest.mirror.convert;

import java.util.*;

public class InstanceFilter {

	// Assumes targetKinds and E are sensible.
	private static <E, F extends E, C extends Collection<F>> C collectionFilter(C collection, Iterable<? extends E> elements, Class<F> clazz) {
		for (E e : elements) {
			//if (clazz.isAssignableFrom(e.getClass()))
			if (clazz.isInstance(e))
				collection.add(clazz.cast(e));
		}
		return collection;
	}

	// Assumes targetKinds and E are sensible.
	public static <E, F extends E> List<F> listFilter(Iterable<? extends E> elements, Class<F> clazz) {
		return collectionFilter(new ArrayList<F>(), elements, clazz);
	}

	// Assumes targetKinds and E are sensible.
	public static <E, F extends E> Set<F> setFilter(Iterable<? extends E> elements, Class<F> clazz) {
		// Return set preserving iteration order of input set.
		return collectionFilter(new LinkedHashSet<F>(), elements, clazz);
	}
}
