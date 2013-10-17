/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import com.giannivanhoecke.dbfreader.domain.Constants;
import com.giannivanhoecke.dbfreader.domain.Controller;
import com.giannivanhoecke.dbfreader.ui.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 15/10/13
 * Time: 13:45
 */
public class GUIPanelContainer extends JFrame implements UI {

    private ToolbarPanel toolbarPanel;
    private StatusbarPanel statusbarPanel;
    private TablePanel tablePanel;

    public GUIPanelContainer() {

        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        this.setTitle( Controller.INSTANCE.getTitle() );

        this.setMinimumSize( Constants.GUI_DIMENSTION );
        this.setSize( Constants.GUI_DIMENSTION );
        this.setPreferredSize( Constants.GUI_DIMENSTION );

        try {

            this.setIconImage( new ImageIcon(
                    ImageIO.read( getClass().getResource( "/database.png" ) ) ).getImage() );
        }
        catch( IOException e ) {

            //Ignore
        }

        this.initLayout();

        this.setLocationRelativeTo( null ); //center
        this.setVisible( true );

        this.pack();
    }

    private void initLayout() {

        this.setLayout( new BorderLayout( 0, 0 ) );

        //Toolbar
        this.toolbarPanel = new ToolbarPanel();
        add( this.toolbarPanel, BorderLayout.NORTH );

        //Status bar
        this.statusbarPanel = new StatusbarPanel();
        add( this.statusbarPanel, BorderLayout.SOUTH );

        //Table
        this.tablePanel = new TablePanel();
        add( this.tablePanel, BorderLayout.CENTER );
    }

    @Override
    public void setUIEnabled( final boolean enabled ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                toolbarPanel.setUIEnabled( enabled );
                tablePanel.setUIEnabled( enabled );

                if( enabled )
                    setCursor( Cursor.getDefaultCursor() );
                else
                    setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
            }
        } );
    }

    @Override
    public void setStatusText( final String status ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                statusbarPanel.setStatusText( status );
            }
        } );
    }

    @Override
    public void setProgress( final int progress ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                statusbarPanel.setProgress( progress );
            }
        } );
    }

    @Override
    public void errorOccurred( final Exception e ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                JOptionPane.showMessageDialog( null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
            }
        } );
    }

    @Override
    public void showMessage( final String title, final String message ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                JOptionPane.showMessageDialog( null, message, title, JOptionPane.INFORMATION_MESSAGE );
            }
        } );
    }

    @Override
    public void clearTable() {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                //Update title
                setTitle( Controller.INSTANCE.getTitle() );

                //Clear table
                tablePanel.clearTable();
            }
        } );
    }

    @Override
    public void setTableHeader( final List<String> names ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                tablePanel.setTableHeader( names );
            }
        } );
    }

    @Override
    public void addRecord( final List<String> values ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                tablePanel.addRecord( values );
            }
        } );
    }

    @Override
    public void indexChanged() {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                toolbarPanel.indexChanged();
            }
        } );
    }

    @Override
    public void setSearching( final boolean isSearching ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                tablePanel.setSearching( isSearching );
            }
        } );
    }

    @Override
    public void setSearchResults( final String results ) {

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {

                tablePanel.setSearchResults( results );
            }
        } );
    }
}
