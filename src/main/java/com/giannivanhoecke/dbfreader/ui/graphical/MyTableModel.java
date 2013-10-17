/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 15:03
 */
public class MyTableModel extends DefaultTableModel {

    public MyTableModel() {

        super();
    }

    public MyTableModel( int requestCounter, int requestCounter1 ) {

        super( requestCounter, requestCounter1 );
    }

    public MyTableModel( Vector vector, int requestCounter ) {

        super( vector, requestCounter );
    }

    public MyTableModel( Object[] objects, int requestCounter ) {

        super( objects, requestCounter );
    }

    public MyTableModel( Vector vector, Vector vector1 ) {

        super( vector, vector1 );
    }

    public MyTableModel( Object[][] objects, Object[] objects1 ) {

        super( objects, objects1 );
    }

    @Override
    public boolean isCellEditable( int row, int column ) {

        return false;
    }
}
