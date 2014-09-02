// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.util;

import java.util.Random;

import org.apache.log4j.Logger;


public class BufferUtils {

	private static final Logger log = Logger.getLogger(BufferUtils.class);

	private final static int randomLen = 20;
	
	public static String getConferenceId(String user){
		return generateRandom(randomLen)+user;
	}
	public static String generateRandom(int length) {
		Random random = new Random();
		byte[] byteArray = new byte[length];
		int i;
		random.nextBytes(byteArray);
		for (i = 0; i < byteArray.length; i++) {
			if (byteArray[i] < 0)
				byteArray[i] *= -1;

			while (!((byteArray[i] >= 65 && byteArray[i] <= 90)
					|| (byteArray[i] >= 97 && byteArray[i] <= 122) || (byteArray[i] <= 57 && byteArray[i] >= 48))) {

				if (byteArray[i] > 122)
					byteArray[i] -= random.nextInt(byteArray[i]);
				if (byteArray[i] < 48)
					byteArray[i] += random.nextInt(5);
				else
					byteArray[i] += random.nextInt(10);
			}
		}
		return new String(byteArray);
	}
	
    /**
     * Copy "copySize" floats from "origBuffer", starting on "startOrigBuffer",
     * to "destBuffer", starting on "startDestBuffer".
     */
    public static int floatBufferIndexedCopy(
        float[] destBuffer,
        int startDestBuffer,
        float[] origBuffer,
        int startOrigBuffer,
        int copySize ) {

        int destBufferIndex = startDestBuffer;
        int origBufferIndex = startOrigBuffer;
        int counter = 0;

        println( "floatBufferIndexedCopy",
                "destBuffer.length = " + destBuffer.length +
                ", startDestBuffer = " + startDestBuffer +
                ", origBuffer.length = " + origBuffer.length +
                ", startOrigBuffer = " + startOrigBuffer +
                ", copySize = " + copySize + "." );

        if ( destBuffer.length < ( startDestBuffer + copySize ) ) {
            println( "floatBufferIndexedCopy", "Size copy problem." );
            return -1;
        }

        for ( counter = 0; counter < copySize; counter++ ) {
            destBuffer[ destBufferIndex ] = origBuffer[ origBufferIndex ];

            destBufferIndex++;
            origBufferIndex++;
        }

        println( "floatBufferIndexedCopy", counter + " bytes copied." );

        return counter;
    }


    /**
     * Copy "copySize" bytes from "origBuffer", starting on "startOrigBuffer",
     * to "destBuffer", starting on "startDestBuffer".
     */
    public static int byteBufferIndexedCopy(
        byte[] destBuffer,
        int startDestBuffer,
        byte[] origBuffer,
        int startOrigBuffer,
        int copySize ) {

        int destBufferIndex = startDestBuffer;
        int origBufferIndex = startOrigBuffer;
        int counter = 0;

        println( "byteBufferIndexedCopy",
                "destBuffer.length = " + destBuffer.length +
                ", startDestBuffer = " + startDestBuffer +
                ", origBuffer.length = " + origBuffer.length +
                ", startOrigBuffer = " + startOrigBuffer +
                ", copySize = " + copySize + "." );

        if ( destBuffer.length < ( startDestBuffer + copySize ) ) {
            println( "byteBufferIndexedCopy", "size copy problem." );
            return -1;
        }

        for ( counter = 0; counter < copySize; counter++ ) {
            destBuffer[ destBufferIndex ] = origBuffer[ origBufferIndex ];

            destBufferIndex++;
            origBufferIndex++;
        }

        println( "byteBufferIndexedCopy", counter + " bytes copied." );

        return counter;
    }


    private static void println( String method, String message ) {

        log.debug( "BufferUtils - " + method + " -> " + message );
        //System.out.println( "BufferUtils - " + method + " -> " + message );
    }
}
