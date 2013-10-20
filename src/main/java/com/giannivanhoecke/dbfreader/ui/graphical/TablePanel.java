/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 14:58
 */
public class TablePanel extends JPanel {

    private JScrollPane scrollPane;
    private JTable table;
    private MyTableModel tableModel;
    private JLabel infoLabel;

    public TablePanel() {

        this.initLayout();
    }

    private void initLayout() {

        this.setLayout( new BorderLayout( 0, 0 ) );

        infoLabel = new JLabel( "Search results" );
        infoLabel.setHorizontalAlignment( SwingConstants.CENTER );
        infoLabel.setBorder( BorderFactory.createRaisedBevelBorder() );

        tableModel = new MyTableModel( new Vector(), new Vector() );

        table = new MyTable();
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        table.setDefaultRenderer( Object.class, new MyCellRenderer() );
        table.setModel( tableModel );

        scrollPane = new JScrollPane( table );
        scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );

        //Drag & Drop
        try {

            new MyDropTargetListener( scrollPane );
        }
        catch( IOException e ) {

            //NO D&D
        }

        add( scrollPane, BorderLayout.CENTER );
    }

    public void clearTable() {

        tableModel = new MyTableModel();
        table.setModel( tableModel );
    }

    public void setTableHeader( final java.util.List<String> names ) {

        tableModel.setColumnIdentifiers( names.toArray( new String[names.size()] ) );

        //Auto resize
        int margin = 5;
        for( int i = 0; i < table.getColumnCount(); i++ ) {

            DefaultTableColumnModel colModel  = (DefaultTableColumnModel) table.getColumnModel();
            TableColumn col = colModel.getColumn( i );

            TableCellRenderer renderer = col.getHeaderRenderer();

            if( renderer == null ) {

                renderer = table.getTableHeader().getDefaultRenderer();
            }

            Component comp = renderer.getTableCellRendererComponent( table, col.getHeaderValue(), false, false, 0, 0 );
            int width = comp.getPreferredSize().width;

            //Add margin
            width += 2 * margin;

            //Set the width
            col.setPreferredWidth( width );
        }
    }

    public void addRecord( final java.util.List<String> values ) {

        tableModel.addRow( values.toArray( new String[values.size()] ) );
    }

    public void setUIEnabled( boolean enabled ) {

        this.table.setEnabled( enabled );
    }

    public void setSearching( boolean isSearching ) {

        if( isSearching ) {

            add( infoLabel, BorderLayout.NORTH );
        }
        else {

            remove( infoLabel );
        }
    }

    public void setSearchResults( String results ) {

        this.infoLabel.setText( results );
    }
}
