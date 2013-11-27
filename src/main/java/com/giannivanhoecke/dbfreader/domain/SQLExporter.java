/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

import nl.knaw.dans.common.dbflib.Field;
import nl.knaw.dans.common.dbflib.IfNonExistent;
import nl.knaw.dans.common.dbflib.Record;
import nl.knaw.dans.common.dbflib.Table;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 14:05
 */
public class SQLExporter implements Runnable {

    private File target;

    public SQLExporter( File target ) {

        this.target = target;
    }

    @Override
    public void run() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

        try {

            Controller.INSTANCE.setUIEnabled( false );

            //Open target for writing
            PrintWriter printWriter = new PrintWriter( new FileWriter( this.target ) );

            //Open dBASE
            Table table = new Table( Controller.INSTANCE.getPath() );
            table.open( IfNonExistent.ERROR );

            //Set variables
            Controller.INSTANCE.setProgress( 0 );
            Controller.INSTANCE.setStatusText( String.format( "Exporting %s to SQL...", table.getName() ) );

            String tableName = Utils.cleanFileName( table.getName() );
            double recordCount = table.getRecordCount();
            List<Field> fields = table.getFields();
            StringBuilder stringBuilder = new StringBuilder();

            //Info
            stringBuilder.append( "# ************************************************************" ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# " + Controller.TITLE ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# Version " ).append( Config.getApplicationVersion() ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# By Gianni Van Hoecke <gianni.vh@gmail.com>" ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# " ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# Database: " ).append( tableName ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# Generation Time: " ).append( simpleDateFormat.format( new Date() ) ).
                    append( Utils.NEW_LINE );
            stringBuilder.append( "# ************************************************************" ).
                    append( Utils.NEW_LINE );

            //Add drop statement
            stringBuilder.append( String.format( "DROP TABLE IF EXISTS `%s`;", tableName ) );
            stringBuilder.append( Utils.NEW_LINE ).append( Utils.NEW_LINE );

            //Add create statement
            stringBuilder.append( String.format( "CREATE TABLE `%s` (", tableName ) ).append( Utils.NEW_LINE );
            for( int i = 0; i < fields.size(); i++ ) {

                Field field = fields.get( i );
                boolean hasNext = i + 1 < fields.size();

                switch( field.getType() ) {

                    case CHARACTER:
                    case GENERAL:
                    default:

                        stringBuilder.append(
                                String.format( "  `%s` varchar(255) DEFAULT NULL", field.getName() ) );

                        break;

                    case BINARY:
                    case MEMO:
                    case PICTURE:

                        stringBuilder.append(
                                String.format( "  `%s` blob DEFAULT NULL", field.getName() ) );

                        break;

                    case DATE:

                        stringBuilder.append(
                                String.format( "  `%s` datetime DEFAULT NULL", field.getName() ) );

                        break;

                    case FLOAT:

                        stringBuilder.append(
                                String.format( "  `%s` decimal(19,2) DEFAULT NULL", field.getName() ) );

                        break;

                    case LOGICAL:

                        stringBuilder.append(
                                String.format( "  `%s` bit(1) DEFAULT NULL", field.getName() ) );

                        break;

                    case NUMBER:

                        stringBuilder.append(
                                String.format( "  `%s` bigint(20) unsigned DEFAULT NULL", field.getName() ) );

                        break;
                }

                stringBuilder.append( String.format( "%s", hasNext ? ", " : "" ) ).append( Utils.NEW_LINE );
            }
            stringBuilder.append( ") ENGINE=InnoDB DEFAULT CHARSET=utf8;" );
            stringBuilder.append( Utils.NEW_LINE ).append( Utils.NEW_LINE );
            printWriter.println( stringBuilder.toString() );
            stringBuilder = new StringBuilder();

            //Add insert statement
            StringBuilder insertBuilder = new StringBuilder();
            insertBuilder.append( String.format( "INSERT INTO `%s` (", tableName ) );
            for( int i = 0; i < fields.size(); i++ ) {

                Field field = fields.get( i );
                boolean hasNext = i + 1 < fields.size();

                insertBuilder.append( String.format( "`%s`%s", field.getName(), hasNext ? ", " : ")" ) );
            }
            insertBuilder.append( Utils.NEW_LINE );
            insertBuilder.append( "VALUES" );

            //Add values
            int count;
            for( count = 0; count < table.getRecordCount(); count++ ) {

                if( count == 0 || count % 100 == 0 ) {

                    printWriter.println( insertBuilder.toString() );
                }

                Controller.INSTANCE.setStatusText( String.format( "Exporting dBASE %s to SQL... (%d/%d)",
                        table.getName(),
                        count + 1,
                        (int) recordCount ) );

                Record record = table.getRecordAt( count );
                StringBuilder value = new StringBuilder();

                value.append( "(" );

                for( int i = 0; i < fields.size(); i++ ) {

                    Field field = fields.get( i );
                    boolean hasNext = i + 1 < fields.size();

                    if( new String( record.getRawValue( field ) ) == null ) {

                        value.append( "NULL" );
                    }
                    else {

                        switch( field.getType() ) {

                            case CHARACTER:
                            case GENERAL:
                            default:

                                value.append(
                                        String.format( "'%s'",
                                                Utils.cleanString( record.getRawValue( field ) ) ) );

                                break;

                            case BINARY:
                            case MEMO:
                            case PICTURE:

                                value.append(
                                        String.format( "'%s'",
                                                Utils.cleanString( new String( record.getRawValue( field ) ) ) ) );

                                break;

                            case DATE:

                                if( record.getDateValue( field.getName() ) == null ) {

                                    value.append( "NULL" );
                                }
                                else {

                                    value.append(
                                        String.format( "'%s'",
                                                simpleDateFormat.format( record.getDateValue( field.getName() ) ) ) );
                                }

                                break;

                            case FLOAT:

                                if( record.getNumberValue( field.getName() ) == null ) {

                                    value.append( "NULL" );
                                }
                                else {

                                    value.append(
                                        String.format( "%.2f",
                                                record.getNumberValue( field.getName() ).doubleValue() ) );
                                }

                                break;

                            case LOGICAL:

                                if( record.getBooleanValue( field.getName() ) == null ) {

                                    value.append( "NULL" );
                                }
                                else {

                                    value.append(
                                        String.format( "%s",
                                                record.getBooleanValue( field.getName() ) ? "00000001" : "00000000" ) );
                                }

                                break;

                            case NUMBER:

                                if( record.getNumberValue( field.getName() ) == null ) {

                                    value.append( "NULL" );
                                }
                                else {

                                    value.append(
                                        String.format( "%d",
                                                record.getNumberValue( field.getName() ).longValue() ) );
                                }

                                break;
                        }
                    }

                    value.append( String.format( "%s", hasNext ? ", " : ")" ) );
                }

                boolean isEndOfStatement = ( (count + 1) >= table.getRecordCount() ) ||
                        ( (count + 1) % 100 == 0 );

                value.append( String.format( "%s", isEndOfStatement ? ";" : "," ) );

                printWriter.println( value.toString() );

                if( isEndOfStatement )
                    printWriter.println();

                Controller.INSTANCE.setProgress( (int) Math.floor( ((double)count / recordCount) * 100.0 ) );
            }

            //Done
            printWriter.close();
            table.close();

            Controller.INSTANCE.showMessage( "Completed", "Export to SQL completed!" );
        }
        catch( Exception e ) {

            e.printStackTrace();
            Controller.INSTANCE.errorOccurred( e );
        }
        finally {

            Controller.INSTANCE.setUIEnabled( true );
            Controller.INSTANCE.setStatusText( "Ready" );
            Controller.INSTANCE.setProgress( 100 );
        }
    }
}
