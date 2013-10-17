/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import com.giannivanhoecke.dbfreader.domain.Controller;
import com.giannivanhoecke.dbfreader.domain.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 13:59
 */
public class ToolbarPanel extends JPanel {

    private JToolBar toolBar;
    private JButton openButton;
    private JButton backButton;
    private JButton nextButton;
    private JButton exitButton;
    private JLabel indexLabel;
    private JButton infoButton;
    private JButton searchButton;
    private JButton clearSearchButton;
    private JButton aboutButton;
    private JButton exportButton;

    public ToolbarPanel() {

        this.initLayout();
    }

    public void initLayout() {

        try {

            this.setLayout( new BorderLayout( 0, 0 ) );

            //Init toolbar
            toolBar = new JToolBar();
            add( toolBar, BorderLayout.CENTER );

            //Open buttons
            openButton = new JButton( "" );
            openButton.setToolTipText( "Open" );
            openButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/folder32.png" ) ) ) );
            toolBar.add( openButton );
            openButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    JFileChooser fileChooser = new JFileChooser( System.getProperty( "user.home" ) );

                    fileChooser.setDialogTitle( "Select your dBASE file" );
                    fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
                    fileChooser.setAcceptAllFileFilterUsed( false );
                    fileChooser.setFileFilter( new FileFilter() {

                        @Override
                        public boolean accept( File f ) {

                            return f.getName().toLowerCase().endsWith( ".dbf" );
                        }

                        @Override
                        public String getDescription() {

                            return "dBASE files";
                        }

                    } );

                    if( fileChooser.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION ) {

                        Controller.INSTANCE.loadFile( fileChooser.getSelectedFile() );
                    }
                }
            } );

            //Separator
            toolBar.addSeparator();

            //Back button
            backButton = new JButton( "" );
            backButton.setToolTipText( "Previous" );
            backButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/leftarrow32.png" ) ) ) );
            backButton.setEnabled( Controller.INSTANCE.hasPrevious() );
            toolBar.add( backButton );
            backButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    Controller.INSTANCE.decreaseOffset();
                    Controller.INSTANCE.loadFromOffset();
                }
            } );

            //Index label
            indexLabel = new JLabel( "" );
            indexLabel.setHorizontalAlignment( SwingConstants.CENTER );
            toolBar.add( indexLabel );

            //Next button
            nextButton = new JButton( "" );
            nextButton.setToolTipText( "Next" );
            nextButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/arrowright32.png" ) ) ) );
            nextButton.setEnabled( Controller.INSTANCE.hasNext() );
            toolBar.add( nextButton );
            nextButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    Controller.INSTANCE.incrementOffset();
                    Controller.INSTANCE.loadFromOffset();
                }
            } );

            //Separator
            toolBar.addSeparator();

            //Info button
            infoButton = new JButton( "" );
            infoButton.setToolTipText( "Table info" );
            infoButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/lightbulb32.png" ) ) ) );
            infoButton.setEnabled( Controller.INSTANCE.hasTableSet() );
            toolBar.add( infoButton );
            infoButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    SwingUtilities.invokeLater( new Runnable() {

                        @Override
                        public void run() {

                            new InfoPanelContainer();
                        }
                    } );
                }
            } );

            //Search button
            searchButton = new JButton( "" );
            searchButton.setToolTipText( "Search" );
            searchButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/search32.png" ) ) ) );
            searchButton.setEnabled( Controller.INSTANCE.hasTableSet() );
            toolBar.add( searchButton );
            searchButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    String searchString = JOptionPane.showInputDialog( null, "Please enter a keyword:", "Search",
                            JOptionPane.QUESTION_MESSAGE );

                    if( searchString != null && !searchString.trim().isEmpty() ) {

                        Controller.INSTANCE.doSearch( searchString );
                    }
                }
            } );

            //Clear search button
            clearSearchButton = new JButton( "" );
            clearSearchButton.setToolTipText( "Clear search results" );
            clearSearchButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/block32.png" ) ) ) );
            clearSearchButton.setEnabled( Controller.INSTANCE.isSearching() );
            toolBar.add( clearSearchButton );
            clearSearchButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    Controller.INSTANCE.clearSearch();
                }
            } );

            //Separator
            toolBar.addSeparator();

            //Export button
            exportButton = new JButton( "" );
            exportButton.setToolTipText( "Export table to SQL" );
            exportButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/boxupload32.png" ) ) ) );
            exportButton.setEnabled( Controller.INSTANCE.hasTableSet() );
            toolBar.add( exportButton );
            exportButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    JFileChooser fileChooser = new JFileChooser( System.getProperty( "user.home" ) );

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyyMMdd_HHmmss" );
                    fileChooser.setSelectedFile( new File(
                            String.format( "%s_%s.sql",
                                    simpleDateFormat.format( new Date() ),
                                    Utils.cleanFileName( Controller.INSTANCE.getTableInfo().getName() ) )
                    ) );

                    fileChooser.setDialogTitle( "Export to..." );
                    fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
                    fileChooser.setAcceptAllFileFilterUsed( false );
                    fileChooser.setFileFilter( new FileFilter() {

                        @Override
                        public boolean accept( File f ) {

                            return f.getName().toLowerCase().endsWith( ".sql" );
                        }

                        @Override
                        public String getDescription() {

                            return "SQL files";
                        }

                    });

                    if( fileChooser.showSaveDialog( null ) == JFileChooser.APPROVE_OPTION ) {

                        //OK
                        Controller.INSTANCE.exportToSQL( fileChooser.getSelectedFile() );
                    }
                }
            } );

            //Separator
            toolBar.addSeparator();

            //About button
            aboutButton = new JButton( "" );
            aboutButton.setToolTipText( "About" );
            aboutButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/questionbook32.png" ) ) ) );
            toolBar.add( aboutButton );
            aboutButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    SwingUtilities.invokeLater( new Runnable() {

                        @Override
                        public void run() {

                            new AboutPanelContainer();
                        }
                    } );
                }
            } );

            //Exit buttons
            exitButton = new JButton( "" );
            exitButton.setToolTipText( "Exit" );
            exitButton.setIcon(
                    new ImageIcon(
                            ImageIO.read( getClass().getResource( "/mono_icons/stop32.png" ) ) ) );
            toolBar.add( exitButton );
            exitButton.addActionListener( new ActionListener() {

                public void actionPerformed( ActionEvent event ) {

                    System.exit( 0 );
                }
            } );
        }
        catch( IOException e ) {

            e.printStackTrace();
            System.exit( 1 );
        }
    }

    public void setUIEnabled( boolean enabled ) {

        this.openButton.setEnabled( enabled );

        backButton.setEnabled( enabled && !Controller.INSTANCE.isSearching() &&Controller.INSTANCE.hasPrevious() );
        nextButton.setEnabled( enabled && !Controller.INSTANCE.isSearching() && Controller.INSTANCE.hasNext() );
        infoButton.setEnabled( enabled && Controller.INSTANCE.hasTableSet() );
        searchButton.setEnabled( enabled && Controller.INSTANCE.hasTableSet() );
        clearSearchButton.setEnabled( enabled && Controller.INSTANCE.isSearching() &&
                Controller.INSTANCE.hasTableSet() );
        exportButton.setEnabled( enabled && !Controller.INSTANCE.isSearching() && Controller.INSTANCE.hasTableSet() );

        indexLabel.setEnabled( !Controller.INSTANCE.isSearching() );
    }

    public void indexChanged() {

        if( Controller.INSTANCE.isSearching() ) {

            this.indexLabel.setText( "Showing search results" );
        }
        else {

            this.indexLabel.setText(
                String.format( "Showing records %d to %d (%d records)",
                        Controller.INSTANCE.getShowingFrom(),
                        Controller.INSTANCE.getShowingTo(),
                        Controller.INSTANCE.getTableInfo().getRecords() ) );
        }
    }
}
