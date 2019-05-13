/*
 * Copyright (C) 2018 Stuiart Davies (stuartdd)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package external;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Thanks to the following articles who's authors inspired this work!
 *
 * https://machinekoder.com/controlling-tp-link-hs100110-smart-plugs-with-machinekit/
 * https://blog.georgovassilis.com/2016/05/07/controlling-the-tp-link-hs100-wi-fi-smart-plug/
 * https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 *
 * @author stuartdd
 */
public class HS100Controller {

    private static final String IPADDRESS_PATTERN
            = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private static Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    private static final int KEY = 171;
    private static final int OFFSET = 4;
    private static final int BUFF_LEN = 1000;
    private static final String ON_STR = "{\"system\":{\"set_relay_state\":{\"state\":1}}}";
    private static final String OFF_STR = "{\"system\":{\"set_relay_state\":{\"state\":0}}}";
    private static final String QUERY_STR = "{\"system\":{\"get_sysinfo\":null}}";

    private static String address;
    private static int port;
    private static String action;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*
        Collect input data from the command line
         */
        parseArgs(args);
        /**
         * Set up the connection to the device
         */
        try (Socket clientSocket = new Socket(address, port)) {
            /*
            Encrypt and Send the action string.
             */
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.write(encrypt(action));
            /*
            Read and decrypt the response then send it to std out
             */
            System.out.println(decrypt(read(clientSocket.getInputStream())));
        } catch (IOException ex) {
            /*
            Somthing wend horibly wrong so print the stack trace and exit!
             */
            ex.printStackTrace(System.err);
            /*
            Exit value is 1 so the Operating System will know we failed!
             */
            System.exit(1);
        }
    }

    /**
     ***********************************************************************
     * Streaming bytes, Encryption and Decryption.
     * **********************************************************************
     */
    /**
     * Read the response stream in to a byte array.
     *
     * @param inputStream The response input stream
     * @return A byte array with all bytes from the stream
     * @throws IOException If an error occurs reading the stream.
     */
    private static byte[] read(InputStream inputStream) throws IOException {
        final byte[] b = new byte[BUFF_LEN];
        int pos = 0;
        int c = inputStream.read();
        while (c >= 0) {
            b[pos++] = (byte) c;
            c = inputStream.read();
        }
        /*
        Return a copy of the buffer with the correct number of bytes in it.
         */
        return Arrays.copyOf(b, pos);
    }

    /**
     * Decrypt using the simple proprietary algorithm the device uses.
     *
     * @param bytes An array of bytes to be decrypted
     * @return The decrypted String value.
     */
    private static String decrypt(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        int key = KEY;
        for (int j = OFFSET; j < bytes.length; j++) {
            int a = key ^ bytes[j];
            key = bytes[j];
            sb.append((char) (a & 0x7F));
        }
        return sb.toString();
    }

    /**
     * Encrypt using the simple proprietary algorithm the device uses.
     *
     * @param in The string to be encrypted (the command to the device)
     * @return An encrypted series of bytes.
     */
    private static byte[] encrypt(final String in) {
        final byte[] buf = new byte[BUFF_LEN];
        int pos = 0;
        for (int i = 0; i < OFFSET; i++) {
            buf[pos++] = 0;
        }
        int key = KEY;
        for (byte b : in.getBytes()) {
            int a = key ^ b;
            key = a;
            buf[pos++] = (byte) a;
        }
        return Arrays.copyOf(buf, pos);
    }

    /**
     ***********************************************************************
     * Validation of args and error handling.
     * **********************************************************************
     */
    /**
     * Parse and Validate all of the command line arguments.
     *
     * @param args A list of command line arguments
     */
    public static void parseArgs(String[] args) {
        /*
        Default values
         */
        action = QUERY_STR;
        port = 9999;
        address = null;
        for (String arg : args) {
            if (arg != null) {
                /*
                Make every arg lowercase
                 */
                arg = arg.toLowerCase();
                /*
                Validate and set the port. 
                 */
                if (arg.startsWith("-p")) {
                    try {
                        port = Integer.parseInt(arg.substring(2));
                    } catch (NumberFormatException nfe) {
                        errorExit("Invalid port number " + arg);
                    }
                    continue;
                }
                /*
                Want the switch ON
                 */
                if (arg.equals("on")) {
                    action = ON_STR;
                    continue;
                }
                /*
                Want the switch OFF
                 */
                if (arg.equals("off")) {
                    action = OFF_STR;
                    continue;
                }
                /*
                Validate and set the IP address
                 */
                if (pattern.matcher(arg).matches()) {
                    address = arg;
                }
            }
        }
        /*
        If the ip address was not provided then we cannot continue!
         */
        if (address == null) {
            errorExit("Required ip address is missing or invalid.");
        }
    }

    /**
     * An error occurd. Notify the user and add some help.
     *
     * Note all output goes to the err stream so all other output can be
     * re-directed.
     *
     * The app exits with a return code of 1 so the Operating System can detect
     * the error.
     *
     * @param message The error message.
     */
    private static void errorExit(String message) {
        System.err.println("Error: " + message);
        System.err.println("Parameters:\n"
                + "  Port: -p1234 Will use port 1234. Default value is 9999.\n"
                + "  Action: ON, OFF or STATUS. Default is STATUS.\n"
                + "  Address: 192.168.1.1 IP Address of the switch. Required.\n"
                + "Ordering of parameters is not important. All are case insensitive.\n"
                + "Examples:\n"
                + "  192.168.1.9\n"
                + "  192.168.1.9 OFF\n"
                + "  On 192.168.1.9 -p9999\n"
                + "  192.168.1.9 status\n"
                + "-------------------------------------------------------------------");
        System.exit(1);
    }
}
