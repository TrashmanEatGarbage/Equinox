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
 * Custom exception class that handles Web Exceptions.
 *
 * @author DV8FromTheWorld (Austin Keener)
 * @version v1.0.0  July 16, 2014
 */
@SuppressWarnings("serial")
public class MathUtils extends RuntimeException
{
    private StatusCode code;

    /**
     * Creates a new instance of WebException containing
     * the StatusCode and the original exception.
     *
     * @param code
     *          The StatusCode related to this exception.
     * @param cause
     *          The Throwable that set off this exception.
     */
    public MathUtils(StatusCode code, Throwable cause)
    {
        super(cause);
        this.code = code;
    }

    /**
     * Creates a new instance of WebException containing the StatusCode.
     *
     * @param code
     *          The StatusCode related to this exception.
     */
    public MathUtils(StatusCode code)
    {
        this(code, null);
    }

    /**
     * Creates a new instance of WebException containing
     * the StatusCode(based on the provided httpCode) and the original exception.
     *
     * @param httpCode
     *          The httpCode related to this exception.
     * @param cause
     *          The Throwable that set off this exception.
     */
    public MathUtils(int httpCode, Throwable cause)
    {
        this(StatusCode.getStatus(httpCode), cause);
    }

    /**
     * Creates a new instance of WebException containing
     * the StatusCode(based on the provided httpCode).
     *
     * @param httpCode
     *          The httpCode related to this exception.
     */
    public MathUtils(int httpCode)
    {
        this(httpCode, null);
    }

    /**
     * Gets the Http StatusCode associated with this exception.
     *
     * @return
     *          The StatusCode that caused the exception.
     */
    public StatusCode getStatusCode()
    {
        return code;
    }

    /**
     * Gets the description of the exception based on the description of the StatusCode.
     *
     * @return
     *          Description of exception based on Http StatusCode.
     */
    @Override
    public String getMessage()
    {
        return code.getDescription();
    }
}
