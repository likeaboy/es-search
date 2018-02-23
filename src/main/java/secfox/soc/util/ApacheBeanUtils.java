package secfox.soc.util;

import java.util.Map;

/** 
	 * 使用org.apache.commons.beanutils进行转换 
	 */ 
public class ApacheBeanUtils {  
	       
		/*
		 * Populate the JavaBeans properties of the specified bean, based on
	     * the specified name/value pairs.
		 */
	    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
	        if (map == null)  
	            return null;  
	   
	        Object obj = beanClass.newInstance();  
	   
	        org.apache.commons.beanutils.BeanUtils.populate(obj, map);  
	   
	        return obj;  
	    }    
	       
	    /*
	     *  An implementation of Map for JavaBeans which uses introspection to
	     * get and put properties in the bean.
	     */
	    public static Map<?, ?>  objectToMap(Object obj)  throws Exception {  
	        if(obj == null)  
	            return null;   
	   
	        return new org.apache.commons.beanutils.BeanMap(obj);  
	    }    
	       
	}