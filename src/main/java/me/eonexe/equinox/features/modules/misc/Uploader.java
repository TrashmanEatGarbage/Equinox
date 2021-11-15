/**
 * Copyright 2014 DV8FromTheWorld (Austin Keener)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.eonexe.equinox.features.modules.misc;

import me.eonexe.equinox.util.MathUtil;
import me.eonexe.equinox.util.StatusCode;
import me.eonexe.equinox.util.MathUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Controls all interface with the web and the Imgur API.
 * 
 * @author DV8FromTheWorld (Austin Keener)
 * @version v1.0.0  July 16, 2014
 */
public class Uploader
{
    public static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";
    public static final String ALBUM_API_URL = "https://api.imgur.com/3/album";
    public static final int MAX_UPLOAD_ATTEMPTS = 3;

    //CHANGE TO @CLIENT_ID@ and replace with buildscript.
    private final static String CLIENT_ID = "efce6070269a7f1";

    /**
     * Takes a file and uploads it to Imgur.
     * Does not check to see if the file is an image, this should be done
     * before the file is passed to this method.
     * 
     * @param file
     *          The image to be uploaded to Imgur.
     * @return
     *          The JSON response from Imgur.
     */
    public static String upload(File file) {
        HttpURLConnection conn = getHttpConnection(UPLOAD_API_URL);
        writeToConnection(conn, "image=" + MathUtil.toBase64(file));
        return getResponse(conn);
    }

    /**
     * Creates an album on Imgur.
     * Does not check if imageIds are valid images on Imgur.
     * 
     * @param imageIds
     *          A list of ids of images on Imgur.
     * @return
     *          The JSON response from Imgur.
     */
    public static String createAlbum(List<String> imageIds)
    {
        HttpURLConnection conn = getHttpConnection(ALBUM_API_URL);
        String ids = "";
        for (String id : imageIds)
        {
            if (!ids.equals(""))
            {
                ids += ",";
            }
            ids += id;
        }
        writeToConnection(conn, "ids=" + ids);
        return getResponse(conn);
    }

    /**
     * Creates and sets up an HttpURLConnection for use with the Imgur API.
     * 
     * @param url
     *          The URL to connect to. (check Imgur API for correct URL).
     * @return
     *          The newly created HttpURLConnection.
     */
    private static HttpURLConnection getHttpConnection(String url)
    {
        HttpURLConnection conn;
        try
        {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            conn.setReadTimeout(100000);
            conn.connect();
            return conn;
        }
        catch (UnknownHostException e)
        {
            throw new MathUtils(StatusCode.UNKNOWN_HOST, e);
        }
        catch (IOException e)
        {
            throw new MathUtils(StatusCode.UNKNOWN_ERROR, e);
        }
    }
    
    /**
     * Sends the provided message to the connection as uploaded data.
     * 
     * @param conn
     *          The connection to send the data to.
     * @param message
     *          The data to upload.
     */
    private static void writeToConnection(HttpURLConnection conn, String message)
    {
        OutputStreamWriter writer;
        try
        {
            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            throw new MathUtils(StatusCode.UNKNOWN_ERROR, e);
        }
    }
    
    /**
     * Gets the response from the connection, Usually in the format of a JSON string.
     * 
     * @param conn
     *          The connection to listen to.
     * @return
     *          The response, usually as a JSON string.
     */
    private static String getResponse(HttpURLConnection conn)
    {
        StringBuilder str = new StringBuilder();
        BufferedReader reader;
        try
        {
            if (conn.getResponseCode() != StatusCode.SUCCESS.getHttpCode())
            {
                throw new MathUtils(conn.getResponseCode());
            }
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                str.append(line);
            }
            reader.close();
        }
        catch (IOException e)
        {
            throw new MathUtils(StatusCode.UNKNOWN_ERROR, e);
        }
        if (str.toString().equals(""))
        {
            throw new MathUtils(StatusCode.UNKNOWN_ERROR);
        }
        return str.toString();
    }
}
