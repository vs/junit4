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

package org.slf4j.spi;

import org.slf4j.IMarkerFactory;


/**
 * An internal interface which helps the static {@link org.slf4j.MarkerFactory} 
 * class bind with the appropriate {@link IMarkerFactory} instance. 
 * 
 * @author <a href="http://www.qos.ch/log4j/">Ceki G&uuml;lc&uuml;</a>
 */
public interface MarkerFactoryBinder {

  /**
   * Return the instance of {@link IMarkerFactory} that 
   * {@link org.slf4j.MarkerFactory} class should bind to.
   * 
   * @return the instance of {@link ILoggerFactory} that 
   * {@link org.slf4j.LoggerFactory} class should bind to.
   */
  public IMarkerFactory getMarkerFactory();

  /**
   * The String form of the {@link IMarkerFactory} object that this 
   * <code>MarkerFactoryBinder</code> instance is <em>intended</em> to return. 
   * 
   * <p>This method allows the developer to intterogate this binder's intention 
   * which may be different from the {@link IMarkerFactory} instance it is able to 
   * return. Such a discrepency should only occur in case of errors.
   * 
   * @return the class name of the intended {@link IMarkerFactory} instance
   */
  public String getMarkerFactoryClassStr();
}
