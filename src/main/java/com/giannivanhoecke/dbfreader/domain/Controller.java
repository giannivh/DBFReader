/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

import com.giannivanhoecke.dbfreader.ui.UI;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 13:48
 */
public enum Controller implements UI {

    INSTANCE;

    public static final int    NUMBER_OF_RECORDS = 5000;
    public static final String TITLE             = "DBF Reader";

    private String title;
    private List<UI> observers;

    //DBF
    private File path;
    private int offset;
    private boolean next;
    private boolean previous;
    private int showingFrom;
    private int showingTo;
    private TableInfo tableInfo;
    private boolean searching;

    Controller() {

        this.title = TITLE;
        this.observers = Collections.synchronizedList( new ArrayList<UI>() );

        this.offset = 0;
        this.previous = false;
        this.next = false;
        this.showingFrom = 0;
        this.showingTo = 0;
        this.tableInfo = null;
        this.searching = false;
    }

    public void addObserver( UI observer ) {

        if( !this.observers.contains( observer ) )
            this.observers.add( observer );
    }

    public void removeObserver( UI observer ) {

        this.observers.remove( observer );
    }

    public void loadFile( File path ) {

        this.path = path;
        this.title = String.format( "%s (%s)", TITLE, path.getAbsolutePath() );

        this.offset = 0;
        this.previous = false;
        this.next = false;
        this.showingFrom = 0;
        this.showingTo = 0;
        this.tableInfo = null;
        this.searching = false;

        this.clearTable();

        this.loadFromOffset();
    }

    public void incrementOffset() {

        this.offset += NUMBER_OF_RECORDS;
    }

    public void decreaseOffset() {

        this.offset -= NUMBER_OF_RECORDS;

        if( this.offset < 0 ) {

            this.offset = 0;
            this.previous = false;
        }
    }

    public void loadFromOffset() {

        if( this.path == null )
            return;

        this.setSearching( false );

        this.previous = this.offset > 0;

        setUIEnabled( false );
        setStatusText( String.format( "Loading dBASE %s", path.getName() ) );
        setProgress( 0 );

        TableLoader tableLoader = new TableLoader( path );
        Thread thread = new Thread( tableLoader );
        thread.start();
    }

    public void exportToSQL( File file ) {

        SQLExporter sqlExporter = new SQLExporter( file );
        Thread thread = new Thread( sqlExporter );
        thread.start();
    }

    public void doSearch( String searchString ) {

        TableSearcher tableSearcher = new TableSearcher( searchString );
        Thread thread = new Thread( tableSearcher );
        thread.start();
    }

    public void clearSearch() {

        this.setSearching( false );
        this.loadFromOffset();
    }

    @Override
    public void setUIEnabled( boolean enabled ) {

        for( UI observer : this.observers )
            observer.setUIEnabled( enabled );
    }

    @Override
    public void setStatusText( String status ) {

        for( UI observer : this.observers )
            observer.setStatusText( status );
    }

    @Override
    public void setProgress( int progress ) {

        for( UI observer : this.observers )
            observer.setProgress( progress );
    }

    @Override
    public void errorOccurred( Exception e ) {

        for( UI observer : this.observers )
            observer.errorOccurred( e );
    }

    @Override
    public void showMessage( String title, String message ) {

        for( UI observer : this.observers )
            observer.showMessage( title, message );
    }

    @Override
    public void clearTable() {

        for( UI observer : this.observers )
            observer.clearTable();
    }

    @Override
    public void setTableHeader( List<String> names ) {

        for( UI observer : this.observers )
            observer.setTableHeader( names );
    }

    @Override
    public void addRecord( List<String> values ) {

        for( UI observer : this.observers )
            observer.addRecord( values );
    }

    @Override
    public void indexChanged() {

        for( UI observer : this.observers )
            observer.indexChanged();
    }

    @Override
    public void setSearching( boolean isSearching ) {

        this.searching = isSearching;

        for( UI observer : this.observers )
            observer.setSearching( isSearching );
    }

    @Override
    public void setSearchResults( String results ) {

        for( UI observer : this.observers )
            observer.setSearchResults( results );
    }

    public boolean hasTableSet() {

        return this.path != null;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle( String title ) {

        this.title = title;
    }

    public File getPath() {

        return path;
    }

    public void setPath( File path ) {

        this.path = path;
    }

    public int getOffset() {

        return offset;
    }

    public void setOffset( int offset ) {

        this.offset = offset;
    }

    public boolean hasNext() {

        return next;
    }

    public void setNext( boolean next ) {

        this.next = next;
    }

    public boolean hasPrevious() {

        return previous;
    }

    public void setPrevious( boolean previous ) {

        this.previous = previous;
    }

    public int getShowingTo() {

        return showingTo;
    }

    public void setShowingTo( int showingTo ) {

        this.showingTo = showingTo;
    }

    public int getShowingFrom() {

        return showingFrom;
    }

    public void setShowingFrom( int showingFrom ) {

        this.showingFrom = showingFrom;
    }

    public TableInfo getTableInfo() {

        return tableInfo;
    }

    public void setTableInfo( TableInfo tableInfo ) {

        this.tableInfo = tableInfo;
    }

    public boolean isSearching() {

        return searching;
    }
}
