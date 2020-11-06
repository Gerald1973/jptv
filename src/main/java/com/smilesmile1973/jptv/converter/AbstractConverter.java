package com.smilesmile1973.jptv.converter;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author smilesmile1973@gmail.com
 *
 * @param <S> the source
 * @param <T> the target
 */
interface Converter<S, T> {

	public T toTarget(S source);

	public S toSource(T target);

	default List<T> toTargets(List<S> sources) {
		LinkedList<T> results = new LinkedList<>();
		if (sources != null && !sources.isEmpty()) {
			for (S source : sources) {
				results.add(toTarget(source));
			}
		}
		return results;
	}

	default List<S> toSources(List<T> targets) {
		LinkedList<S> results = new LinkedList<>();
		if (targets != null && !targets.isEmpty()) {
			for (T target : targets) {
				results.add(toSource(target));
			}
		}
		return results;
	}

}
