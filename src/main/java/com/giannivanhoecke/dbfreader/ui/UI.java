/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui;

import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 13:44
 */
public interface UI {

    public void setUIEnabled( boolean enabled );
    public void setStatusText( String status );
    public void setProgress( int progress );
    public void errorOccurred( Exception e );
    public void showMessage( String title, String message );

    public void clearTable();
    public void setTableHeader( List<String> names );
    public void addRecord( List<String> values );
    public void indexChanged();
    public void setSearching( boolean isSearching );
    public void setSearchResults( String results );
}
