package nz.co.logicons.tlp.mobile.stobyapp.util;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.SocketFactory;

/*
 * @author by Allen
 */
public class DoesNetworkHaveInternet {

    static boolean execute(SocketFactory socketFactory){
        try{
            Log.d(Constants.TAG, "PINGING google.");
            Socket socket = socketFactory.createSocket();
            if (socket == null) {
                throw new IOException("Socket is null.");
            }
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            socket.close();
            Log.d(Constants.TAG, "PING success.");
            return true;
        }catch(IOException io){
            Log.e(Constants.TAG, "No internet connection. ", io);
            return false;
        }
    }
}
