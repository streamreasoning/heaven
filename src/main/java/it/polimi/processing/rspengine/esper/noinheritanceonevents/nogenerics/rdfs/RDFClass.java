package it.polimi.processing.rspengine.esper.noinheritanceonevents.nogenerics.rdfs;

import lombok.NonNull;

import org.apache.log4j.Logger;

public final class RDFClass extends RDFResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Class<?> clazz;

	public RDFClass(@NonNull Class<?> class1, @NonNull String value) {
		super(value);
		this.clazz = class1;
	}

	@Override
	public RDFClass getSuper() {
		if (clazz == null) {
			Logger.getRootLogger().debug(value);
			return null;
		}
		try {
			if (!Object.class.equals(clazz.getSuperclass())) {
				Class<?> sc = clazz.getSuperclass();
				RDFResource r;
				r = (RDFResource) sc.newInstance();
				RDFClass rdfClass = new RDFClass(clazz.getSuperclass(),
						r.getValue());
				return rdfClass;
			} else
				Logger.getRootLogger().debug("root reached " + value);
			return new RDFClass(clazz, value);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new WrongSuperMethodCall(e);
		}
	}

	@Override
	public int hashCode() {
		if (clazz != null)
			return clazz.hashCode();
		else
			return "null".hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (getClass() != obj.getClass() || obj == null) {
			return false;
		}
		RDFClass other = (RDFClass) obj;
		if (other != null && clazz != null)
			return other.clazz.equals(clazz);
		else
			return false;
	}

	public Class<?> getType() {
		return clazz;
	}
}
