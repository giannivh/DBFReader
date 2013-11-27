/*
 * SafeOnline project.
 *
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 * Lin.k N.V. proprietary/confidential. Use is subject to license terms.
 */

package com.giannivanhoecke.dbfreader.domain;

import java.nio.CharBuffer;
import java.nio.charset.*;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 27/11/13
 * Time: 13:09
 */
public class UTF8Util {

    public static Charset charset = Charset.forName( "UTF-8" );
    public static CharsetEncoder encoder = charset.newEncoder();
    public static CharsetDecoder decoder = charset.newDecoder();

    static {

        decoder.onMalformedInput( CodingErrorAction.IGNORE);
        decoder.onUnmappableCharacter( CodingErrorAction.IGNORE );
    }

    public static String utf8( byte[] input )
            throws CharacterCodingException {

        return decoder.decode( encoder.encode( CharBuffer.wrap( new String( input ) ) ) ).toString();
    }
}
