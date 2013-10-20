/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Vector;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 18/10/13
 * Time: 13:57
 */
public class MyTable extends JTable {

    public static final int MAX_COLUMN_WIDTH = 300;
    public static final int COLUMN_SPACING   = 10;

    private java.awt.Color rowColors[] = new java.awt.Color[2];
    private boolean drawStripes = false;

    public MyTable() {

        super();
    }

    public MyTable( TableModel tableModel ) {

        super( tableModel );
    }

    public MyTable( TableModel tableModel, TableColumnModel tableColumnModel ) {

        super( tableModel, tableColumnModel );
    }

    public MyTable( TableModel tableModel, TableColumnModel tableColumnModel, ListSelectionModel listSelectionModel ) {

        super( tableModel, tableColumnModel, listSelectionModel );
    }

    public MyTable( int requestCounter, int requestCounter1 ) {

        super( requestCounter, requestCounter1 );
    }

    public MyTable( Vector vector, Vector vector1 ) {

        super( vector, vector1 );
    }

    public MyTable( Object[][] objects, Object[] objects1 ) {

        super( objects, objects1 );
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {

        return getPreferredSize().width < getParent().getWidth();
    }

    @Override
    public void doLayout() {

        TableColumn resizingColumn = null;

        if( tableHeader != null ) {

            resizingColumn = tableHeader.getResizingColumn();
        }

        if( resizingColumn == null ) {

            //Viewport size changed. May need to increase columns widths
            setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            super.doLayout();
        }
        else {

            //Specific column resized. Reset preferred widths
            TableColumnModel tcm = getColumnModel();

            for( int i = 0; i < tcm.getColumnCount(); i++ ) {

                TableColumn tc = tcm.getColumn(i);
                tc.setPreferredWidth( tc.getWidth() );
            }

            //Columns don't fill the viewport, invoke default layout
            if( tcm.getTotalColumnWidth() < getParent().getWidth() ) {

                setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            }

            super.doLayout();
        }

        setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    }

    /**
     * Add stripes between cells and behind non-opaque cells.
     */
    public void paintComponent( Graphics g ) {

        if( !( drawStripes = isOpaque() ) ) {

            super.paintComponent( g );
            return;
        }

        //Paint striped background stripes
        updateStripedColors();

        final Insets insets = getInsets();
        final int w = getWidth() - insets.left - insets.right;
        final int h = getHeight() - insets.top - insets.bottom;
        final int x = insets.left;
        int y = insets.top;
        int rowHeight = 16; //A default for empty tables
        final int nItems = getRowCount();

        for( int i = 0; i < nItems; i++, y += rowHeight ) {

            rowHeight = getRowHeight( i );
            g.setColor( rowColors[i & 1] );
            g.fillRect( x, y, w, rowHeight );
        }

        //Use last row height for remainder of table area
        final int nRows = nItems + ( insets.top + h - y ) / rowHeight;

        for( int i = nItems; i < nRows; i++, y += rowHeight ) {

            g.setColor( rowColors[i & 1] );
            g.fillRect( x, y, w, rowHeight );
        }

        final int remainder = insets.top + h - y;

        if( remainder > 0 ) {

            g.setColor( rowColors[nRows & 1] );
            g.fillRect( x, y, w, remainder );
        }

        //Paint component
        setOpaque( false );
        super.paintComponent( g );
        setOpaque( true );
    }

    /**
     * Add background stripes behind rendered cells.
     */
    public Component prepareRenderer( TableCellRenderer renderer, int row, int col ) {

        final Component component = super.prepareRenderer( renderer, row, col );

        if( drawStripes && !isCellSelected( row, col ) ) {

            component.setBackground( rowColors[row & 1] );
        }

        //Width
        int rendererWidth = component.getPreferredSize().width + COLUMN_SPACING;
        TableColumn tableColumn = getColumnModel().getColumn( col );
        rendererWidth = Math.max( rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth() );

        if( rendererWidth > MAX_COLUMN_WIDTH ) {

            rendererWidth = MAX_COLUMN_WIDTH;
        }

        tableColumn.setPreferredWidth( rendererWidth );

        return component;
    }

    /**
     * Add background stripes behind edited cells.
     */
    public Component prepareEditor( TableCellEditor editor, int row, int col ) {

        final Component c = super.prepareEditor( editor, row, col );

        if( drawStripes && !isCellSelected( row, col ) ) {

            c.setBackground( rowColors[row & 1] );
        }

        return c;
    }

    /**
     * Force the table to fill the viewport's height.
     */
    public boolean getScrollableTracksViewportHeight() {

        final Component p = getParent();

        return p instanceof JViewport && p.getHeight() > getPreferredSize().height;
    }

    /**
     * Compute striped background colors.
     */
    private void updateStripedColors() {

        if( ( rowColors[0] = getBackground() ) == null ) {

            rowColors[0] = rowColors[1] = Color.white;
            return;
        }

        final Color sel = getSelectionBackground();

        if( sel == null ) {

            rowColors[1] = rowColors[0];
            return;
        }

        final float[] bgHSB = Color.RGBtoHSB(
                rowColors[0].getRed(), rowColors[0].getGreen(),
                rowColors[0].getBlue(), null );

        final float[] selHSB = Color.RGBtoHSB(
                sel.getRed(), sel.getGreen(), sel.getBlue(), null );

        rowColors[1] = Color.getHSBColor(
                ( selHSB[1] == 0.0 || selHSB[2] == 0.0 ) ? bgHSB[0] : selHSB[0],
                0.1f * selHSB[1] + 0.9f * bgHSB[1],
                bgHSB[2] + ( ( bgHSB[2] < 0.5f ) ? 0.05f : -0.05f ) );
    }
}
