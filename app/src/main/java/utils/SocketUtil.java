package utils;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketUtil {
    public static Socket getConnection() throws URISyntaxException {
        Socket socket= IO.socket("http://192.168.50.53:5000/");
        return socket;
    }
}
