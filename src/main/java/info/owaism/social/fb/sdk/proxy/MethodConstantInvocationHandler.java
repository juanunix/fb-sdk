/**
 * Apache 2.0 License.
 * @author Owais Mohamed
 */
package info.owaism.social.fb.sdk.proxy;

import static com.google.common.base.Preconditions.checkArgument;
import info.owaism.social.fb.sdk.annotation.Constant;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 *
 */
public class MethodConstantInvocationHandler implements InvocationHandler {

	@NotNull
	private Object targetObject;
	private Class<?> targetClass;

	/**
	 * 
	 */
	public MethodConstantInvocationHandler(@NotNull Object targetObject) {
		checkArgument(null != targetObject);
		this.targetObject = targetObject;
		this.targetClass = this.targetObject.getClass();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		Constant[] classConstants = this.targetClass
				.getAnnotationsByType(Constant.class);
		Constant[] methodConstants = this.targetClass.getMethod(
				method.getName(), method.getParameterTypes())
				.getAnnotationsByType(Constant.class);

		if (null == classConstants && null == methodConstants) {
			return method.invoke(targetObject, args);
		}

		Map<String, Object> methodConstantMap = MethodConstant.init();

		putConstantsInMap(methodConstantMap, classConstants);
		putConstantsInMap(methodConstantMap, methodConstants);

		try {
			return method.invoke(targetObject, args);
		} finally {
			MethodConstant.destroy();
		}
	}

	/**
	 * @param methodConstantMap
	 * @param constants
	 */
	private void putConstantsInMap(Map<String, Object> methodConstantMap,
			Constant[] constants) {
		if (null == constants) {
			return;
		}
		for (Constant constant : constants) {
			methodConstantMap.put(constant.constantName(),
					constant.constantValue());
		}
	}

}
