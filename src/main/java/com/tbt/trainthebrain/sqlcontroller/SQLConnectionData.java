package com.tbt.trainthebrain.sqlcontroller;

public class SQLConnectionData {
    private final static String USER = "trainthebrain";
    private final static String PASSWORD = "trainthebrain";
    private final static String DATABASE = "trainthebrain";
    private final static String HOST = "localhost";
    private final static String DATABASETYPE = "mariadb";
    private final static int PORT = 3306;

    public static String getURL(){ return "jdbc:"+DATABASETYPE+"://"+HOST+":"+PORT+"/"+DATABASE; }

    public static String getUSER() {
        return USER;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static String getDB(){
        return DATABASE;
    }

    public static String getTYPE(){
        return DATABASETYPE;
    }

    public static int getPORT(){
        return PORT;
    }


}
