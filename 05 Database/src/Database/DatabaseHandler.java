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
// from file and is then able to load other databases from and save them to file. The info.storage 
// file is updated when databases are changes (added or dropped), to add or remove a new record 
// of a database. Each database is created in a file, which i supdated on each quuery cycle.
public class DatabaseHandler {
    private static DatabaseHandler dbHandler;

    // The path provides a path to the database files, while the handlerfile refers
    // to the dbHandlers own file that it loads into the databases hashmap on
    // initialisation.
    private String dbFolderPath = ".." + File.separator + "database" + File.separator;
    private File dbHandlerFile = new File(dbFolderPath + "info.storage");

    // Databases maps the database name to the file associated with that database.
    // It is stored in info.storage and is loaded on initiation of the dbhandler.
    // Actived database holds a reference to the current active database and
    // associated .db file to be able to write it to file on updates
    static private HashMap<String, File> databases;
    private SimpleEntry<Database, File> currentDatabaseRecord;

    // Constructor loads existing info.storage file if it exists, or else creates a
    // new one. This file stores a record of all the databases available to work
    // with
    private DatabaseHandler() {
        if (dbHandlerFile.exists()) {
            System.out.println("Loading info.storage database file");

            // Is it possible to avoid an unchecked cast warning no the below? Couldn't
            // figure out how...
            try {
                databases = (HashMap<String, File>) readSerializedObject(dbHandlerFile);

            } catch (ClassNotFoundException | IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        } else {
            try {
                databases = new HashMap<>();

                dbHandlerFile.getParentFile().mkdirs();
                dbHandlerFile.createNewFile();

                writeSerializedObject(dbHandlerFile, databases);
                System.out.println("info.storage database file created");

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    // Singleton design pattern ensures only one instance of dbhandler can exist.
    public static DatabaseHandler getInstance() {
        if (dbHandler == null) {
            dbHandler = new DatabaseHandler();
        }
        return dbHandler;
    }

    // Creates new database and saves it to file
    public void createDatabase(String databaseName) throws RuntimeException {
        if (checkDatabaseExists(databaseName)) {
            throw new RuntimeException("ERROR: Database " + databaseName + " already exists.");
        }

        Database database = new Database();
        File databaseFile = new File(dbFolderPath + databaseName + ".db");

        databases.put(databaseName, databaseFile);

        // Save database file and update dbhandler info.storage
        writeSerializedObject(dbHandlerFile, databases);
        writeSerializedObject(databaseFile, database);
    }

    // Load database from file if it exists and return Database object
    public Database useDatabase(String databaseName) throws RuntimeException {
        if (!checkDatabaseExists(databaseName)) {
            throw new RuntimeException("ERROR: Unknown database " + databaseName + ".");
        }

        File databaseFile = databases.get(databaseName);

        try {
            Database database = (Database) readSerializedObject(databaseFile);
            currentDatabaseRecord = new SimpleEntry<Database, File>(database, databaseFile);
            return database;

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("ERROR: Could not read file " + databaseName + " from disk.");
        }
    }

    // Remove database reference, update info.storage file and delete database file
    public Database dropDatabase(String databaseName) throws RuntimeException {
        if (!checkDatabaseExists(databaseName)) {
            throw new RuntimeException("ERROR: Unknown database " + databaseName + ".");
        }

        File databaseFile = databases.remove(databaseName);
        databaseFile.delete();

        writeSerializedObject(dbHandlerFile, databases);

        // Returns current active database to ensure it is cleared in context if
        // required
        if (databaseFile == currentDatabaseRecord.getValue()) {
            Database activeDatabase = currentDatabaseRecord.getKey();
            currentDatabaseRecord = null;
            return activeDatabase;
        }
        return null;
    }

    private boolean checkDatabaseExists(String databaseName) {
        return databases.containsKey(databaseName);
    }

    public void writeChangesToFile() {
        if (currentDatabaseRecord != null) {
            writeSerializedObject(currentDatabaseRecord.getValue(), currentDatabaseRecord.getKey());
        }
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

    // This throws the exception as failed reads will be handlesdd differenlu. If
    // the main database file can't be open, an ERROR: is just thrown, otherwise a
    // warning is return that the database file can't be read
    private Object readSerializedObject(File file) throws IOException, ClassNotFoundException {
        Object serialObject;

        FileInputStream fin = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fin);

        serialObject = in.readObject();
        in.close();
        fin.close();
        return serialObject;
    }
}
