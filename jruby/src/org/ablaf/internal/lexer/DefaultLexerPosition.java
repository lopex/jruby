/*
 * DefaultLexerPosition.java
 * Created on 08.02.2002, 20:46:57
 *
 * Copyright (C) 2002 Jan Arne Petersen <jpetersen@uni-bonn.de>. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by
 *        Jan Arne Petersen (jpetersen@uni-bonn.de)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "AbLaF" and "Abstract Language Framework" must not be
 *    used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact jpetersen@uni-bonn.de.
 *
 * 5. Products derived from this software may not be called
 *    "Abstract Language Framework", nor may
 *    "Abstract Language Framework" appear in their name, without prior
 *    written permission of Jan Arne Petersen.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL JAN ARNE PETERSEN OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * ====================================================================
 *
 */
package org.ablaf.internal.lexer;

import java.io.Serializable;

import org.ablaf.common.ISourcePosition;

/**
 * @author  jpetersen, ndrs
 * @version $Revision$
 */
public class DefaultLexerPosition implements ISourcePosition, Serializable {
    private final String file;
    private final short line;
    private final short column;

    // In a majority of cases we want the same values as last time,
    // so we simply cache the last value.
    // (A measurement on the test suite showed that this reduced the
    // number of instances by almost 90%).

    private static String lastFile;
    private static int lastLine;
    private static int lastColumn;
    private static DefaultLexerPosition lastInstance;

    private DefaultLexerPosition(String file, int line, int column) {
        this.file = file;
        this.line = (short) line;
        this.column = (short) column;
    }

    public static ISourcePosition getInstance(String file, int line) {
        return getInstance(file, line, 0);
    }

    public static ISourcePosition getInstance(String file, int line, int column) {
        synchronized (DefaultLexerPosition.class) {
            if (file.equals(lastFile) && line == lastLine && column == lastColumn) {
                return lastInstance;
            }
            lastFile = file;
            lastLine = line;
            lastColumn = column;
            lastInstance = new DefaultLexerPosition(file, line, column);
        }
        return lastInstance;
    }


    /**
     * @see ISourcePosition#getFile()
     */
    public String getFile() {
        return file;
    }

    /**
     * @see ISourcePosition#getLine()
     */
    public int getLine() {
        return line;
    }

    /**
     * @see ISourcePosition#getColumn()
     */
    public int getColumn() {
        return column;
    }

    public boolean equals(Object iOther) {
        if (iOther instanceof DefaultLexerPosition) {
            DefaultLexerPosition lOther = (DefaultLexerPosition) iOther;
            return file.equals(lOther.file) && line == lOther.line && column == lOther.column;
        }
        return false;
    }

    /**
     * hashcode based on the position value.
     **/
    public int hashCode() {
        return file.hashCode() ^ line ^ column;
    }

    public String toString() {
        if (column == 0) {
            return file + ":" + line;
        } else {
            return file + ":" + line + ":" + column;
        }
    }
}
