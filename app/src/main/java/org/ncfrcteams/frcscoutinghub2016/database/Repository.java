package org.ncfrcteams.frcscoutinghub2016.database;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 3/8/2016.
 */
public class Repository implements Serializable {
    private int ID;
    private File file;
    private Map<String,Database> databases = new HashMap<>();
    private List<SuperMap> compiledData;

    public static synchronized Repository openRepository(Context context, int ID) {
        File file = fileFromRepositoryID(context, ID);
        if(file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                return (Repository) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        Repository repository = new Repository(context,ID);
        repository.saveToFile();
        return repository;
    }

    private static File fileFromRepositoryID(Context context, int ID) {
        String path = "Repository_" + ID + ".txt";
        return new File(context.getFilesDir(),path);
    }

    private Repository(Context context, int ID) {
        this.ID = ID;
        file = fileFromRepositoryID(context, ID);
        compiledData = new ArrayList<>();
    }

    public synchronized void saveToFile() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(this);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addDatabase(Database database, String author) {
        if(database.getID() != ID)
            return;

        databases.put(author, database);
    }

    public synchronized void compile() {
        compiledData.clear();

        for(Database database : databases.values()) {
            compiledData.addAll(database.getSuperMapList());
        }
    }

    public synchronized List<SuperMap> getCompiledData() {
        return compiledData;
    }

    public int getID() {
        return ID;
    }
}
