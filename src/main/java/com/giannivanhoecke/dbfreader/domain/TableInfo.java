/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.giannivanhoecke.dbfreader.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 11:37
 */
public class TableInfo implements Serializable {

    private String name;
    private Date lastModifiedDate;
    private int columns;
    private int records;
    private String version;

    public TableInfo( String name, Date lastModifiedDate, int columns, int records, String version ) {

        this.name = name;
        this.lastModifiedDate = lastModifiedDate;
        this.columns = columns;
        this.records = records;
        this.version = version;
    }

    public String getName() {

        return name;
    }

    public void setName( String name ) {

        this.name = name;
    }

    public Date getLastModifiedDate() {

        return lastModifiedDate;
    }

    public void setLastModifiedDate( Date lastModifiedDate ) {

        this.lastModifiedDate = lastModifiedDate;
    }

    public int getColumns() {

        return columns;
    }

    public void setColumns( int columns ) {

        this.columns = columns;
    }

    public int getRecords() {

        return records;
    }

    public void setRecords( int records ) {

        this.records = records;
    }

    public String getVersion() {

        return version;
    }

    public void setVersion( String version ) {

        this.version = version;
    }
}
