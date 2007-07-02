/* 
 * Copyright (c) 2004-2007 QOS.ch
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
 */

package org.slf4j;

import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMDCBinder;
import org.slf4j.spi.MDCAdapter;

/**
 * MDC class serves as an abstraction of the underlying logging system's 
 * MDC implementation. At this time, only log4j and logback offer MDC
 * functionality. For other systems, this SLF4J defaults to nop (empty)
 * implementation.
 * 
 * <p>Please note that all methods in this class are static.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MDC {
  static MDCAdapter mdcAdapter;

  private MDC() {
  }

  static {
    try {
      mdcAdapter = StaticMDCBinder.SINGLETON.getMDCA();
    } catch (Exception e) {
      // we should never get here
      Util.reportFailure("Could not instantiate instance of class ["
          + StaticMDCBinder.SINGLETON.getMDCAdapterClassStr() + "]", e);
    }
  }

  
  /**
   * Put a context value (the <code>val</code> parameter) as identified with
   * the <code>key</code> parameter into the current thread's context map. This 
   * method delegates all  work to the MDC of the underlying logging system.
   */
  public static void put(String key, String val) {
    mdcAdapter.put(key, val);
  }

  /**
   * Get the context identified by the <code>key</code> parameter. 
   * This method delegates all  work to the MDC of the underlying logging system.
   * 
   * @return the string value identified by the <code>key</code> parameter.
   */
  public static String get(String key) {
    return mdcAdapter.get(key);
  }

  /**
   * Remove the the context identified by the <code>key</code> parameter using 
   * the underlying system's MDC implementation.
   */
  public static void remove(String key) {
    mdcAdapter.remove(key);
  }

  /**
   * Clear all entries in the MDC of the underlying implementation.
   */
  public void clear() {
    mdcAdapter.clear();
  }
  
 
}