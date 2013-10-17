/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 16:30
 */
public class MyCellRenderer extends DefaultTableCellRenderer {

    public MyCellRenderer() {

        super();
    }

    @Override
    public Component getTableCellRendererComponent( JTable tab, Object val, boolean isSelected,
                                                    boolean hasFocus, int row, int col ) {

        setEnabled( tab.isEnabled() );

        return super.getTableCellRendererComponent( tab, val, isSelected, hasFocus, row, col );
    }
}
