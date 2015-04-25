package com.bloodnbonesgaming.blockphysics.util;

public class TypeHelper {

	public static <T> T cast(final Class<T> to, final Object obj){
		return to.cast(obj);
	}

	public static boolean instanceOf(final Class<?> type, final Object obj){
		return type.isAssignableFrom(obj.getClass());
	}

}
