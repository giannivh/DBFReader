/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

import nl.knaw.dans.common.dbflib.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 14:25
 */
public class TableLoader implements Runnable {

    private File path;

    public TableLoader( File path ) {

        this.path = path;
    }

    @Override
    public void run() {

        Table table = new Table( path );

        try {

            table.open( IfNonExistent.ERROR );

            double recordCount = table.getRecordCount();

            if( Controller.INSTANCE.getOffset() > recordCount ) {

                Controller.INSTANCE.setOffset( Controller.INSTANCE.getOffset() - Controller.NUMBER_OF_RECORDS );
                return;
            }

            //Variables
            List<String> headerNames  = new ArrayList<String>();

            Controller.INSTANCE.clearTable();

            //Fields & Header
            List<Field> fields = table.getFields();

            for( Field field : fields ) {

                headerNames.add( field.getName() );
            }

            //Table info
            TableInfo tableInfo = new TableInfo(
                    table.getName(),
                    table.getLastModifiedDate(),
                    fields.size(),
                    table.getRecordCount(),
                    table.getVersion().name() );

            Controller.INSTANCE.setTableInfo( tableInfo );

            Controller.INSTANCE.setTableHeader( headerNames );

            //Records

            int to = Controller.INSTANCE.getOffset() + Controller.NUMBER_OF_RECORDS;
            if( to >= recordCount) {

                to = (int) recordCount;
                Controller.INSTANCE.setNext( false );
            }
            else {

                Controller.INSTANCE.setNext( true );
            }

            //Set from & to
            Controller.INSTANCE.setShowingFrom( Controller.INSTANCE.getOffset() );
            Controller.INSTANCE.setShowingTo( to );
            Controller.INSTANCE.indexChanged();

            int count = 0;
            int total = to - Controller.INSTANCE.getOffset();

            for( int j = Controller.INSTANCE.getOffset(); j < to; j++ ) {

                Controller.INSTANCE.setStatusText( String.format( "Loading dBASE %s (%d/%d)",
                        path.getName(),
                        (j + 1),
                        to ) );

                Record record = table.getRecordAt( j );
                List<String> recordValues = new ArrayList<String>();

                for( Field field : fields ) {

                    byte[] rawValue = record.getRawValue( field );

                    recordValues.add( rawValue == null ? "<NULL>" : new String( rawValue ).trim() );
                }

                Controller.INSTANCE.addRecord( recordValues );

                Controller.INSTANCE.setProgress( (int) Math.floor( ((double)count++ / (double)total) * 100.0 ) );
            }
        }
        catch( CorruptedTableException e ) {

            Controller.INSTANCE.setTableInfo( null );
            Controller.INSTANCE.setPath( null );
            Controller.INSTANCE.errorOccurred( e );
        }
        catch( IOException e ) {

            Controller.INSTANCE.setTableInfo( null );
            Controller.INSTANCE.setPath( null );
            Controller.INSTANCE.errorOccurred( e );
        }
        catch( DbfLibException e ) {

            Controller.INSTANCE.setTableInfo( null );
            Controller.INSTANCE.setPath( null );
            Controller.INSTANCE.errorOccurred( e );
        }
        finally {

            try {

                table.close();
            }
            catch( IOException e ) {

                Controller.INSTANCE.errorOccurred( e );
            }
            finally {

                //Done
                Controller.INSTANCE.setStatusText( "Ready" );
                Controller.INSTANCE.setProgress( 100 );
                Controller.INSTANCE.setUIEnabled( true );
            }
        }
    }
}
