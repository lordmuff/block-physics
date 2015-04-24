package com.bloodnbonesgaming.blockphysics.util;

public class TypeHelper {

	public static <T> T cast(Class<T> to, Object obj){
		return to.cast(obj);
	}
	
	public static boolean instanceOf(Class<?> type, Object obj){
		return type.isAssignableFrom(obj.getClass());
	}
	
}
