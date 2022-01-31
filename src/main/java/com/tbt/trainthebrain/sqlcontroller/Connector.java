package com.tbt.trainthebrain.sqlcontroller;

import java.sql.Connection;

public class Connector {
    private final String url;
    private final String database;
    private final String databaseType;
    private final int port;
    private final String user;
    private final String pwd;
    private final Connection connection = null;

    public Connector(String url, int port, String database, String databaseType, String user, String pwd) {
        this.url = url;
        this.database = database;
        this.databaseType = databaseType;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public Connector(){
        this.url = SQLConnectionData.getURL();
        this.database = SQLConnectionData.getDB();
        this.databaseType = SQLConnectionData.getTYPE();
        this.port = SQLConnectionData.getPORT();
        this.user = SQLConnectionData.getUSER();
        this.pwd = SQLConnectionData.getPASSWORD();
    }
}
