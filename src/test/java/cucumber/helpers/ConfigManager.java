package cucumber.helpers;

import io.github.cdimascio.dotenv.Dotenv;

public class ConfigManager {
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static String getBaseUrl() {
        return dotenv.get("BASE_URL");
    }

    public static String getName() {
        return dotenv.get("ACCOUNT_USERNAME");
    }

    public static String getPassword() {
        return dotenv.get("ACCOUNT_PASSWORD");
    }

    public static String getToken() {
        return dotenv.get("TOKEN");
    }
}
