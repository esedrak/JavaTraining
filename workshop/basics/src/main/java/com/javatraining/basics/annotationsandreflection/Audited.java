package com.javatraining.basics.annotationsandreflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method-level annotation marking an operation for audit logging.
 *
 * <p>An AOP advice or manual reflection can detect this annotation at runtime and record an audit
 * trail entry for the annotated method invocation.
 *
 * <p>Example:
 *
 * <pre>
 * {@literal @}Audited(operation = "TRANSFER_FUNDS")
 * public void transferFunds(String fromId, String toId, double amount) { ... }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Audited {

  /**
   * Human-readable name of the operation being audited (e.g. "CREATE_USER"). Defaults to the empty
   * string, in which case the method name should be used.
   */
  String operation() default "";
}
