/**
 * Copyright (c) 2004-2011 QOS.ch
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.apache.commons.logging.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Implementation of {@link Log org.apache.commons.logging.Log} interface which
 * delegates all processing to a wrapped {@link Logger org.slf4j.Logger}
 * instance.
 * 
 * <p>
 * JCL's FATAL level is mapped to ERROR. All other levels map one to one.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class SLF4JLocationAwareLog implements Log, Serializable {

  private static final long serialVersionUID = -2379157579039314822L;

  // used to store this logger's name to recreate it after serialization
  protected String name;

  // in both Log4jLogger and Jdk14Logger classes in the original JCL, the
  // logger instance is transient
  private transient LocationAwareLogger logger;

  private static final String FQCN = SLF4JLocationAwareLog.class.getName();

  SLF4JLocationAwareLog(LocationAwareLogger logger) {
    this.logger = logger;
    this.name = logger.getName();
  }

  /**
   * Delegates to the <code>isTraceEnabled<code> method of the wrapped 
   * <code>org.slf4j.Logger</code> instance.
   */
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  /**
   * Directly delegates to the wrapped <code>org.slf4j.Logger</code> instance.
   */
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  /**
   * Directly delegates to the wrapped <code>org.slf4j.Logger</code> instance.
   */
  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  /**
   * Directly delegates to the wrapped <code>org.slf4j.Logger</code> instance.
   */
  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  /**
   * Directly delegates to the wrapped <code>org.slf4j.Logger</code> instance.
   */
  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  /**
   * Delegates to the <code>isErrorEnabled<code> method of the wrapped 
   * <code>org.slf4j.Logger</code> instance.
   */
  public boolean isFatalEnabled() {
    return logger.isErrorEnabled();
  }

  /**
   * Converts the input parameter to String and then delegates to the debug
   * method of the wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   */
  public void trace(Object message) {
    logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, String
        .valueOf(message), null, null);
  }

  /**
   * Converts the first input parameter to String and then delegates to the
   * debug method of the wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   * @param t
   *          the exception to log
   */
  public void trace(Object message, Throwable t) {
    logger.log(null, FQCN, LocationAwareLogger.TRACE_INT, String
        .valueOf(message), null, t);
  }

  /**
   * Converts the input parameter to String and then delegates to the wrapped
   * <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   */
  public void debug(Object message) {
    logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, String
        .valueOf(message), null, null);
  }

  /**
   * Converts the first input parameter to String and then delegates to the
   * wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   * @param t
   *          the exception to log
   */
  public void debug(Object message, Throwable t) {
    logger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, String
        .valueOf(message), null, t);
  }

  /**
   * Converts the input parameter to String and then delegates to the wrapped
   * <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   */
  public void info(Object message) {
    logger.log(null, FQCN, LocationAwareLogger.INFO_INT, String
        .valueOf(message), null, null);
  }

  /**
   * Converts the first input parameter to String and then delegates to the
   * wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   * @param t
   *          the exception to log
   */
  public void info(Object message, Throwable t) {
    logger.log(null, FQCN, LocationAwareLogger.INFO_INT, String
        .valueOf(message), null, t);
  }

  /**
   * Converts the input parameter to String and then delegates to the wrapped
   * <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   */
  public void warn(Object message) {
    logger.log(null, FQCN, LocationAwareLogger.WARN_INT, String
        .valueOf(message), null, null);
  }

  /**
   * Converts the first input parameter to String and then delegates to the
   * wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   * @param t
   *          the exception to log
   */
  public void warn(Object message, Throwable t) {
    logger.log(null, FQCN, LocationAwareLogger.WARN_INT, String
        .valueOf(message), null, t);
  }

  /**
   * Converts the input parameter to String and then delegates to the wrapped
   * <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   */
  public void error(Object message) {
    logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, String
        .valueOf(message), null, null);
  }

  /**
   * Converts the first input parameter to String and then delegates to the
   * wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   * @param t
   *          the exception to log
   */
  public void error(Object message, Throwable t) {
    logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, String
        .valueOf(message), null, t);
  }

  /**
   * Converts the input parameter to String and then delegates to the error
   * method of the wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   */
  public void fatal(Object message) {
    logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, String
        .valueOf(message), null, null);
  }

  /**
   * Converts the first input parameter to String and then delegates to the
   * error method of the wrapped <code>org.slf4j.Logger</code> instance.
   * 
   * @param message
   *          the message to log. Converted to {@link String}
   * @param t
   *          the exception to log
   */
  public void fatal(Object message, Throwable t) {
    logger.log(null, FQCN, LocationAwareLogger.ERROR_INT, String
        .valueOf(message), null, t);
  }

  /**
   * Replace this instance with a homonymous (same name) logger returned by
   * LoggerFactory. Note that this method is only called during deserialization.
   * 
   * @return logger with same name as returned by LoggerFactory
   * @throws ObjectStreamException
   */
  protected Object readResolve() throws ObjectStreamException {
    Logger logger = LoggerFactory.getLogger(this.name);
    return new SLF4JLocationAwareLog((LocationAwareLogger) logger);
  }
}