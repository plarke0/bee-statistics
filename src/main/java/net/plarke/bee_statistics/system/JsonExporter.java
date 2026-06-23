package net.plarke.bee_statistics.system;

import net.plarke.bee_statistics.registry.HiveRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class JsonExporter {
    public static void export(String fileName) throws FileAlreadyExistsException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String filePath = "config/" + fileName + ".json";

        File file = new File(filePath);
        if (file.isFile()) {
            throw new FileAlreadyExistsException("Cannot export data to an existing file");
        }

        //TODO Add handling for failed saving
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(HiveRegistry.getAll(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
