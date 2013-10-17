/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.ui.graphical;

import com.giannivanhoecke.dbfreader.domain.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 17/10/13
 * Time: 14:31
 */
public class MyDropTargetListener implements DropTargetListener {

    private Component component;
    private Color originalColor;
    private Cursor originalCursor;

    public MyDropTargetListener( Component component )
            throws IOException {

        this.component = component;
        this.originalColor = component.getBackground();
        this.originalCursor = component.getCursor();

        new DropTarget( component, this );
    }

    private void setDragCursor( boolean isDragging ) {

        try {

            if( isDragging ) {

                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Image image = new ImageIcon(
                        ImageIO.read( getClass().getResource( "/mono_icons/folder32.png" ) ) ).getImage();
                Point hotSpot = new Point( 0, 0 );

                Cursor cursor = toolkit.createCustomCursor( image, hotSpot, "Open" );
                this.component.setCursor( cursor );
            }
            else {

                this.component.setCursor( this.originalCursor );
            }
        }
        catch( IOException e ) {

            e.printStackTrace();
        }
    }

    @Override
    public void dragEnter( DropTargetDragEvent dropTargetDragEvent ) {

        this.component.setBackground( Color.BLUE );
        this.setDragCursor( true );
    }

    @Override
    public void dragOver( DropTargetDragEvent dropTargetDragEvent ) {

        //Nothing
    }

    @Override
    public void dropActionChanged( DropTargetDragEvent dropTargetDragEvent ) {

        //Nothing
    }

    @Override
    public void dragExit( DropTargetEvent dropTargetEvent ) {

        this.component.setBackground( this.originalColor );
        this.setDragCursor( false );
    }

    @Override
    public void drop( DropTargetDropEvent dropTargetDropEvent ) {

        //Accept copy drops
        dropTargetDropEvent.acceptDrop( DnDConstants.ACTION_COPY );

        //Get the transfer which can provide the dropped item data
        Transferable transferable = dropTargetDropEvent.getTransferable();

        //Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();

        //Loop through the flavors
        for( DataFlavor flavor : flavors ) {

            try {

                //If the drop items are files
                if( flavor.isFlavorJavaFileListType() ) {

                    //Get all of the dropped files
                    List<File> files = (List<File>) transferable.getTransferData( flavor );

                    //Only open one file, so get the first, if any
                    if( !files.isEmpty() ) {

                        File file = files.get( 0 );

                        //Send to controller to open it.
                        Controller.INSTANCE.loadFile( file );
                    }
                }
            }
            catch( Exception e ) {

                //Print out the error stack
                e.printStackTrace();
            }
        }

        //Inform that the drop is complete
        dropTargetDropEvent.dropComplete( true );

        this.component.setBackground( this.originalColor );
        this.setDragCursor( false );
    }
}
