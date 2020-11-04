package utils;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketUtil {
    public static Socket getConnection() throws URISyntaxException {
        Socket socket;
        socket = IO.socket("http://192.168.137.1:5000/");
        return socket;
    }
}
