/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.util;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class HttpUtil {

	/**
	 * 
	 */
	private HttpUtil() {
		// TODO Auto-generated constructor stub
	}
	
	// TODO refine code. 
	public static Map<String, String> getQueryMap(String query)  
	 {  
	     String[] params = query.split("&");  
	     Map<String, String> map = new HashMap<String, String>();  
	     for (String param : params)  
	     {  
	         String name = param.split("=")[0];  
	         String value = param.split("=")[1];  
	         map.put(name, value);  
	     }  
	     return map;  
	 } 

}
