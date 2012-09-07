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

public abstract class Convertable<F, T> implements ConvertableIface<F, T> {

	public abstract T convertToType(F from);

	@Override
	@SuppressWarnings({"unchecked"})
	public <E extends T> E convertToType(F from, Class<E> type) {
		return (E) convertToType(from);
	}

	public <E extends T> List<E> convert(Collection<? extends F> from, Class<E> type) {
		List<E> ret = new ArrayList<E>(from.size());
		for (F fromF : from)
			ret.add(convertToType(fromF, type));
		return ret;
	}

	public <E extends T> Set<E> convertToSet(Collection<? extends F> from, Class<E> type) {
		Set<E> ret = new LinkedHashSet<E>(from.size());
		for (F fromF : from)
			ret.add(convertToType(fromF, type));
		return ret;
	}

	public List<T> convert(Collection<? extends F> from) {
		List<T> ret = new ArrayList<T>(from.size());
		for (F fromF : from)
			ret.add(convertToType(fromF));
		return ret;
	}

	public Set<T> convertToSet(Collection<? extends F> from) {
		Set<T> ret = new LinkedHashSet<T>(from.size());
		for (F fromF : from)
			ret.add(convertToType(fromF));
		return ret;
	}

	@SuppressWarnings({"unchecked"})
	public static <R> R[] unwrapClass(Object[] convertable, R[] dest) {
		if (convertable == null || dest == null)
			return null;
		if (convertable.length != dest.length)
			throw new RuntimeException("convertable and dest need to be arrays of same length!");
		try {
			Class<R> componentType = (Class<R>) dest.getClass().getComponentType();
			for (int x = 0; x < dest.length; ++x)
				dest[x] = unwrapClass(convertable[x], componentType);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("fatal error!");
			System.exit(1);
		}
		return dest;
	}

	@SuppressWarnings({"unchecked"})
	public static <R> R unwrapClass(Object convertable, Class<R> iface) {
		if (convertable == null)
			return null;
		try {
			return iface.cast(((ConvertableIface<? extends R, ?>) convertable).unwrap());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("fatal error!");
			System.exit(1);
		}
		return null;
	}

	public static <E extends Enum<E>> List<E> convertEnums(Collection<? extends Enum> from, Class<E> newe) {
		List<E> ret = new ArrayList<E>(from.size());
		for (Enum old : from)
			ret.add(convertEnum(old, newe));
		return ret;
	}

	public static <E extends Enum<E>> E convertEnum(Enum old, Class<E> newe) {
		return Enum.valueOf(newe, old.toString().toUpperCase());
	}

	public static <T> Set<T> toSet(Collection<T> c) {
		try {
			if (c instanceof Set)
				return (Set<T>) c;
		} catch (Exception e) {
			// do nothing
		}
		Set<T> ret = new LinkedHashSet<T>();
		ret.addAll(c);
		return ret;
	}

	@SuppressWarnings({"unchecked"})
	public static <E> List<E> sort(List<E> ret) {
		//System.out.println("un-sorted: "+ret);
		Collections.sort(ret, toStringComparator);
		//System.out.println("sorted: "+ret);
		return ret;
	}

	@SuppressWarnings({"unchecked"})
	public static <E> Set<E> sort(Set<E> set) {
		/*return set;*/

		TreeSet<E> ret = new TreeSet<E>(toStringComparator);
		ret.addAll(set);
		return ret;

	}

	@SuppressWarnings({"unchecked"})
	public static <E> E[] sort(E[] ret) {
		Arrays.sort(ret, toStringComparator);
		return ret;
	}

	public static final Comparator toStringComparator = new ToStringComparator();

	private static class ToStringComparator implements Comparator<Object> {
		public int compare(Object f1, Object f2) {
			return f1.toString().compareTo(f2.toString());
		}
	}

}
