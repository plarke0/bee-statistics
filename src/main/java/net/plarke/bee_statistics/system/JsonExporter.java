package net.plarke.bee_statistics.system;

import net.plarke.bee_statistics.registry.HiveRegistry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class JsonExporter {
    public static void export() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        //TODO Add option for filename
        File file = new File("config/hive_logs.json");

        //TODO Add handling for failed saving
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(HiveRegistry.getAll(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
