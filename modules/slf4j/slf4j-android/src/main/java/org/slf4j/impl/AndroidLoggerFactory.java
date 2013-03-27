/*
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
package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * AndroidLoggerFactory is an implementation of {@link ILoggerFactory} returning
 * the appropriately named {@link AndroidLoggerFactory} instance.
 *
 * @author Andrey Korzhevskiy <a.korzhevskiy@gmail.com>
 */
public class AndroidLoggerFactory implements ILoggerFactory {
    private final ConcurrentMap<String, Logger> loggerMap;

    static final String ANONYMOUS_TAG = "null"; //taken from dalvik.system.DalvikLogging
    static final int TAG_MAX_LENGTH = 23; // tag names cannot be longer on Android platform
    // see also android/system/core/include/cutils/property.h
    // and android/frameworks/base/core/jni/android_util_Log.cpp

    public AndroidLoggerFactory() {
        loggerMap = new ConcurrentHashMap<String, Logger>();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
     */
    public Logger getLogger(String name) {
        String tag = loggerNameToTag(name); // fix for bug #173
/*
        if (name != null && !name.equals(tag)) {
            Log.i(AndroidLoggerFactory.class.getSimpleName(),
                    "Logger name '" + name + "' exceeds maximum length of " + TAG_MAX_LENGTH +
                            " characters, using '" + tag + "' instead.");
        }
*/

        Logger logger = loggerMap.get(tag);
        if (logger == null) {
            Logger newInstance = new AndroidLoggerAdapter(tag);
            Logger oldInstance = loggerMap.putIfAbsent(tag, newInstance);
            logger = oldInstance == null ? newInstance : oldInstance;
        }
        return logger;
    }

    /**
     * Returns the short logger tag (up to {@value #TAG_MAX_LENGTH} chars) for the given logger name.
     * Traditionally loggers are named by fully-qualified Java classes; this
     * method attempts to return a concise identifying part of such names.
     */
    static String loggerNameToTag(String loggerName) {
        // Anonymous logger
        if (loggerName == null) {
            return ANONYMOUS_TAG;
        }

        int length = loggerName.length();
        if (length <= TAG_MAX_LENGTH) {
            return loggerName;
        }

        int lastTokenIndex = 0;
        int lastPeriodIndex;
        StringBuilder tagName = new StringBuilder();
        while ((lastPeriodIndex = loggerName.indexOf('.', lastTokenIndex)) != -1) {
            tagName.append(loggerName.charAt(lastTokenIndex));
            // token of one character appended as is otherwise truncate it to one character
            int tokenLength = lastPeriodIndex - lastTokenIndex;
            if (tokenLength > 1) {
                tagName.append('*');
            }
            tagName.append('.');
            lastTokenIndex = lastPeriodIndex + 1;
        }
        // last token (usually class name) appended as is
        tagName.append(loggerName, lastTokenIndex, length);
        if (tagName.length() <= TAG_MAX_LENGTH) {
            return tagName.toString();
        }

        // Either we had no useful dot location at all or name still too long.
        // Take leading part and append '*' to indicate that it was truncated
        lastPeriodIndex = loggerName.lastIndexOf('.');
        return lastPeriodIndex != -1 && length - (lastPeriodIndex + 1) <= TAG_MAX_LENGTH
                ? loggerName.substring(lastPeriodIndex + 1)
                : '*' + loggerName.substring(length - TAG_MAX_LENGTH + 1);
    }
}
