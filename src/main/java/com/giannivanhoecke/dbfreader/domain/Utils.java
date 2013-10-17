/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 11:20
 */
public class Utils {

    public static final String NEW_LINE = System.getProperty( "line.separator" );

    public static String toHumanReadableByteCount( long size ) {

        int unit = 1024;

        if( size < unit )
            return size + " B";

        int exp = (int) (Math.log( size ) / Math.log( unit ));

        String pre = "KMGTPE".charAt( exp - 1 ) + "i";

        return String.format( "%.1f %sB", size / Math.pow( unit, exp ), pre );
    }

    public static String cleanFileName( String name ) {

        return name.substring( 0, name.lastIndexOf( "." ) ).replaceAll( "[^a-zA-Z0-9]", "" );
    }

    public static String cleanString( String string ) {

        return string.replace( "'", "\\'" );
    }
}
