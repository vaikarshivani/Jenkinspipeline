package com.google.firebase.samples.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.Template;
import com.google.gson.*;
import org.json.JSONObject;
import javax.xml.validation.Schema;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Configure {
    private static final String SERVICE_ACCOUNT_KEY_PATH = "./serviceAccount.json";

    public static void main(String[] args) throws IOException {
        FileInputStream serviceAccount = new FileInputStream(SERVICE_ACCOUNT_KEY_PATH);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        JsonObject localJsonObject = null;
        JsonObject configJsonObject = null;
        try {

            String localJsonContent = new String(Files.readAllBytes(Paths.get("D:/firebaseDemoProject/token/local_config.json")));
            String configJsonContent = new String(Files.readAllBytes(Paths.get("D:/firebaseDemoProject/token/config.json")));
            localJsonObject = JsonParser.parseString(localJsonContent).getAsJsonObject();
            configJsonObject = JsonParser.parseString(configJsonContent).getAsJsonObject();
            JsonObject mergedJsonObject = mergeJsonObjects(localJsonObject, configJsonObject);

            String mergedConfigJson = toFormattedJsonString(mergedJsonObject);
            // Optionally, you can write the merged JSON to a new file
            writeToFile("merged.json", mergedConfigJson);
            getTemplate();
            String mergedJsonContent = readConfig(); // Read merged.json content


            // Retrieve and publish the template
//            String etag = "*"; // Set the etag value accordingly
            publishTemplate(mergedConfigJson);
        } catch (IOException e) {
            System.err.println("Error while merging or reading JSON files: " + e.getMessage());
        }
    }

    public static void writeToFile(String filename, String content) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(content);
            System.out.println("JSON template has been written to " + filename + " successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void publishTemplate(String mergedJson) throws IOException {
        String templateStr = readConfig();



            try {
                Template template = Template.fromJSON(mergedJson);
                FirebaseRemoteConfig.getInstance().validateTemplateAsync(template);
                System.out.println("Template was valid and safe to use");
                FirebaseRemoteConfig.getInstance().publishTemplate(template);
                Template etag = FirebaseRemoteConfig.getInstance().getTemplateAsync().get();
                System.out.println("Template has been published." +etag.getETag());




            } catch (FirebaseRemoteConfigException e) {
                System.out.println(e.getCause().getMessage());
                System.out.println("Error publishing template: ");
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//        }
    }

    private static String readConfig() throws FileNotFoundException {
        File file = new File("merged.json");
        Scanner scanner = new Scanner(file);

        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    // Get template from Firebase Remote Config and write it to config.json
    private static void getTemplate() throws IOException {
        try {

            Template template = FirebaseRemoteConfig.getInstance().getTemplate();
            JsonParser jsonParser = new JsonParser();
            JsonObject templateJson = jsonParser.parse(template.toJSON()).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String jsonStr = gson.toJson(templateJson);
            String etag = template.getETag();
            System.out.println("ETag from server: " + etag);
            File file = new File("config.json");
            PrintWriter printWriter = new PrintWriter(new FileWriter(file));
            printWriter.print(jsonStr);
            printWriter.flush();
            printWriter.close();
            System.out.println("Template retrieved and has been written to config.json");
        } catch (FirebaseRemoteConfigException e) {
            System.out.println("Error retrieving template: " + e.getHttpResponse().getContent());
        }
    }

    private static JsonObject mergeJsonObjects(JsonObject localJson, JsonObject configJson) {
        JsonObject mergedObject = new JsonObject();
        // Merge your JSON objects as needed.
        for (Map.Entry<String, JsonElement> localEntry : localJson.entrySet()) {
            String key = localEntry.getKey();
            JsonElement value = localEntry.getValue();
            if (!mergedObject.has(key)) {
                if (!value.isJsonNull()) {
                    mergedObject.add(key, value);
                }
            }
        }
        for (Map.Entry<String, JsonElement> configEntry : configJson.entrySet()) {
            String key = configEntry.getKey();
            JsonElement value = configEntry.getValue();
            if (!mergedObject.has(key)) {
                if (!value.isJsonNull()) {
                    mergedObject.add(key, value);
                }
            }
        }

        System.out.println("Local object and config.json files are merged and written to merged.json");
        return mergedObject;
    }

    private static String toFormattedJsonString(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(jsonObject);
    }



}
