import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

class SystemThread extends Thread {
    private final String type;
    private final HashMap<String, String> config;
    private boolean isExiting;
    public SystemThread(String newType) throws Exception {
        type = newType;
        config = Core.loadConfig(type);
        isExiting = false;
    }
    public void setExiting(boolean exiting) { isExiting = exiting; }
    public void run() {
        String module = "";
        try {
            synchronized (this) {
                switch (type) {
                    case "bs":
                        module = "Banking System";
                        while (!isExiting) {
                            BankingSystem.RequestManager.Main.main(config);
                        }
                        // Do any post-launch cleanup here...
                        break;
                    case "oss":
                        module = "Online Shopping System";
                        OnlineShoppingSystem.UI.Main.main(config);
                        // Do any post-launch cleanup here...
                        break;
                }
            }
        } catch (Exception ex) {
            // Do any mandatory cleanup/error recovery here...
            System.out.printf("An unexpected error occurred while running the %s module.\n", module);
        } finally {
            synchronized (this) { notify(); }
        }
    }
}

public class Core {
    public static boolean _DEBUG = true;
    public static boolean _BANKREQUESTDIR = false;
    public static HashMap<String, String> loadConfig(String type) throws Exception {
        HashMap<String, String> defaultConfigData = new HashMap<>(),
            configData = new HashMap<>();
        String dir = "",
            root = "",
            system = "";
        switch (type) {
            case "bs":
                dir = "BankingSystem/";
                system = "Banking System";
                defaultConfigData.put("DIR_BANKREQUESTS", "Requests/");
                defaultConfigData.put("DIR_DATABASE", "DataBase/");
                defaultConfigData.put("FILE_DATA_BANKACCOUNTS", "BankAccounts.dat");
                break;
            case "oss":
                dir = "OnlineShoppingSystem/";
                system = "Online Shopping System";
                defaultConfigData.put("DIR_BANKREQUESTS", "../BankingSystem/Requests/");
                defaultConfigData.put("DIR_DATABASE", "DataBase/");
                defaultConfigData.put("FILE_DATA_CUSTOMERACCOUNTS", "CustomerAccounts.dat");
                defaultConfigData.put("FILE_DATA_ITEMS", "Items.dat");
                defaultConfigData.put("FILE_DATA_ORDERS", "Orders.dat");
                defaultConfigData.put("FILE_DATA_ORDERITEMS", "OrderItems.dat");
                defaultConfigData.put("FILE_DATA_SUPPLIERACCOUNTS", "SupplierAccounts.dat");
                break;
        }
        // Verify the config files are in place or recreate them
        URL dirURL = ClassLoader.getSystemResource(dir);
        if (dirURL != null) {
            root = dirURL.getPath().substring(1).replace(dir, "");
            File configFile = new File(String.format("%s/%s.config", dirURL.getPath().substring(1), type));
            if (configFile.createNewFile()) {
                // Repopulate file using default records
                if (_DEBUG) System.out.printf("The %s configuration is missing. Creating a new file with the default configuration...", system);
                try {
                    FileWriter writer = new FileWriter(configFile);
                    for (String key : defaultConfigData.keySet()) {
                        writer.write(String.format("%s=%s\n", key, defaultConfigData.get(key)));
                        configData.put(key, defaultConfigData.get(key));
                    }
                    writer.close();
                } catch (Exception ex) {
                    if (_DEBUG) System.out.printf("The %s configuration file could not be created.", system);
                }
            } else {
                // Load file and patch missing data with default records
                try {
                    Scanner reader = new Scanner(configFile);
                    while (reader.hasNextLine()) {
                        // All pathing should be completed with / instead of \, and all directory config values should end with /
                        String[] parts = reader.nextLine().split("=");
                        String value = "";
                        for (int i = 1; i < parts.length; i++) {
                            value += i == 1 ? "" : "=" + parts[i].replace('\\', '/');
                        }
                        configData.put(parts[0], value + (parts[0].startsWith("DIR_") && !value.endsWith("/") ? "/" : ""));
                    }
                    reader.close();
                    FileWriter writer = new FileWriter(configFile);
                    for (String key : defaultConfigData.keySet()) {
                        if (!configData.containsKey(key) || configData.get(key).isEmpty()) {
                            writer.write(String.format("%s=%s\n", key, defaultConfigData.get(key)));
                            configData.put(key, defaultConfigData.get(key));
                        }
                    }
                    writer.close();
                } catch (Exception ex) {
                    if (_DEBUG) System.out.printf("The %s configuration file could not be accessed.", system);
                }
            }
        } else {
            throw new Exception(String.format("The %s directory is missing.", dir));
        }
        // Upsert all configData records with defaultConfigData records
        for (String key : defaultConfigData.keySet()) {
            if (!configData.containsKey(key) || configData.get(key).isEmpty()) {
                if (_DEBUG) System.out.printf("The custom %s configuration was missing a value for key %s. The value will default to %s.\n", system, key, defaultConfigData.get(key));
                configData.put(key, defaultConfigData.get(key));
                break;
            }
        }
        // Verify the requests directory is in place and is synced across all systems
        if (type.equals("oss") || type.equals("bs")) {
            String requestDir = "BankingSystem/" + configData.get("DIR_BANKREQUESTS").replace("../BankingSystem/", "");
            URL requestURL = ClassLoader.getSystemResource(requestDir);
            if (requestURL == null && (_BANKREQUESTDIR || !(new File(root + requestDir).mkdir()))) {
                throw new Exception(String.format("The %s directory either could not be accessed, could not be created, or is not the same for all related systems.", requestDir));
            }
            _BANKREQUESTDIR = true;
        }
        if (configData.containsKey("DIR_DATABASE")) {
            // Verify the local database directory is in place
            String dbDir = dir + configData.get("DIR_DATABASE");
            URL dbURL = ClassLoader.getSystemResource(dbDir);
            if (dbURL == null && !(new File(root + dbDir).mkdir())) {
                throw new Exception(String.format("The %s directory could not be either accessed or created.", dbDir));
            } else {
                // Verify all data files are in place
                String dataPath;
                URL dataURL;
                for (String key : configData.keySet()) {
                    if (key.startsWith("FILE_DATA_")) {
                        if (key.contains("/")) {
                            throw new Exception(String.format("The %s data files must be located in the %s directory.", system, dbDir));
                        } else {
                            dataPath = dbDir + configData.get(key);
                            File dataFile = new File(root + dataPath);
                            if (!dataFile.exists() && !dataFile.createNewFile()) {
                                throw new Exception(String.format("The %s data file could not be either accessed or created.", dataPath));
                            }
                        }
                    }
                }
            }
        }
        configData.put("DEBUG", _DEBUG ? "Y" : "N");
        return configData;
    }
    public static void main(String[] args) throws Exception {
        _DEBUG = true || args.length > 0 && args[1].equalsIgnoreCase("v");
        if (_DEBUG) System.out.println("Loading the Online Shopping System module...");
        SystemThread oss = new SystemThread("oss");
        if (_DEBUG) System.out.println("Loading the Banking System module...");
        SystemThread bs = new SystemThread("bs");
        if (_DEBUG) System.out.println("Starting the Online Shopping System module...");
        oss.start();
        if (_DEBUG) System.out.println("Starting the Banking System module...");
        bs.start();
        synchronized(oss) {
            while (oss.isAlive()) {
                oss.wait();
            }
        }
        if (_DEBUG) System.out.println("The Online Shopping System module has terminated.");
        bs.setExiting(true);
        synchronized(bs) {
            if (bs.isAlive()) {
                bs.wait();
            }
        }
        if (_DEBUG) System.out.println("The Banking System Module has terminated.");
    }
}
