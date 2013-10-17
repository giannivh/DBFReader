/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import com.giannivanhoecke.dbfreader.domain.Constants;
import com.giannivanhoecke.dbfreader.domain.Controller;
import com.giannivanhoecke.dbfreader.domain.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 10:41
 */
public class InfoPanelContainer extends JDialog {

    public InfoPanelContainer() {

        this.setTitle( "Table info" );
        this.setModal( true );
        this.getRootPane().putClientProperty( "Window.style", "small" );

        this.setMinimumSize( Constants.MODAL_DIMENSTION );
        this.setSize( Constants.MODAL_DIMENSTION );
        this.setPreferredSize( Constants.MODAL_DIMENSTION );

        try {

            this.setIconImage( new ImageIcon(
                    ImageIO.read( getClass().getResource( "/database.png" ) ) ).getImage() );
        }
        catch( IOException e ) {

            //Ignore
        }

        this.initLayout();

        this.pack();
        this.setLocationRelativeTo( null );
        this.setVisible( true );
    }

    private void initLayout() {

        this.setLayout( new BorderLayout() );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd" );

        //Panels
        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout( new GridLayout( 0, 1 ) );
        add( labelsPanel, BorderLayout.WEST );

        JPanel valuesPanel = new JPanel();
        valuesPanel.setLayout( new GridLayout( 0, 1 ) );
        add( valuesPanel, BorderLayout.CENTER );

        //Path
        JLabel pathLabel = new JLabel( "<html><b>File</b>: " );
        pathLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( pathLabel );

        JLabel pathLabelValue = new JLabel( Controller.INSTANCE.getPath().getAbsolutePath() );
        pathLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( pathLabelValue );

        //Size
        JLabel sizeLabel = new JLabel( "<html><b>Size</b>: " );
        sizeLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( sizeLabel );

        JLabel sizeLabelValue = new JLabel( Utils.toHumanReadableByteCount( Controller.INSTANCE.getPath().length() ) );
        sizeLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( sizeLabelValue );

        //Name
        JLabel nameLabel = new JLabel( "<html><b>Name</b>: " );
        nameLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( nameLabel );

        JLabel nameLabelValue = new JLabel( Controller.INSTANCE.getTableInfo().getName() );
        nameLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( nameLabelValue );

        //Last modified date
        JLabel modifiedLabel = new JLabel( "<html><b>Modified</b>: " );
        modifiedLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( modifiedLabel );

        JLabel modifiedLabelValue = new JLabel(
                simpleDateFormat.format( Controller.INSTANCE.getTableInfo().getLastModifiedDate() ) );
        modifiedLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( modifiedLabelValue );

        //Version
        JLabel versionLabel = new JLabel( "<html><b>Version</b>: " );
        versionLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( versionLabel );

        JLabel versionLabelValue = new JLabel( Controller.INSTANCE.getTableInfo().getVersion() );
        versionLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( versionLabelValue );

        //Columns
        JLabel columnsLabel = new JLabel( "<html><b>Columns</b>: " );
        columnsLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( columnsLabel );

        JLabel columnsLabelValue = new JLabel( String.valueOf( Controller.INSTANCE.getTableInfo().getColumns() ) );
        columnsLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( columnsLabelValue );

        //Records
        JLabel recordsLabel = new JLabel( "<html><b>Records</b>: " );
        recordsLabel.setBorder( new EmptyBorder( Constants.MARGINS ) );
        labelsPanel.add( recordsLabel );

        JLabel recordsLabelValue = new JLabel( String.valueOf( Controller.INSTANCE.getTableInfo().getRecords() ) );
        recordsLabelValue.setBorder( new EmptyBorder( Constants.MARGINS ) );
        valuesPanel.add( recordsLabelValue );
    }
}
