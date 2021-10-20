package com.example.p2pmessenger.network.logic;

import android.graphics.Color;
import android.util.Log;

import com.example.p2pmessenger.MainActivity;
import com.example.p2pmessenger.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendReceive extends Thread{
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private final MainActivity core;

    public SendReceive(Socket skt, MainActivity core) {
        socket = skt;
        this.core = core;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;

        while (socket != null) {
            try {
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    core.handler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // writing
    public void write(String msg) {
        new Thread(() -> {
            try {
                byte[] bytes = new byte[0];
                outputStream.write(msg.getBytes());
                core.addMessage(Color.parseColor("#FCE4EC"), msg, bytes);
                core.runOnUiThread(() ->
                        core.messageEditText.setText("")
                );
            } catch (IOException e) {
                Log.d(Constants.TAG, "Can't send message: " + e);
            } catch (Exception e) {
                Log.d(Constants.TAG, "Error: " + e);
            }
        }).start();

    }

    public void writeFile(byte[] bytes, String msg) {
        new Thread(() -> {
            try {
                outputStream.write(bytes);
                core.addMessage(Color.parseColor("#FCE4EC"), msg, bytes);
                core.runOnUiThread(() ->
                        core.messageEditText.setText("")
                );
            } catch (IOException e) {
                Log.d(Constants.TAG, "Can't send message: " + e);
            } catch (Exception e) {
                Log.d(Constants.TAG, "Error: " + e);
            }
        }).start();

    }
}
