package Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

// SINGLETON pattern

public class DatabaseHandler {
    private static DatabaseHandler dbHandler;

    private SimpleEntry<Database, File> activeDatabase;

    private Map<String, File> databases;

    String dbFolderPath = ".." + File.separator + "database" + File.separator;

    File dbHandlerFile = new File(dbFolderPath + "info.storage");

    private DatabaseHandler() {
        if (dbHandlerFile.exists()) {
            databases = (Map<String, File>) readSerializedObject(dbHandlerFile);
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
        return dbHandler;
    }

    public void createDatabase(String databaseName) throws Exception {
        if (checkDatabaseExists(databaseName)) {
            throw new Exception("ERROR: Database " + databaseName + " already exists.");
        }

        Database database = new Database(databaseName);
        File databaseFile = new File(dbFolderPath + databaseName + ".db");

        databases.put(databaseName, databaseFile);

        writeSerializedObject(databaseFile, database);
    }

    public Database useDatabase(String databaseName) throws Exception {
        if (!checkDatabaseExists(databaseName)) {
            throw new Exception("ERROR: Unknown database " + databaseName + ".");
        }

        File databaseFile = databases.get(databaseName);
        Database database = (Database) readSerializedObject(databaseFile);
        activeDatabase = new SimpleEntry<Database, File>(database, databaseFile);

        return database;
    }

    public void dropDatabase(String databaseName) throws Exception {
        if (!checkDatabaseExists(databaseName)) {
            throw new Exception("ERROR: Unknown database " + databaseName + ".");
        }

        File databaseFile = databases.remove(databaseName);
        // TODO error checking on delete?
        databaseFile.delete();
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

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private Object readSerializedObject(File file) {
        Object serialObject;

        try {
            FileInputStream fin = new FileInputStream(dbHandlerFile);
            ObjectInputStream in = new ObjectInputStream(fin);

            serialObject = in.readObject();
            in.close();

            return serialObject;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        return null;
    }

    public void writeChanges() {
        writeSerializedObject(activeDatabase.getValue(), activeDatabase.getKey());
    }

}
