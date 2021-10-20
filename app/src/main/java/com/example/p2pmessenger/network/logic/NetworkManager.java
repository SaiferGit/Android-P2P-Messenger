package com.example.p2pmessenger.network.logic;

import android.graphics.Color;
import android.os.Looper;
import android.util.Log;

import com.example.p2pmessenger.MainActivity;
import com.example.p2pmessenger.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager {
    private final MainActivity core;

    public SendReceive sendReceive;
    public ClientClass clientClass;
    public ServerClass serverClass;

    public NetworkManager(MainActivity core){
        this.core = core;
    }

    // server class for listening
    public class ServerClass extends Thread {

        public Socket socket;
        ServerSocket serverSocket;
        int port;

        public ServerClass(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {

                //System.setProperty("javax.net.ssl.keyStore", "za.store");
                //System.setProperty("javax.net.ssl.keyStorePassword", "password");

                //serverSocket = ((SSLServerSocketFactory)SSLServerSocketFactory.getDefault()).createServerSocket(port);
                serverSocket = new ServerSocket(port);
                Looper.prepare();
                core.showToast("Server Started. Waiting for client...");
                Log.d(Constants.TAG, "Waiting for client...");
                socket = serverSocket.accept();
                Log.d(Constants.TAG, "Connection established from server");
                sendReceive = new SendReceive(socket, core);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "ERROR: " + e);
            } catch (Exception e) {
                Log.d(Constants.TAG, "ERROR: " + e);
            }
        }
    }

    // client class for sending
    public class ClientClass extends Thread {
        public Socket socket;
        String hostAdd;
        int port;

        public ClientClass(String hostAddress, int port) {
            this.port = port;
            this.hostAdd = hostAddress;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(hostAdd, port);

                sendReceive = new SendReceive(socket, core);
                sendReceive.start();
                core.showToast("Connected to other device. You can now exchange messages.");

                Log.d(Constants.TAG, "Client is connected to server");

                // enabling invisible components
                core.enableComponent();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "Can't connect to server. Check the IP address and Port number and try again: " + e);
            } catch (Exception e) {
                Log.d(Constants.TAG, "ERROR: " + e);
            }
        }
    }



    public void createServerThread(String port){
        serverClass = new ServerClass(Integer.parseInt(port));
    }

    public void createClientThread(String targetIp, String port){
        clientClass = new ClientClass(targetIp, Integer.parseInt(port));
    }
}
