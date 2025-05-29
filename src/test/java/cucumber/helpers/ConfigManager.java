package cucumber.helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static String getBaseUrl() {
        return dotenv.get("BASE_URL");
    }

    public static String get(String key) {
        return dotenv.get(key);
    }
}
