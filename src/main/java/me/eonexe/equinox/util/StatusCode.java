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

package me.eonexe.equinox.util;

/**
 * Enum that holds the Http Status Codes that we are concerned about when
 * creating and catching WebExceptions.
 *
 * @author DV8FromTheWorld (Austin Keener)
 * @version v1.0.0  July 16, 2014
 */
public enum StatusCode
{
    UNKNOWN_HOST("Couldn't find api.imgur.com, are you connected to the internet?", 1),
    SUCCESS("The action was successful!", 200),
    BAD_REQUEST("Upload interupted or corrupted.", 400),
    UNAUTHORIZED("Action requires Auth. Credentials are invalid.", 401),
    FORBIDDEN("You don't have access.  Possible lack of API credits.", 403),
    NOT_FOUND("The requested image or album does not exist.", 404),
    FILE_TOO_BIG("The file that you tried to upload was too big!", 413),
    UPLOAD_LIMITED("You are uploading to quickly! You've been rate limited.", 429),
    INTERNAL_SERVER_ERROR("Imgur unexpected internal server error. Not our fault.", 500),
    SERVICE_UNAVAILABLE("Imgur is unavailable currently.  Most likely over capacity.", 502),
    UNKNOWN_ERROR("An error occured, but we don't know what kind. Sorry!", -1);

    private String description;
    private int httpCode;

    /**
     * Creates a new StatusCode with provided description and http code.
     *
     * @param description
     *          A message that describes the status or what might have caused it.
     * @param httpCode
     *          The Http response associated with this StatusCode.
     */
    private StatusCode(String description, int httpCode)
    {
        this.description = description;
        this.httpCode = httpCode;
    }

    /**
     * Gets the StatusCode associated with this Http response code.
     *
     * @param code
     *          Http response code.
     * @return
     *          The StatusCode associated with the provided code.
     */
    public static StatusCode getStatus(int code)
    {
        switch (code)
        {
            case 1:
                return UNKNOWN_HOST;
            case 200:
                return SUCCESS;
            case 400:
                return BAD_REQUEST;
            case 401:
                return UNAUTHORIZED;
            case 403:
                return FORBIDDEN;
            case 404:
                return NOT_FOUND;
            case 413:
                return FILE_TOO_BIG;
            case 429:
                return UPLOAD_LIMITED;
            case 500:
                return INTERNAL_SERVER_ERROR;
            case 502:
                return SERVICE_UNAVAILABLE;
            default:
                return UNKNOWN_ERROR;
        }
    }

    /**
     * Gets the string description of this StatusCode.
     *
     * @return
     *          The description of this StatusCode.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the Http code associated with this StatusCode.
     *
     * @return
     *          The Http associated with this StatusCode.
     */
    public int getHttpCode()
    {
        return httpCode;
    }

    /**
     * Returns a string that represents this StatusCode.
     * Format: StatusCode - Name: [name] - HttpCode: [httpCode] - Description: [description]
     */
    @Override
    public String toString()
    {
        return String.format("StatusCode - %s: %s - %s: %d - %s: %s",
                "Name", super.toString(),
                "HttpCode", getHttpCode(),
                "Description", getDescription());
    }
}
