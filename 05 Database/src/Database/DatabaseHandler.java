package Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;

// Some references used for the design of the database. There's a lot of nuance that wasn't 
// implemented in the end, especiallly around effective seraching
//https://dzone.com/articles/how-three-fundamental-data-structures-impact-stora
//https://www.tutorialcup.com/dbms/selection-algorithm.htm
//https://hpi.de/fileadmin/user_upload/fachgebiete/plattner/teaching/DataStructures/2011/In-Memory_Data_Structures.pdf

// Database handler uses the singleton pattern to ensure that references to it are all to the
// same object. It acts as the handler that loads its own state (a list of available databases)
// from file and is then able to load other databases from and save them to file
public class DatabaseHandler {
    private static DatabaseHandler dbHandler;

    private SimpleEntry<Database, File> activeDatabase;

    static private HashMap<String, File> databases;

    String dbFolderPath = ".." + File.separator + "database" + File.separator;

    File dbHandlerFile = new File(dbFolderPath + "info.storage");

    private DatabaseHandler() {
        if (dbHandlerFile.exists()) {
            System.out.println("Loading DB file");
            databases = (HashMap<String, File>) readSerializedObject(dbHandlerFile);
        } else {
            try {
                databases = new HashMap<>();

                dbHandlerFile.getParentFile().mkdirs();
                dbHandlerFile.createNewFile();

                writeSerializedObject(dbHandlerFile, databases);
                System.out.println("DB Storage created");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }

        }

    }

    public static DatabaseHandler getInstance() {
        if (dbHandler == null) {
            dbHandler = new DatabaseHandler();
        }
        System.out.println("DATABASES: " + databases);
        return dbHandler;
    }

    public void createDatabase(String databaseName) throws RuntimeException {
        if (checkDatabaseExists(databaseName)) {
            throw new RuntimeException("ERROR: Database " + databaseName + " already exists.");
        }

        Database database = new Database();
        File databaseFile = new File(dbFolderPath + databaseName + ".db");

        databases.put(databaseName, databaseFile);

        writeSerializedObject(dbHandlerFile, databases);
        writeSerializedObject(databaseFile, database);
    }

    public Database useDatabase(String databaseName) throws RuntimeException {
        if (!checkDatabaseExists(databaseName)) {
            throw new RuntimeException("ERROR: Unknown database " + databaseName + ".");
        }

        File databaseFile = databases.get(databaseName);
        Database database = (Database) readSerializedObject(databaseFile);
        activeDatabase = new SimpleEntry<Database, File>(database, databaseFile);

        return database;
    }

    public Database dropDatabase(String databaseName) throws RuntimeException {
        if (!checkDatabaseExists(databaseName)) {
            throw new RuntimeException("ERROR: Unknown database " + databaseName + ".");
        }

        File databaseFile = databases.remove(databaseName);
        // TODO error checking on delete?
        databaseFile.delete();

        writeSerializedObject(dbHandlerFile, databases);

        if (databaseFile == activeDatabase.getValue()) {
            return activeDatabase.getKey();
        }
        return null;

    }

    private boolean checkDatabaseExists(String databaseName) {
        return databases.containsKey(databaseName);
    }

    private void writeSerializedObject(File file, Object object) {
        try {
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(object);
            out.flush();
            out.close();
            fout.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private Object readSerializedObject(File file) {
        Object serialObject;

        try {
            FileInputStream fin = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fin);

            serialObject = in.readObject();
            in.close();
            fin.close();
            return serialObject;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public void writeChangesToFile() {
        writeSerializedObject(activeDatabase.getValue(), activeDatabase.getKey());
    }

}
