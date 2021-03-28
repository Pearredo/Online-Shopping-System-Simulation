package BankingSystem.Data;

import java.io.File;
import java.util.Scanner;

public class DataManager {
    private static String dataBaseDirectory;
    public static String getDataBaseDirectory() { return dataBaseDirectory; }
    public static void setDataBaseDirectory(String directory) { dataBaseDirectory = directory; }
    public static void create(DataObject data) {
        System.out.print(data.getDataType());
        // create create request containing bytearray of data
    }
    public static void read(DataObject data) {
        System.out.print(data.getDataType());
        // create read request containing bytearray of data
    }
    public static void update(DataObject data) {
        System.out.print(data.getDataType());
        // create update request containing bytearray of data
    }
    public static void delete(DataObject data) {
        System.out.print(data.getDataType());
        // create delete request containing bytearray of data
    }
}
