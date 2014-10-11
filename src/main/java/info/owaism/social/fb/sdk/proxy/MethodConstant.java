/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class used to get constants defined for a method execution.
 */
public class MethodConstant {

	/**
	 * Utility Class.
	 */
	private MethodConstant() {
	}

	private static final ThreadLocal<Map<String, Object>> METHOD_CONSTANT_THREAD_LOCAL = new ThreadLocal<Map<String, Object>>();

	/**
	 * Sets up map to put in constants for this method. Constants put into the returned {@link Map} will be put into the thread local.
	 * @return
	 * 	{@link Map} in ThreadLocal
	 */
	static final Map<String, Object> init() {
		Map<String, Object> constantMap = new HashMap<String, Object>();
		METHOD_CONSTANT_THREAD_LOCAL.set(constantMap);
		return constantMap;
	}

	/**
	 * Destroys all constants for this method.
	 */
	static final void destroy() {
		METHOD_CONSTANT_THREAD_LOCAL.remove();
	}

	/**
	 * Gets a constant that has been defined for this method.
	 * @param constantName Name of the constant required.
	 * @return Constant value defined.
	 */
	public static final String get(String constantName) {
		Map<String, Object> constantMap = METHOD_CONSTANT_THREAD_LOCAL.get();

		if (null != constantMap) {
			String value = (String)constantMap.get(constantName);

			if (null != value) {
				return value;
			}
		}

		throw new IllegalStateException(
				String.format(
						"Unable to find constant '%s' defined for this method. Either you have used to @Constant Annotation properly or you have not defined proxy for this Object.",
						constantName));
	}

}
