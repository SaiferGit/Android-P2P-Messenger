package com.example.p2pmessenger;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.LambdaConversionException;
import java.net.ServerSocket;
import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity {

    EditText receivePortEditText, targetPortEditText, messageEditText, targetIPEditText, encrypKeyEditText;
    RelativeLayout firstLayout, thirdLayout;
    Button bgChange;
    ImageButton sendButton;

    MenuItem changeBG, saveChat, disconnect, removeAllChat;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;

    ScrollView conversations;
    LinearLayout conversationLayout;

    private Toolbar mToolbar; // for adding the toolbar

    String filePath = null, ip = null;


    int shift = 0;


    static final int MESSAGE_READ=1;
    static final String TAG = "trap";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.what == MESSAGE_READ) {
                byte[] readBuff = (byte[]) msg.obj;
                String tempMsg = new String(readBuff, 0, msg.arg1);
                addMessage(Color.parseColor("#FFFFFF"), tempMsg);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_layout);

        initialization();
        //writeToFile("Hello :(", false);
    }

    private void initialization() {

        // referencing variables with layout
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        // adding the toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chit Chat"); // giving the title


        receivePortEditText = findViewById(R.id.receiveEditText);
        targetPortEditText = findViewById(R.id.targetPortEditText);
        messageEditText = findViewById(R.id.messageEditText);
        targetIPEditText = findViewById(R.id.targetIPEditText);
        encrypKeyEditText = findViewById(R.id.encrypEditText);
        firstLayout = findViewById(R.id.firstLayout);
        thirdLayout = findViewById(R.id.third_layout);
        sendButton = findViewById(R.id.send_message_btn);

        conversations = findViewById(R.id.conversations);
        conversationLayout = findViewById(R.id.scroll_view_linear_layout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_change_bg_option)
            openChangeBGDialogBox();

        if(item.getItemId() == R.id.main_save_chat_option)
            openSaveChatDialogBoxForHim();

        if(item.getItemId() == R.id.main_disconnect_option)
            openDisconnectAlertDialogBox();

        if(item.getItemId() == R.id.main_remove_chat_option)
            openRemoveAllChatAlertDialogBox();

        return true;
    }

    private void openRemoveAllChatAlertDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_remove_all_chat_dialog, null);


        CheckBox cbRemoveForAll = mView.findViewById(R.id.remove_for_all);

        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_clear_messages = (Button) mView.findViewById(R.id.btn_clear_messages);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Removing All Chat");
        cbRemoveForAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) buttonView.setTextColor(getResources().getColor(R.color.green));
                else buttonView.setTextColor(getResources().getColor(R.color.black));
            }
        });



        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        btn_clear_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRemoveForAll.isChecked()){
                    // do something
                    sendReceive.write(caesarCipherEncryption("remove@%@", shift));
                }
                removeAllChatForHim();
                //sendReceive.write(caesarCipherEncryption("diconnect@%@d", shift));
                Log.d(TAG, "Remove all chat Msg: " +caesarCipherEncryption("diconnect@%@d", shift));
                //disconnectHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void removeAllChatForHim() {
        String allMessage = "";
        conversationLayout.removeAllViews();
        Toast.makeText(this, "All chat have been removed!", Toast.LENGTH_SHORT).show();

    }

    private void openChangeBGDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_background_change_dialog, null);

        Button layout1 =  mView.findViewById(R.id.btn_bg1);
        Button layout2 =  mView.findViewById(R.id.btn_bg2);
        Button layout3 =  mView.findViewById(R.id.btn_bg3);
        Button layout4 =  mView.findViewById(R.id.btn_bg4);
        Button layout5 =  mView.findViewById(R.id.btn_bg5);
        Button layout6 =  mView.findViewById(R.id.btn_bg6);
        Button layout7 =  mView.findViewById(R.id.btn_bg7);
        Button layout8 =  mView.findViewById(R.id.btn_bg8);
        Button layout9 =  mView.findViewById(R.id.btn_bg9);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("Changing Background");

        layout1.setOnClickListener((v) -> {
            String msg =  "bg@%@bg1";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 1", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout2.setOnClickListener((v) -> {
            String msg =  "bg@%@bg2";
            sendReceive.write(caesarCipherEncryption(msg,shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 2", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout3.setOnClickListener((v) -> {
            String msg =  "bg@%@bg3";
            sendReceive.write(caesarCipherEncryption(msg,shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 3", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout4.setOnClickListener((v) -> {
            String msg =  "bg@%@bg4";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 4", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });
        layout5.setOnClickListener((v)-> {
            String msg =  "bg@%@bg5";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 5", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout6.setOnClickListener((v) -> {
            String msg =  "bg@%@bg6";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 6", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout7.setOnClickListener((v) -> {
            String msg =  "bg@%@bg7";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 7", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout8.setOnClickListener((v) -> {
            String msg =  "bg@%@bg8";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 8", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        layout9.setOnClickListener((v) -> {
            String msg =  "bg@%@bg9";
            sendReceive.write(caesarCipherEncryption(msg, shift));
            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 9", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        alertDialog.show();

    }

    private void changeBGforHim(String msg) {

        if(msg.equals("bg@%@bg1")){
            thirdLayout.setBackgroundResource(R.drawable.background1);
            mToolbar.setBackgroundColor(Color.parseColor("#8B0000"));
        }

        else if(msg.equals("bg@%@bg2")){
            thirdLayout.setBackgroundResource(R.drawable.background2);
            mToolbar.setBackgroundColor(Color.parseColor("#461D3D"));
        }

        else if(msg.equals("bg@%@bg3")){
            thirdLayout.setBackgroundResource(R.drawable.background10);
            mToolbar.setBackgroundColor(Color.parseColor("#B0CA4590"));
        }

        else if(msg.equals("bg@%@bg4")){
            thirdLayout.setBackgroundResource(R.drawable.background4);
            mToolbar.setBackgroundColor(Color.parseColor("#30504C"));
        }

        else if(msg.equals("bg@%@bg5")){
            thirdLayout.setBackgroundResource(R.drawable.background5);
            mToolbar.setBackgroundColor(Color.parseColor("#30504C"));
        }

        else if(msg.equals("bg@%@bg6")){
            thirdLayout.setBackgroundResource(R.drawable.background6);
            mToolbar.setBackgroundColor(Color.parseColor("#461D3D"));
        }
        else if(msg.equals("bg@%@bg7")){
            thirdLayout.setBackgroundResource(R.drawable.background7);
            mToolbar.setBackgroundColor(Color.parseColor("#2E3F3B"));
        }
        else if(msg.equals("bg@%@bg8")){
            thirdLayout.setBackgroundResource(R.drawable.background8);
            mToolbar.setBackgroundColor(Color.parseColor("#DBAC30"));
        }
        else if(msg.equals("bg@%@bg9")){
            thirdLayout.setBackgroundResource(R.drawable.background9);
            mToolbar.setBackgroundColor(Color.parseColor("#095061"));
        }
    }


    // by this method we can access the options inside the menu folder
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu); // linking up with menu option resource file

        changeBG = menu.findItem(R.id.main_change_bg_option);
        saveChat = menu.findItem(R.id.main_save_chat_option);
        disconnect = menu.findItem(R.id.main_disconnect_option);
        removeAllChat = menu.findItem(R.id.main_remove_chat_option);

        changeBG.setEnabled(false);
        saveChat.setEnabled(false);
        disconnect.setEnabled(false);
        removeAllChat.setEnabled(false);

        return true;
    }

    private void openSaveChatDialogBoxForHim() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_disconnect_dialog, null);

        TextView textView = mView.findViewById(R.id.custom_disconnect_dialog_textView);
        textView.setText("Are you sure you want to save your conversations? It will be saved on android/data/com.example.p2pmessenger/");
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_yes = (Button) mView.findViewById(R.id.btn_yes);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Saving Conversations");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChatForHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void saveChatForHim() {
        String allMessage = "";
        int count = conversationLayout.getChildCount();
        TextView children;
        for (int i = 0; i < count; i++) {
            if(conversationLayout.getChildAt(i) instanceof ImageView)
                continue;
            else{
                children = (TextView) conversationLayout.getChildAt(i);
                if(children.getText().toString() != "Background has been changed"){
                    if (children.getCurrentTextColor() == Color.parseColor("#FCE4EC")) {
                        allMessage += "ME: " + children.getText().toString() + "\n\n";
                    } else {
                        allMessage += "CLIENT: " + children.getText().toString() + "\n\n";
                    }
                }

            }

        }
        writeToFile(allMessage, true, "Chat History");
    }

    private void writeToFile(String data, boolean timeStamp, String name) {
        String time = "";
        if(timeStamp){
            //Time getting
            time = getTime();
        }


        File path = this.getExternalFilesDir(null);
        filePath = path.toString();

        File file;

        if(time.equals(""))
            file = new File(path, name);

        else
            file = new File(path, name + "(" + time + ")" + System.lineSeparator() + ".txt");

        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file, false);
            stream.write(data.getBytes());
            stream.close();
            Toast.makeText(this, "File Succcessfully Saved!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, data);
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
    }


    private String getTime() {
        int minute, hour, second;
        String zone = "am";
        String time = "";

        Calendar calendar = Calendar.getInstance();
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 12) {
            zone = "pm";
        }
        if (hour > 12) {
            hour = hour % 12;
        }
        second = calendar.get(Calendar.SECOND);

        time = hour + ":" + minute + ":" + second + " " + zone;

        return time;
    }

    public void onSendFilesAndVoiceClicked(View view) {
        openFileandVoiceShareAlertDialog();


    }

    private void openFileandVoiceShareAlertDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_file_voice_change_dialog, null);

        Button btn_file = (Button) mView.findViewById(R.id.btn_file);
        Button btn_voice = (Button) mView.findViewById(R.id.btn_voice);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("File and Voice Sharing");

        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setType("text/plain").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a TXT file"), 123);
                alertDialog.dismiss();
            }
        });

        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVoiceForHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void takeVoiceForHim() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri uri = intent.getData();
            String path = getFilePathFromUri(uri);
            File file = new File(path);
            if(file.exists())
                Log.d(TAG, "Selected file exists");
            else
                Toast.makeText(this, "Selected file doesn't exists", Toast.LENGTH_SHORT).show();

            String fileText = readTextFile(uri);
            Log.d(TAG, "text inside file: "+fileText);

            String writeMsg = "file@%@"+file.getName()+"@%@"+fileText;
            sendFilesALertDialog(writeMsg);
        }

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && intent != null) {
                    ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    messageEditText.setText(result.get(0));
                }
                break;
        }
    }

    private void sendFilesALertDialog(String writeMsg) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_disconnect_dialog, null);

        TextView textView = mView.findViewById(R.id.custom_disconnect_dialog_textView);
        textView.setText("Are you sure you want to send file?");
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_yes = (Button) mView.findViewById(R.id.btn_yes);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Sending File");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReceive.write(caesarCipherEncryption(writeMsg, shift));
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private String readTextFile(Uri uri){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append("\n" + line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    private String getFilePathFromUri(Uri uri){
        String path = uri.getPathSegments().get(1);
        path = Environment.getExternalStorageDirectory().getPath()+"/"+path.split(":")[1];
        return path;
    }

    private void openDisconnectAlertDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_disconnect_dialog, null);

        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_yes = (Button) mView.findViewById(R.id.btn_yes);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Disconnecting");

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReceive.write(caesarCipherEncryption("diconnect@%@d", shift));
                Log.d(TAG, "Disconnect Msg: " +caesarCipherEncryption("diconnect@%@d", shift));
                disconnectHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void disconnectHim() {

        try {
            sendReceive.socket.close();
            clientClass.socket.close();
            serverClass.socket.close();


            thirdLayout.setVisibility(View.GONE);
            mToolbar.setBackgroundColor(Color.parseColor("#233E4E"));
            thirdLayout.setBackgroundResource(R.drawable.background3);
            changeBG.setEnabled(false);
            saveChat.setEnabled(false);
            disconnect.setEnabled(false);
            getSupportActionBar().setTitle("Chit Chat");
            firstLayout.setVisibility(View.VISIBLE);

            Toast.makeText(MainActivity.this, "chat is disconnected", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR/n"+e);

            //Toast.makeText(MainActivity.this, "Error: " +e, Toast.LENGTH_LONG).show();
        }
    }

    public void onStartServerClicked(View v){
        String port = receivePortEditText.getText().toString();

        if(TextUtils.isEmpty(port)){
            receivePortEditText.requestFocus();
            receivePortEditText.setError("Please write your receive port first");
        }


        else{
            try{
                serverClass = new ServerClass(Integer.parseInt(port));
                serverClass.start();
                Toast.makeText(this, "Server has been started..", Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Can't start server, please check the port number first", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onConnectClicked(View v){

        String port = targetPortEditText.getText().toString();
        String tergetIP = targetIPEditText.getText().toString();
        String encrypKey = encrypKeyEditText.getText().toString();
        shift = Integer.parseInt(encrypKey);



        if(TextUtils.isEmpty(port)){
            targetPortEditText.requestFocus();
            targetPortEditText.setError("Please write your target port first");
        }

        else if(tergetIP.equals(ip)){
            targetIPEditText.requestFocus();
            targetIPEditText.setError("This is your self IP, please change it");
        }


        else{
            try{
                clientClass = new ClientClass(tergetIP, Integer.parseInt(port));
                clientClass.start();
                Toast.makeText(MainActivity.this, "your sending port and listening port has been set successfully", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.d(TAG, "ERROR: "+e);
                Toast.makeText(MainActivity.this, "Can't connect with server, please check all the requirements", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onSendClicked(View v){

        String msg = messageEditText.getText().toString();

        if(TextUtils.isEmpty(msg)){
            messageEditText.requestFocus();
            messageEditText.setError("Please write your message first");
        }
        else
        {
            //sendReceive.write("message@%@"+msg);
            sendReceive.write(caesarCipherEncryption(msg, shift));
        }

    }

    public void onGetIPClicked(View view) {

        ip = Utils.getIPAddress(true);
        openIPAlertDialog();
    }

    private void openIPAlertDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_disconnect_dialog, null);

        TextView textView = mView.findViewById(R.id.custom_disconnect_dialog_textView);
        if( ip != null && ip != "")
            textView.setText("Your IP Add is : "+ ip);
        else
            textView.setText("Can't get your IP address, please check your connectionn");
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_yes = (Button) mView.findViewById(R.id.btn_yes);
        btn_cancel.setVisibility(View.GONE);
        btn_yes.setVisibility(View.GONE);
        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("IP Address");

        alertDialog.show();
    }

    private class SendReceive extends Thread {
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket skt) {
            socket = skt;
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
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(String msg) {
            new Thread(() -> {
                try {
                    outputStream.write(msg.getBytes());
                    addMessage(Color.parseColor("#FCE4EC"), msg);
                    runOnUiThread(() ->
                            messageEditText.setText("")
                    );
                } catch (IOException e) {
                    Log.d(TAG, "Can't send message: " + e);
                } catch (Exception e) {
                    Log.d(TAG, "Error: " + e);
                }
            }).start();

        }
    }

    public class ServerClass extends Thread {

        Socket socket;
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
                showToast("Server Started. Waiting for client...");
                Log.d(TAG, "Waiting for client...");
                socket = serverSocket.accept();
                Log.d(TAG, "Connection established from server");
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "ERROR: " + e);
            } catch (Exception e) {
                Log.d(TAG, "ERROR: " + e);
            }
        }
    }

    public class ClientClass extends Thread {
        Socket socket;
        String hostAdd;
        int port;

        public ClientClass(String hostAddress, int port) {
            this.port = port;
            this.hostAdd = hostAddress;
        }

        @Override
        public void run() {
            try {

                //System.setProperty("javax.net.ssl.trustStore", "za.store");

                //socket = ((SSLSocketFactory)SSLSocketFactory.getDefault()).createSocket(hostAdd, port);
                socket = new Socket(hostAdd, port);

                sendReceive = new SendReceive(socket);
                sendReceive.start();
                showToast("Connected to other device. You can now exchange messages.");

                Log.d(TAG, "Client is connected to server");
                enableComponent();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Can't connect to server. Check the IP address and Port number and try again: " + e);
            } catch (Exception e) {
                Log.d(TAG, "ERROR: " + e);
            }
        }
    }

    private void addMessage(int color, String message) {

        runOnUiThread(() -> {
                    TextView textView = new TextView(this);

                    if (color == Color.parseColor("#FCE4EC")
                            && !(caesarCipherDecryption(message, shift).contains("bg@%@bg"))
                            && !(caesarCipherDecryption(message, shift).contains("diconnect@%@d"))
                            && !(caesarCipherDecryption(message, shift).contains("file@%@"))
                            && !(caesarCipherDecryption(message, shift).contains("remove@%@"))) {
                        textView.setPadding(200, 10, 10, 20);
                        textView.setMaxLines(5);
                        textView.setGravity(Gravity.RIGHT);
                        textView.setBackgroundResource(R.drawable.sender_messages_layout);
                        textView.setTextIsSelectable(true);

                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        // lp1.setMargins(10, 10, 10, 10);
                        // lp1.setMargins(10, 10, 10, 10);
                        //lp1.width = 400;
                        lp1.leftMargin = 200;
                        //lp1.rightMargin = 50;
                        textView.setLayoutParams(lp1);

                        //textView.setBackgroundResource(R.drawable.sender_messages_layout);
                    } else if(!(caesarCipherDecryption(message, shift).contains("bg@%@bg"))
                            && !(caesarCipherDecryption(message, shift).contains("diconnect@%@d"))
                            && !(caesarCipherDecryption(message, shift).contains("file@%@"))
                            && !(caesarCipherDecryption(message, shift).contains("remove@%@"))) {
                        textView.setPadding(10, 10, 200, 20);
                        textView.setMaxLines(5);
                        textView.setGravity(Gravity.LEFT);
                        textView.setBackgroundResource(R.drawable.receiver_messages_layout);
                        textView.setTextIsSelectable(true);

                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        //lp1.setMargins(10, 10, 10, 10);
                        //lp1.width = 400;
                        //lp1.leftMargin = 150;
                        lp2.rightMargin = 200;
                        textView.setLayoutParams(lp2);


                    }
                    textView.setTextColor(color);
                    Log.d(TAG, "encrypted msg: " + message);
                    String actualMessage = caesarCipherDecryption(message, shift);
                    Log.d(TAG, "decrypted msg: " + actualMessage);


                    String[]  messages = actualMessage.split("@%@", 0);

                    if(messages[0].equals("file")){
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(15);
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);


                        Log.d(TAG, "File Name: "+messages[1]);
                        Log.d(TAG, "File Contains:\n "+messages[2]);
                        if(color == Color.parseColor("#FCE4EC"))
                            textView.setText(messages[1]+" has been sent");
                        else{
                            textView.setText(messages[1]+" has been received and downloaded on android/data/com.example.p2p/");
                            writeToFile(messages[2], false, messages[1]);
                        }

                    }
                    else if(messages[0].equals("remove")){
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(15);
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);
                        removeAllChatForHim();

                        if(color == Color.parseColor("#FCE4EC"))
                            textView.setText("You have been removed all the previous message");
                        else{
                            textView.setText("Your pair has been removed all the previous message");
                        }

                    }
                    else if(actualMessage.contains("bg@%@bg")){
                        changeBGforHim(actualMessage);
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(13);
                        textView.setTextSize(15);
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("Background has been changed");
                    }

                    else if(actualMessage.contains("diconnect@%@d")){
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(13);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("Your Pair has been disconnected.");

                    }
                    //String actualMessage = editMessage(message);
                    else{

                        textView.setTextSize(20);
                        textView.setText(actualMessage);
                    }


                    // creating divider between two messages
                    addDividerBetweenTwoMessages();


                    conversationLayout.addView(textView);
                    conversations.post(() -> conversations.fullScroll(View.FOCUS_DOWN));
                }
        );
    }

    private void addDividerBetweenTwoMessages() {
        ImageView divider = new ImageView(this);
        conversationLayout.addView(divider);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(9, 9, 9, 9);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(Color.TRANSPARENT);
    }


    public void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    public void enableComponent() {
        runOnUiThread(() -> {
            firstLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.VISIBLE);
            saveChat.setEnabled(true);
            changeBG.setEnabled(true);
            disconnect.setEnabled(true);
            removeAllChat.setEnabled(true);
        });
    }

    public static String caesarCipherEncryption(String plainText, int shift){
        if( shift > 26 ) shift = shift % 26;
        else if( shift < 0 ) shift = (shift % 26) + 26;

        String cipherText = "";
        int length = plainText.length();
        for(int i = 0; i< length; i++){
            char ch = plainText.charAt(i);
            if(Character.isLetter(ch)){
                if(Character.isLowerCase(ch)){
                    char c = (char)(ch + shift);
                    if(c > 'z'){
                        cipherText += (char)(ch - (26-shift));
                    }
                    else{
                        cipherText += c;
                    }
                }
                else if(Character.isUpperCase(ch)){
                    char c = (char)(ch + shift);
                    if(c > 'Z'){
                        cipherText += (char)(ch - (26-shift));
                    }
                    else{
                        cipherText += c;
                    }
                }
            }
            else{
                cipherText += ch;
            }
        }

        return cipherText;
    }

    public static String caesarCipherDecryption(String plainText, int shift){
        if( shift > 26 ) shift = shift % 26;
        else if( shift < 0 ) shift = (shift % 26) + 26;

        String cipherText = "";
        int length = plainText.length();
        for(int i = 0; i< length; i++){
            char ch = plainText.charAt(i);
            if(Character.isLetter(ch)){
                if(Character.isLowerCase(ch)){
                    char c = (char)(ch - shift);
                    if(c < 'a'){
                        cipherText += (char)(ch + (26-shift));
                    }
                    else{
                        cipherText += c;
                    }
                }
                else if(Character.isUpperCase(ch)){
                    char c = (char)(ch - shift);
                    if(c < 'A'){
                        cipherText += (char)(ch + (26-shift));
                    }
                    else{
                        cipherText += c;
                    }
                }
            }
            else{
                cipherText += ch;
            }
        }

        return cipherText;
    }


}

