/*
 * Copyright (c) 2004-2005 SLF4J.ORG
 * Copyright (c) 2004-2005 QOS.ch
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute, and/or sell copies of  the Software, and to permit persons
 * to whom  the Software is furnished  to do so, provided  that the above
 * copyright notice(s) and this permission notice appear in all copies of
 * the  Software and  that both  the above  copyright notice(s)  and this
 * permission notice appear in supporting documentation.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR  A PARTICULAR PURPOSE AND NONINFRINGEMENT
 * OF  THIRD PARTY  RIGHTS. IN  NO EVENT  SHALL THE  COPYRIGHT  HOLDER OR
 * HOLDERS  INCLUDED IN  THIS  NOTICE BE  LIABLE  FOR ANY  CLAIM, OR  ANY
 * SPECIAL INDIRECT  OR CONSEQUENTIAL DAMAGES, OR  ANY DAMAGES WHATSOEVER
 * RESULTING FROM LOSS  OF USE, DATA OR PROFITS, WHETHER  IN AN ACTION OF
 * CONTRACT, NEGLIGENCE  OR OTHER TORTIOUS  ACTION, ARISING OUT OF  OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 * Except as  contained in  this notice, the  name of a  copyright holder
 * shall not be used in advertising or otherwise to promote the sale, use
 * or other dealings in this Software without prior written authorization
 * of the copyright holder.
 *
 */

package org.slf4j.impl;

import org.slf4j.Marker;
import org.slf4j.MarkingLogger;


/**
 * A simple (and direct) implementation that logs messages of level
 * INFO or higher on the console (<code>System.out<code>).
 *
 * <p>The output includes the relative time in milliseconds, thread
 * name, the level, logger name, and the message followed by the line
 * separator for the host.  In log4j terms it amounts to the "%r [%t]
 * %level %logger - %m%n" pattern.  *</p>
 *
 * <p>Sample output follows.</p>
 * <pre>
176 [main] INFO examples.Sort - Populating an array of 2 elements in reverse order.
225 [main] INFO examples.SortAlgo - Entered the sort method.
304 [main] INFO examples.SortAlgo - Dump of interger array:
317 [main] INFO examples.SortAlgo - Element [0] = 0
331 [main] INFO examples.SortAlgo - Element [1] = 1
343 [main] INFO examples.Sort - The next log statement should be an error message.
346 [main] ERROR examples.SortAlgo - Tried to dump an uninitialized array.
        at org.log4j.examples.SortAlgo.dump(SortAlgo.java:58)
        at org.log4j.examples.Sort.main(Sort.java:64)
467 [main] INFO  examples.Sort - Exiting main method.
</pre>
 *
 * @author Ceki G&uuml;lc&uuml;
 */
public class MSimpleLogger implements MarkingLogger {
  /**
   * Mark the time when this class gets loaded into memory.
   */
  private static long startTime = System.currentTimeMillis();
  public static final String LINE_SEPARATOR =
    System.getProperty("line.separator");
  private static String INFO_STR = "INFO";
  private static String WARN_STR = "WARN";
  private static String ERROR_STR = "ERROR";
  String name;

  /**
   * Package access allows only {@link SimpleLoggerFactory} to instantiate
   * SimpleLogger instances.
   */
  MSimpleLogger(String name) {
    this.name = name;
  }

  public String getName() {
    return name;    
  }
  /**
   * Always returns false.
   * @return always false
   */
  public boolean isDebugEnabled() {
    return false;
  }

  /**
   * Always returns false.
   * @return always false
   */
  public boolean isDebugEnabled(Marker marker) {
    return false;
  }
  
  /**
   * A NOP implementation, as this logger is permanently disabled for
   * the DEBUG level.
   */
  public void debug(String msg) {
    // NOP
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for
   * the DEBUG level.
   */
  public void debug(String format, Object param1) {
    // NOP
  }

  /**
   * A NOP implementation, as this logger is permanently disabled for
   * the DEBUG level.
   */
  public void debug(String format, Object param1, Object param2) {
    // NOP
  }

  public void debug(String format, Object[] argArray) {
    // NOP
  }
  
  /**
   * A NOP implementation, as this logger is permanently disabled for
   * the DEBUG level.
   */
  public void debug(String msg, Throwable t) {
    // NOP
  }

  public void debug(Marker marker, String msg) {
  }

  public void debug(Marker marker, String format, Object arg) {
    debug(format, arg);
  }

  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    debug(format, arg1, arg2);
  }

  public void debug(Marker marker, String msg, Throwable t) {
    debug(msg, t);
  }

  /**
   * This is our internal implementation for logging regular (non-parameterized)
   * log messages.
   *
   * @param level
   * @param message
   * @param t
   */
  private void log(String level, String message, Throwable t) {
    StringBuffer buf = new StringBuffer();

    long millis = System.currentTimeMillis();
    buf.append(millis - startTime);

    buf.append(" [");
    buf.append(Thread.currentThread().getName());
    buf.append("] ");

    buf.append(level);
    buf.append(" ");

    buf.append(name);
    buf.append(" - ");

    buf.append(message);

    buf.append(LINE_SEPARATOR);

    System.out.print(buf.toString());
    if (t != null) {
      t.printStackTrace(System.out);
    }
    System.out.flush();
  }

  /**
   * For formatted messages, first substitute arguments and then log.
   *
   * @param level
   * @param format
   * @param param1
   * @param param2
   */
  private void formatAndLog(
    String level, String format, Object arg1, Object arg2) {
    String message = MessageFormatter.format(format, arg1, arg2);
    log(level, message, null);
  }

  /**
   * Always returns true.
   */
  public boolean isInfoEnabled() {
    return true;
  }

  /**
   * Always returns true.
   */
  public boolean isInfoEnabled(Marker marker) {
    return true;
  }
  
  /**
   * A simple implementation which always logs messages of level INFO according
   * to the format outlined above.
   */
  public void info(String msg) {
    log(INFO_STR, msg, null);
  }

  /**
   * Perform single parameter substituion before logging the message of level
   * INFO according to the format outlined above.
   */
  public void info(String format, Object arg) {
    formatAndLog(INFO_STR, format, arg, null);
  }

  /**
   * Perform double parameter substituion before logging the message of level
   * INFO according to the format outlined above.
   */
  public void info(String format, Object arg1, Object arg2) {
    formatAndLog(INFO_STR, format, arg1, arg2);
  }

  /**
   * Log a message of level INFO, including an exception.
   */
  public void info(String msg, Throwable t) {
    log(INFO_STR, msg.toString(), t);
  }

  public void info(Marker marker, String msg) {
    info(msg);
  }

  public void info(Marker marker, String format, Object arg) {
    info(format, arg);
  }

  public void info(Marker marker, String format, Object arg1, Object arg2) {
    info(format, arg1, arg2);
  }

  public void info(Marker marker, String msg, Throwable t) {
    info(msg, t);
  }

  /**
   * Always returns true.
   */
  public boolean isWarnEnabled() {
    return true;
  }
  
  /**
   * Always returns true.
   */
  public boolean isWarnEnabled(Marker marker) {
    return true;
  }

  /**
   * A simple implementation which always logs messages of level WARN according
   * to the format outlined above.
  */
  public void warn(String msg) {
    log(WARN_STR, msg.toString(), null);
  }

  /**
   * Perform single parameter substituion before logging the message of level
   * WARN according to the format outlined above.
   */
  public void warn(String format, Object arg) {
    formatAndLog(WARN_STR, format, arg, null);
  }

  /**
   * Perform double parameter substituion before logging the message of level
   * WARN according to the format outlined above.
   */
  public void warn(String format, Object arg1, Object arg2) {
    formatAndLog(WARN_STR, format, arg1, arg2);
  }

  /**
   * Log a message of level WARN, including an exception.
   */
  public void warn(String msg, Throwable t) {
    log(WARN_STR, msg.toString(), t);
  }

  public void warn(Marker marker, String msg) {
    warn(msg);
  }

  public void warn(Marker marker, String format, Object arg) {
    warn(format, arg);
  }

  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    warn(format, arg1, arg2);
  }

  public void warn(Marker marker, String msg, Throwable t) {
    warn(msg, t);
  }

  /**
   * Always returns true.
   */
  public boolean isErrorEnabled() {
    return true;
  }

  /**
   * Always returns true.
   */
  public boolean isErrorEnabled(Marker marker) {
    return true;
  }

  /**
   * A simple implementation which always logs messages of level ERROR acoording
   * to the format outlined above.
   */
  public void error(String msg) {
    log(ERROR_STR, msg.toString(), null);
  }

  /**
   * Perform single parameter substituion before logging the message of level
   * ERROR according to the format outlined above.
   */
  public void error(String format, Object arg) {
    formatAndLog(ERROR_STR, format, arg, null);
  }

  /**
   * Perform double parameter substituion before logging the message of level
   * ERROR according to the format outlined above.
   */
  public void error(String format, Object arg1, Object arg2) {
    formatAndLog(ERROR_STR, format, arg1, arg2);
  }

  /**
   * Log a message of level ERROR, including an exception.
   */
  public void error(String msg, Throwable t) {
    log(ERROR_STR, msg.toString(), t);
  }

  public void error(Marker marker, String msg) {
    error(msg);
  }

  public void error(Marker marker, String format, Object arg) {
    error(format, arg);
  }

  public void error(Marker marker, String format, Object arg1, Object arg2) {
    error(format, arg1, arg2);
  }

  public void error(Marker marker, String msg, Throwable t) {
    error(msg, t);
  }
}
