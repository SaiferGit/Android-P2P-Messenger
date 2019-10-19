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
    Button connectBtn, getIPBtn;
    ImageButton sendButton, voiceMsgOn, attachmentBtn;
    TextView clickHereBtn;

    MenuItem changeBG, saveChat, disconnect, removeAllChat,voiceMode, resetLayout;

    ServerClass serverClass;
    ClientClass clientClass;
    SendReceive sendReceive;

    ScrollView conversations;
    LinearLayout conversationLayout;

    boolean need = true, voice = false;

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
        setContentView(R.layout.activity_main);

        initialization();

    }

    private void initialization() {

        // referencing variables with layout
        mToolbar = findViewById(R.id.main_page_toolbar);

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
        connectBtn = findViewById(R.id.connectButton);
        getIPBtn = findViewById(R.id.getIPButton);
        clickHereBtn = findViewById(R.id.click_here);
        voiceMsgOn = findViewById(R.id.voice_btn);
        attachmentBtn = findViewById(R.id.send_files_and_voice_btn);

        conversations = findViewById(R.id.conversations);
        conversationLayout = findViewById(R.id.scroll_view_linear_layout);
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
        voiceMode = menu.findItem(R.id.main_voice_mode);
        resetLayout = menu.findItem(R.id.main_reset_layout);

        changeBG.setEnabled(false);
        saveChat.setEnabled(false);
        disconnect.setEnabled(false);
        removeAllChat.setEnabled(false);
        voiceMode.setEnabled(false);
        resetLayout.setEnabled(false);

        return true;
    }

    // when menu items will be selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // opening a dialog box for confirmation everytime, so for cleaning code, we wrote it in a method
        if(item.getItemId() == R.id.main_change_bg_option)
            openChangeBGDialogBox();

        if(item.getItemId() == R.id.main_save_chat_option)
            openSaveChatDialogBoxForHim();

        if(item.getItemId() == R.id.main_disconnect_option)
            openDisconnectAlertDialogBox();

        if(item.getItemId() == R.id.main_remove_chat_option)
            openRemoveAllChatAlertDialogBox();

        if(item.getItemId() == R.id.main_voice_mode)
            voiceModeOperation();

        if(item.getItemId() == R.id.main_reset_layout)
            resetLayoutAlertDialog();
        return true;
    }

    private void resetLayoutAlertDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_disconnect_dialog, null);

        TextView textView = mView.findViewById(R.id.custom_disconnect_dialog_textView);
        textView.setText("Are you sure you want to reset your layout?");
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel);
        Button btn_yes = (Button) mView.findViewById(R.id.btn_yes);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Reseting Layout");

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
                // saving chat on confirmation
                resetLayoutForHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void resetLayoutForHim() {
        sendReceive.write(caesarCipherEncryption("bg@%@bg0",shift));
        thirdLayout.setBackgroundResource(R.drawable.background3);
        mToolbar.setBackgroundColor(Color.parseColor("#233E4E"));
    }

    // on voice mode button selected
    private void voiceModeOperation() {
        if(voiceMode.getTitle().equals("Voice Mode : Off")) {
            voiceMode.setTitle("Voice Mode : On");
            openVoiceModeDialogBox();
            voice = true;
            attachmentBtn.setVisibility(View.INVISIBLE);
            voiceMsgOn.setVisibility(View.VISIBLE);
            saveChat.setEnabled(false);
            disconnect.setEnabled(false);
            changeBG.setEnabled(false);
            removeAllChat.setEnabled(false);
            resetLayout.setEnabled(false);
            Toast.makeText(MainActivity.this, "Voice Mode Enabled", Toast.LENGTH_SHORT).show();


        }
        else {
            voiceMode.setTitle("Voice Mode : Off");
            voice = false;
            attachmentBtn.setVisibility(View.VISIBLE);
            voiceMsgOn.setVisibility(View.INVISIBLE);
            saveChat.setEnabled(true);
            disconnect.setEnabled(true);
            changeBG.setEnabled(true);
            removeAllChat.setEnabled(true);
            resetLayout.setEnabled(true);
            Toast.makeText(MainActivity.this, "Voice Mode Disabled", Toast.LENGTH_SHORT).show();


        }

    }

    // voice mode dialog box selection
    private void openVoiceModeDialogBox() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_voice_mode_dialog, null);

        Button btn_yes = (Button) mView.findViewById(R.id.btn_yes);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("Voice Command Mode");

        btn_yes.setOnClickListener((v) -> {
            alertDialog.dismiss();
        });


        alertDialog.show();
    }

    // when removing  all chat selected
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
                if(isChecked) buttonView.setTextColor(getResources().getColor(R.color.green)); // changing color of checkbox
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
                // if user selects checkbox also, we will remove all the chat for everyone. so sending a code to the other end
                if(cbRemoveForAll.isChecked()){
                    sendReceive.write(caesarCipherEncryption("remove@%@", shift));
                }
                // remove self chat
                removeAllChatForHim();
                Log.d(TAG, "Remove all chat Msg: " +caesarCipherEncryption("diconnect@%@d", shift)); // notify user
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    // removing chat main method
    private void removeAllChatForHim() {
        // removing all the linear layout
        conversationLayout.removeAllViews();
        Toast.makeText(this, "All chat have been removed!", Toast.LENGTH_SHORT).show();

    }

    // opening bg change dialog box for showing 9 more layouts
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

        // when each of a layout is selected, a coded message is sending other end so that we can change it for other end also
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

    // when changebgforhim method is called, it takes a string, that is sent through the socket, and  changing background when getting the string
    // it also changes the toolbar color so that the UI/Theme looks awesome :)
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
        else if(msg.equals("bg@%@bg0")){
            thirdLayout.setBackgroundResource(R.drawable.background3);
            mToolbar.setBackgroundColor(Color.parseColor("#233E4E"));
        }
    }

    // on save chat selection it will appear
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
                // saving chat on confirmation
                saveChatForHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }


    // main method for saving chat
    private void saveChatForHim() {
        String allMessage = ""; // took a empty string
        int count = conversationLayout.getChildCount(); // getting all the linear layout no
        TextView children; // instance of a textview
        for (int i = 0; i < count; i++) {
            // for every message, we add 3 linear layout, 2 textview one for showing messages, another for showing time
            // and 1 image view as a divider between 2 messages.

            // if  the chlid of a linear layout is a instance of imageview, then there is no text, as it is a divider.
            if(conversationLayout.getChildAt(i) instanceof ImageView)
                continue;
            else{
                children = (TextView) conversationLayout.getChildAt(i); // getting the textview
                // adding messages
                if(children.getText().toString().contains(".txt has been received and downloaded on android/data/com.example.p2p/")){
                    allMessage += "CLIENT: " + children.getText().toString() + "\n\n";
                }
                else if(children.getText().toString().contains(".txt has been sent")){
                    allMessage += "ME: " + children.getText().toString() + "\n\n";
                }
                // if textview is a time message then
                else if(children.getText().toString().contains("am") || children.getText().toString().contains("pm")){

                    if (children.getCurrentTextColor() == Color.parseColor("#FCE4EC")) {
                        allMessage += " " + children.getText().toString() + "\n\n";
                    } else {
                        allMessage += " " + children.getText().toString() + "\n\n";
                    }

                }
                // null messages won't be saved, we aren't sending time with file sending, so there generates a null text view.
                else if(children.getText().toString().equals("") || children.getText().toString().equals(null)){

                }
                else if(!children.getText().toString().equals("Background has been changed") ){
                    // if textview color is #FCE4EC then it is sender message, saving as ME
                    // otherwise it is receiver message
                    if (children.getCurrentTextColor() == Color.parseColor("#FCE4EC")) {
                        allMessage += "ME: " + children.getText().toString() + "\n";
                    } else {
                        allMessage += "CLIENT: " + children.getText().toString() +"\n";
                    }
                }

            }

        }
        // write the string and save it as chat history.txt, and add time stamp with it
        writeToFile(allMessage, true, "Chat History");
    }

    // writing a text file
    private void writeToFile(String data, boolean timeStamp, String name) {
        String time = "";
        if(timeStamp){
            //Time getting
            time = getTime(true);
        }


        File path = this.getExternalFilesDir(null); // getting file path
        filePath = path.toString();

        File file;

        if(time.equals(""))
            file = new File(path, name); // saving shared files

        else
            file = new File(path, name + "(" + time + ")" + System.lineSeparator() + ".txt"); // saving chat history.txt with timestamp

        FileOutputStream stream; // creating a file output stream for sending file
        try {
            stream = new FileOutputStream(file, false);
            stream.write(data.getBytes()); // writing
            stream.close(); // closing the stream
            Toast.makeText(this, "File Succcessfully Saved!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, data);
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
    }

    // getting time function
    private String getTime(boolean need) {
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

        if(need)
            time = hour + ":" + minute + ":" + second + " " + zone;
        else
            time = hour + ":" + minute + " " + zone;

        return time;
    }

    // on attachment button click
    public void onSendFilesAndVoiceClicked(View view) {
        openFileandVoiceShareAlertDialog(); // opening a dialog for choosing next step

    }

    // opening dialog
    private void openFileandVoiceShareAlertDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.custom_file_voice_change_dialog, null);

        Button btn_file = (Button) mView.findViewById(R.id.btn_file);
        Button btn_voice = (Button) mView.findViewById(R.id.btn_voice);

        alert.setView(mView);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setTitle("File and Voice Sharing");

        // on file button click storage will be opened
        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStorage();
                alertDialog.dismiss();
            }
        });

        // on voice button click we started of taking input
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeVoiceForHim();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void openStorage() {
        // creating new gallery intent for selecting text file only
        Intent intent = new Intent().setType("text/plain").setAction(Intent.ACTION_GET_CONTENT);
        // called a override method for starting gallery intent
        startActivityForResult(Intent.createChooser(intent, "Select a TXT file"), 123);

    }

    // taking voice input
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

    // on activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // if this is a gallery opening request
        if(requestCode==123 && resultCode==RESULT_OK) {
            Uri uri = intent.getData();
            String path = getFilePathFromUri(uri); // getting file path
            File file = new File(path);
            if(file.exists())
                Log.d(TAG, "Selected file exists");


            String fileText = readTextFile(uri); // getting files inside information.
            Log.d(TAG, "text inside file: "+fileText);

            // sending the file with a special code for recognize other end.
            // file code + file name + code + inside file information
            String writeMsg = "file@%@"+file.getName()+"@%@"+fileText;
            sendFilesALertDialog(writeMsg); // confirming before sending
        }

        // if request is a voice input then
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && intent != null) {
                    ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String voiceCommand = result.get(0); // getting the best result which is at position 0

                    // comparing voice command for checking if it is a command or not
                    int x = voiceCommand.compareToIgnoreCase("Remove All Chat");
                    int y = voiceCommand.compareToIgnoreCase("Save Chat");
                    int z = voiceCommand.compareToIgnoreCase("Disconnect");
                    int m = voiceCommand.compareToIgnoreCase("voice mode off");
                    int n = voiceCommand.compareToIgnoreCase("share a file");
                    boolean p = voiceCommand.contains("change background as ");
                    int q = voiceCommand.compareToIgnoreCase("reset layout");

                    // doing change by voice by redirecting it to the related function
                    if(x == 0){
                        openRemoveAllChatAlertDialogBox();
                    }
                    else if(y == 0){
                        openSaveChatDialogBoxForHim();
                    }
                    else if(z == 0){
                        openDisconnectAlertDialogBox();
                    }
                    else if(m == 0){
                        voiceModeOperation();
                    }
                    else if(n == 0){
                        openStorage();
                    }
                    else if(q == 0){
                        resetLayoutAlertDialog();
                    }
                    // changing background
                    else if(p){
                        boolean layout1 = voiceCommand.contains("layout 1");
                        boolean layout2 = voiceCommand.contains("layout 2");
                        boolean layout3 = voiceCommand.contains("layout 3");
                        boolean layout4 = voiceCommand.contains("layout 4");
                        boolean layout5 = voiceCommand.contains("layout 5");
                        boolean layout6 = voiceCommand.contains("layout 6");
                        boolean layout7 = voiceCommand.contains("layout 7");
                        boolean layout8 = voiceCommand.contains("layout 8");
                        boolean layout9 = voiceCommand.contains("layout 9");

                        if(layout1){
                            String msg =  "bg@%@bg1";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 1", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout2){
                            String msg =  "bg@%@bg2";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 2", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout3){
                            String msg =  "bg@%@bg3";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 3", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout4){
                            String msg = "bg@%@bg4";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 4", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout5){
                            String msg =  "bg@%@bg5";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 5", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout6){
                            String msg =  "bg@%@bg6";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 6", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout7){
                            String msg =  "bg@%@bg7";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 7", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout8){
                            String msg =  "bg@%@bg8";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 8", Toast.LENGTH_SHORT).show();
                        }

                        else if(layout9){
                            String msg =  "bg@%@bg9";
                            sendReceive.write(caesarCipherEncryption(msg, shift));
                            Toast.makeText(MainActivity.this, "background is selected as LAYOUT 9", Toast.LENGTH_SHORT).show();
                        }


                    }

                    else{
                        messageEditText.setText(voiceCommand);
                        Toast.makeText(this, "Didn't get the input, please try again", Toast.LENGTH_SHORT).show();
                    }


                    //if(result.get(0).equals("save chat"))

                }
                break;
        }
    }

    // after selecting file, a dialog will be opened for confirmation
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

        // on cancellation
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        // on accept
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReceive.write(caesarCipherEncryption(writeMsg, shift)); // sending it with proper encoding
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    // reading the file text whenever it is selected
    private String readTextFile(Uri uri){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)));
            String line = "";

            // reading line by line and adding a \n at the end
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

    // getting file path using uri
    private String getFilePathFromUri(Uri uri){
        String path = uri.getPathSegments().get(1);
        path = Environment.getExternalStorageDirectory().getPath()+"/"+path.split(":")[1];
        return path;
    }

    // opening disconnect dialog box
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

    // disconnect main function
    public void disconnectHim() {

        // closing client and server socket, means closing sending and receiving port
        // changing layout and background
        try {
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

            targetIPEditText.setVisibility(View.INVISIBLE);
            targetPortEditText.setVisibility(View.INVISIBLE);
            connectBtn.setVisibility(View.INVISIBLE);
            clickHereBtn.setVisibility(View.INVISIBLE);
            getIPBtn.setVisibility(View.INVISIBLE);
            encrypKeyEditText.setVisibility(View.INVISIBLE);

            Toast.makeText(MainActivity.this, "chat is disconnected", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR/n"+e);

            //Toast.makeText(MainActivity.this, "Error: " +e, Toast.LENGTH_LONG).show();
        }
    }

    // whenever on start server is clicked
    public void onStartServerClicked(View v){
        String port = receivePortEditText.getText().toString(); // getting the port from edittext

        // checking if port is empty or not
        if(TextUtils.isEmpty(port)){
            receivePortEditText.requestFocus(); // focusing as an error
            receivePortEditText.setError("Please write your receive port first"); // showing what need to avoid the error
        }

        // if there's a valid input then create a server class on that port so that the client can take data from that port
        else{
            try{
                serverClass = new ServerClass(Integer.parseInt(port));
                serverClass.start();
                Toast.makeText(this, "Server has been started..", Toast.LENGTH_SHORT).show();

                // showing the further information
                targetIPEditText.setVisibility(View.VISIBLE);
                targetPortEditText.setVisibility(View.VISIBLE);
                connectBtn.setVisibility(View.VISIBLE);
                clickHereBtn.setVisibility(View.VISIBLE);
                getIPBtn.setVisibility(View.VISIBLE);
                encrypKeyEditText.setVisibility(View.VISIBLE);

            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Can't start server, please check the port number first", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // on connect clicked
    public void onConnectClicked(View v){

        String port = targetPortEditText.getText().toString();
        String tergetIP = targetIPEditText.getText().toString();
        String encrypKey = encrypKeyEditText.getText().toString(); // takling the encryption key shift number
        shift = Integer.parseInt(encrypKey);

        // checking is empty or not
        if(TextUtils.isEmpty(port)){
            targetPortEditText.requestFocus();
            targetPortEditText.setError("Please write your target port first");
        }

        // checking self ip or not
        else if(tergetIP.equals(ip)){
            targetIPEditText.requestFocus();
            targetIPEditText.setError("This is your self IP, please change it");
        }

        // else connect him, and redirect to chat screen
        else{
            try{
                clientClass = new ClientClass(tergetIP, Integer.parseInt(port));
                clientClass.start();
                // showing success message
                Toast.makeText(MainActivity.this, "your sending port and listening port has been set successfully", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Log.d(TAG, "ERROR: "+e);
                Toast.makeText(MainActivity.this, "Can't connect with server, please check all the requirements", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // on sending click
    public void onSendClicked(View v){
        String msg = messageEditText.getText().toString().trim(); // getting the typed messages excluding new line first and last
        String msgTime = "@%@" + getTime(false);
        String msgWithTime = msg + msgTime; // sending message with time

        // you need to type some before sending
        if(TextUtils.isEmpty(msg)){
            messageEditText.requestFocus();
            messageEditText.setError("Please write your message first");
        }
        else
        {
            //sendReceive.write("message@%@"+msg);
            sendReceive.write(caesarCipherEncryption(msgWithTime, shift)); // send the encrypted message
        }

    }

    // getting ip
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

    public void onSVoiceIconClicked(View view) {
        takeVoiceForHim();
    }

    // sending and receiving socket
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
        // writing
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

    // server class for listening
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

    // client class for sending
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
                socket = new Socket(hostAdd, port);

                sendReceive = new SendReceive(socket);
                sendReceive.start();
                showToast("Connected to other device. You can now exchange messages.");

                Log.d(TAG, "Client is connected to server");

                // enabling invisible components
                enableComponent();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Can't connect to server. Check the IP address and Port number and try again: " + e);
            } catch (Exception e) {
                Log.d(TAG, "ERROR: " + e);
            }
        }
    }

    // add mesage method
    private void addMessage(int color, String message) {

        runOnUiThread(() -> {
                    TextView textView = new TextView(this);
                    TextView msgTime = new TextView(this);

                    // if it's a sender message
                    if (color == Color.parseColor("#FCE4EC")
                            && !(caesarCipherDecryption(message, shift).contains("bg@%@bg"))
                            && !(caesarCipherDecryption(message, shift).contains("diconnect@%@d"))
                            && !(caesarCipherDecryption(message, shift).contains("file@%@"))
                            && !(caesarCipherDecryption(message, shift).contains("remove@%@"))) {
                        textView.setPadding(200, 20, 10, 10);
                        //textView.setMaxLines(5);
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

                        msgTime.setPadding(0,0,0,0);

                        msgTime.setTextSize(14);
                        msgTime.setTextColor(Color.parseColor("#FCE4EC"));
                        msgTime.setTypeface(textView.getTypeface(), Typeface.ITALIC );
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        msgTime.setGravity(Gravity.LEFT);

                        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        // lp1.setMargins(10, 10, 10, 10);
                        // lp1.setMargins(10, 10, 10, 10);
                        //lp1.width = 400;
                        lp4.leftMargin = 200;
                        msgTime.setLayoutParams(lp4);

                        //textView.setBackgroundResource(R.drawable.sender_messages_layout);
                    }
                    // else if receiver message
                    else if(!(caesarCipherDecryption(message, shift).contains("bg@%@bg"))
                            && !(caesarCipherDecryption(message, shift).contains("diconnect@%@d"))
                            && !(caesarCipherDecryption(message, shift).contains("file@%@"))
                            && !(caesarCipherDecryption(message, shift).contains("remove@%@"))) {
                        textView.setPadding(10, 20, 200, 10);
                        //textView.setMaxLines(5);
                        textView.setGravity(Gravity.LEFT);
                        textView.setBackgroundResource(R.drawable.receiver_messages_layout);
                        textView.setTextIsSelectable(true);

                        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        //lp1.setMargins(10, 10, 10, 10);
                        //lp1.width = 400;
                        //lp1.leftMargin = 150;
                        lp2.rightMargin = 200;
                        textView.setLayoutParams(lp2);

                        msgTime.setTextSize(14);
                        msgTime.setTextColor(Color.parseColor("#FFFFFF"));
                        msgTime.setTypeface(textView.getTypeface(), Typeface.ITALIC);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        //lp1.setMargins(10, 10, 10, 10);
                        //lp1.width = 400;
                        //lp1.leftMargin = 150;
                        lp3.rightMargin = 200;
                        msgTime.setGravity(Gravity.RIGHT);
                        msgTime.setLayoutParams(lp3);


                    }
                    textView.setTextColor(color);
                    Log.d(TAG, "encrypted msg: " + message);
                    String actualMessage = caesarCipherDecryption(message, shift);
                    Log.d(TAG, "decrypted msg: " + actualMessage);


                    String[]  messages = actualMessage.split("@%@", 0);

                    // if its a file
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
                    // if its a remove message
                    else if(messages[0].equals("remove")){
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(15);
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);
                        removeAllChatForHim();

                        if(color == Color.parseColor("#FCE4EC"))
                            textView.setText("You have removed all the previous message");
                        else{
                            textView.setText("Your pair has removed all the previous message");
                        }

                    }
                    // if its a bg change message
                    else if(actualMessage.contains("bg@%@bg")){
                        changeBGforHim(actualMessage);
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(13);
                        textView.setTextSize(15);
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);
                        if(actualMessage.equals("bg@%@bg0")){
                            textView.setText("Background reset to default");
                        }
                        else
                            textView.setText("Background has been changed");

                    }
                    // if its a disconnect message
                    else if(actualMessage.contains("diconnect@%@d")){
                        textView.setPadding(0,0,0,0);

                        textView.setTextSize(13);
                        conversationLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        textView.setGravity(Gravity.CENTER);
                        textView.setText("Your Pair has been disconnected.");

                    }
                    // else it's a normal message
                    else{
                        Log.d(TAG, "messages[0]: " +messages[0]);
                        Log.d(TAG, "messages[0]: " +messages[1]);

                        textView.setTextSize(20);
                        textView.setText(messages[0]); // setting message on the message textview

                        msgTime.setText("(" + getTime(false) + ")"); // setting messing time
                    }


                    // creating divider between two messages
                    addDividerBetweenTwoMessages();

                    // adding 2 more views in linear layout every time
                    conversationLayout.addView(textView);
                    conversationLayout.addView(msgTime);
                    conversations.post(() -> conversations.fullScroll(View.FOCUS_DOWN)); // for getting last message in first
                }
        );
    }

    // adding divider method
    private void addDividerBetweenTwoMessages() {
        ImageView divider = new ImageView(this);
        conversationLayout.addView(divider);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 4, 4, 4);
        divider.setLayoutParams(lp);
        divider.setBackgroundColor(Color.TRANSPARENT);
    }

    // custom show toast function
    public void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    // enabling component on creating client class
    public void enableComponent() {
        runOnUiThread(() -> {
            firstLayout.setVisibility(View.GONE);
            thirdLayout.setVisibility(View.VISIBLE);
            saveChat.setEnabled(true);
            changeBG.setEnabled(true);
            disconnect.setEnabled(true);
            removeAllChat.setEnabled(true);
            voiceMode.setEnabled(true);
            resetLayout.setEnabled(true);
        });
    }

    // caesar cipher encryption, every message, sending through socket, is encrypted for security purpose
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

    // decreption on the other end
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

