/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import javax.swing.*;
import java.awt.*;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 14:26
 */
public class StatusbarPanel extends JPanel {

    private JProgressBar progressBar;

    public StatusbarPanel() {

        this.initLayout();
    }

    private void initLayout() {

        this.setLayout( new BorderLayout( 0, 0 ) );
        this.setBorder( BorderFactory.createRaisedBevelBorder() );

        //Progress bar
        this.progressBar = new JProgressBar();
        add( this.progressBar, BorderLayout.CENTER );
        this.progressBar.setStringPainted( true );

        //Standard values
        this.setStatusText( "Ready" );
        this.setProgress( 100 );
    }

    public void setStatusText( String status ) {

        this.progressBar.setString( status );
    }

    public void setProgress( int progress ) {

        this.progressBar.setValue( progress );
    }
}
