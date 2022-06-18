package org.concurrency.library.juc.queueutils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtils {
//	private final static Log log = LogFactory.getLog(ReflectionUtils.class);
	
	public interface Matcher{
		boolean matches(final String pName);
	}
	
	public interface Indexer{
		void put(final Map<String, Object> pMap, final String key, final Object pObject);
	}
	
	private static Indexer defaultIndexer = new DefaultIndexer();
    private static Matcher defaultMatcher = new DefaultMatcher();

    public static class DefaultMatcher implements Matcher {
        public boolean matches(final String pName) {
            return true;
        }
    }

    public static class DefaultIndexer implements Indexer {
        public void put(final Map<String, Object> pMap, final String pKey, final Object pObject) {
            pMap.put(pKey, pObject);
        }
    }
    
    public static Map<String,Object> discoverFields(final Class<?> pClazz,final Matcher pMatcher){
    	 return discoverFields(pClazz, pMatcher, defaultIndexer);
    }

    public static Map<String, Object> discoverFields( final Class<?> pClazz)  {
        return discoverFields(pClazz, defaultMatcher, defaultIndexer);
    }

	private static Map<String, Object> discoverFields(Class<?> pClazz,Matcher pMatcher, Indexer pIndexer) {
		System.out.println("discovering fields on " + pClazz.getName());
		final Map<String,Object> result  = new HashMap<String, Object>();
		Class<?> current = pClazz;
		do{
			for(Field field:current.getDeclaredFields()){
				final String fname = field.getName();
				if(pMatcher.matches(fname)){
					pIndexer.put(result,fname, field);
				}
			}
			current = current.getSuperclass();
		}while(current!=null);
		return result;
	}
	
	 public static Map<String, Object> discoverMethods(final Class<?> pClazz,final Matcher pMatcher ) {

	        return discoverMethods(pClazz, pMatcher, defaultIndexer);
	 }

	public static Map<String, Object> discoverMethods( final Class<?> pClazz ) {

		return discoverMethods(pClazz, defaultMatcher, defaultIndexer);
	}

	public static Map<String, Object> discoverMethods( final Class<?> pClazz,final Matcher pMatcher,  final Indexer pIndexer ) {

		final Map<String, Object> result = new HashMap<String, Object>();
		Class<?> current = pClazz;
		do {
			for (Method method : current.getDeclaredMethods()) {
				final String mname = method.getName();
				if (pMatcher.matches(mname)) {
					pIndexer.put(result, mname, method);
				}
			}
			current = current.getSuperclass();
		} while(current != null);

		return result;
	}
	    
	public static String getClassName(final Object o){
		if(o==null){
			return "unknown";
		}
		return o.getClass().getName()+"@"+o.hashCode();
	}


	public static String getClassLoader(final Object o){
		if(o==null){
			return "unknow";
		}
		return getClassName(o.getClass().getClassLoader());
	}

	public static Object cast(Object o) throws IOException, ClassNotFoundException {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(buffer);
		oos.writeObject(o);
		oos.flush();
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
		return ois.readObject();
	  }

	public static void main(String[] args) {
		Map<String,Object>s =  ReflectionUtils.discoverMethods(java.util.Scanner.class);
		for(Map.Entry entry:s.entrySet()){
			System.out.println(entry.getKey()+"---"+entry.getValue());
		}
	}
}
