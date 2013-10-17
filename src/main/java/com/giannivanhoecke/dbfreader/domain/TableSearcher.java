/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

import nl.knaw.dans.common.dbflib.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 17/10/13
 * Time: 15:39
 */
public class TableSearcher implements Runnable {

    private String searchString;

    public TableSearcher( String searchString ) {

        this.searchString = searchString;
    }

    @Override
    public void run() {

        Table table = new Table( Controller.INSTANCE.getPath() );

        try {

            Controller.INSTANCE.setUIEnabled( false );
            Controller.INSTANCE.setStatusText( "Searching..." );
            Controller.INSTANCE.setProgress( 0 );
            Controller.INSTANCE.setSearching( true );
            Controller.INSTANCE.setSearchResults( "Search results for \"" + searchString + "\"" );
            Controller.INSTANCE.clearTable();
            Controller.INSTANCE.indexChanged();

            table.open( IfNonExistent.ERROR );

            double recordCount = table.getRecordCount();

            //Variables
            List<String> headerNames  = new ArrayList<String>();

            Controller.INSTANCE.clearTable();

            //Fields & Header
            List<Field> fields = table.getFields();

            for( Field field : fields ) {

                headerNames.add( field.getName() );
            }

            Controller.INSTANCE.setTableHeader( headerNames );

            //Records
            int count;
            int hitCount = 0;
            for( count = 0; count < table.getRecordCount(); count++ ) {

                Controller.INSTANCE.setStatusText( String.format( "Searching... (%d/%d)",
                        count + 1,
                        (int) recordCount ) );

                Record record = table.getRecordAt( count );
                List<String> recordValues = new ArrayList<String>();
                boolean hasHit = false;

                for( Field field : fields ) {

                    byte[] rawValue = record.getRawValue( field );

                    if( rawValue != null ) {

                        String value = new String( rawValue ).trim();

                        if( value.toLowerCase().contains( searchString.toLowerCase() ) ) {

                            hasHit = true;
                            hitCount++;
                        }
                    }

                    recordValues.add( rawValue == null ? "<NULL>" : new String( rawValue ).trim() );
                }

                if( hasHit ) {

                    Controller.INSTANCE.addRecord( recordValues );
                    Controller.INSTANCE.setSearchResults( String.format( "Search results for \"%s\" " +
                            "(%d hit%s)", searchString, hitCount, hitCount == 1 ? "" : "s" ) );
                }

                Controller.INSTANCE.setProgress( (int) Math.floor( ((double)count / recordCount) * 100.0 ) );
            }

            //Done
            table.close();
        }
        catch( CorruptedTableException e ) {

            Controller.INSTANCE.errorOccurred( e );
        }
        catch( IOException e ) {

            Controller.INSTANCE.errorOccurred( e );
        }
        catch( DbfLibException e ) {

            Controller.INSTANCE.errorOccurred( e );
        }
        finally {

            Controller.INSTANCE.setUIEnabled( true );
            Controller.INSTANCE.setStatusText( "Ready" );
            Controller.INSTANCE.setProgress( 100 );
        }
    }
}
