/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

import com.giannivanhoecke.jupdatechecker.domain.Version;
import com.giannivanhoecke.jupdatechecker.exception.InvalidVersionException;

import java.util.ResourceBundle;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 18/10/13
 * Time: 11:24
 */
public class Config {

    public static String getApplicationVersion() {

        return getConfig().getString( "dbfreader.version" );
    }

    public static Version getVersion() {

        try {

            return Version.getVersion( getApplicationVersion() );
        }
        catch( InvalidVersionException e ) {

            //Should never happen though, except for when working on GIT-SNAPSHOT...
            return new Version( 0, 0 );
        }
    }

    private static ResourceBundle getConfig() {

        return ResourceBundle.getBundle( "dbfreader" );
    }
}
