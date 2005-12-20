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


/**
 * Formats messages according to very simple rules. 
 * See {@link #format(String, Object)} and 
 * {@link #format(String, Object, Object)} for more details.
 *
 * @author <a href="http://www.qos.ch/log4j/">Ceki G&uuml;lc&uuml;</a>
 */
public class MessageFormatter {
  static final char DELIM_START = '{';
  static final char DELIM_STOP = '}';

  /**
   * Performs single argument substitution for the 'messagePattern' passed as
   * parameter.
   * <p>
   * For example, <code>MessageFormatter.format("Hi {}.", "there");</code> will
   * return the string "Hi there.".
   * <p>
   * The {} pair is called the formatting element. It serves to designate the
   * location where the argument needs to be inserted within the pattern.
   * 
   * @param messagePattern The message pattern which will be parsed and formatted
   * @param argument The argument to be inserted instead of the formatting element
   * @return The formatted message
   */
  public static String format(String messagePattern, Object arg) {
    return arrayFormat(messagePattern, new Object[] {arg});   
   }
  
  /**
   *
   * Performs a two argument substitution for the 'messagePattern' passed as
   * parameter.
   * <p>
   * For example, <code>MessageFormatter.format("Hi {}. My name is {}.", 
   * "there", "David");</code> will return the string "Hi there. My name is David.".
   * <p>
   * The '{}' pair is called a formatting element. It serves to designate the
   * location where the arguments need to be inserted within the message pattern.
   * 
   * @param messagePattern The message pattern which will be parsed and formatted
   * @param arg1 The first argument to replace the first formatting element
   * @param arg2 The second argument to replace the second formatting element
   * @return The formatted message
   */
  public static String format(String messagePattern, Object arg1, Object arg2) {
   return arrayFormat(messagePattern, new Object[] {arg1, arg2});   
  }
  
  public static String arrayFormat(String messagePattern, Object[] argArray) {
    if(messagePattern == null) {
      return null;
    }
    int i = 0;
    int len = messagePattern.length();
    int j = messagePattern.indexOf(DELIM_START);
    
  
    
    StringBuffer sbuf = new StringBuffer(messagePattern.length() + 50);

    for (int L = 0; L < argArray.length; L++) {
      
      char escape = 'x';
      
      j = messagePattern.indexOf(DELIM_START, i);

      if (j == -1 || (j+1 == len)) {
        // no more variables
        if (i == 0) { // this is a simple string
          return messagePattern;
        } else { // add the tail string which contains no variables and return the result.
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        }
      } else {
        char delimStop = messagePattern.charAt(j + 1);
        if (j > 0) {
          escape = messagePattern.charAt(j - 1);
        }
        
        if(escape == '\\') {
          L--; // DELIM_START was escaped, thus should not be incremented
          sbuf.append(messagePattern.substring(i, j-1));
          sbuf.append(DELIM_START);
          i = j + 1;
        } else if ((delimStop != DELIM_STOP)) {
          // invalid DELIM_START/DELIM_STOP pair
          sbuf.append(messagePattern.substring(i, messagePattern.length()));
          return sbuf.toString();
        } else {
          // normal case
          sbuf.append(messagePattern.substring(i, j));
          sbuf.append(argArray[L]);
          i = j + 2;
        }
      }
    }
    // append the characters following the second {} pair.
    sbuf.append(messagePattern.substring(i, messagePattern.length()));
    return sbuf.toString();
  }
}
