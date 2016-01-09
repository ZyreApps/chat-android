//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.sprobertson.ZyreTest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import org.zeromq.czmq.Zmsg;
import org.zeromq.czmq.Zsock;
import org.zeromq.zyre.Zyre;

public class MainActivity extends Activity {
    private LinearLayout layout;
    private EditText input;
    private Button button;
    private ListView listView;
    private MessagesAdapter listAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        // Input section
        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.HORIZONTAL);
        layout.addView(top);

        // Shout input
        input = new EditText(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(0, 50, 1));
        top.addView(input);

        // Shout button
        button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(70, 50));
        button.setText("Shout");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ZyreTest.MainActivity", "Send shout");
                sendShout(input.getText().toString());
                input.setText("");
            }
        });
        top.addView(button);

        // Messages list
        listView = new ListView(this);
        listAdapter = new MessagesAdapter(this);
        listView.setAdapter(listAdapter);
        layout.addView(listView);

        // Start ZyreService and listen for received messages
        Log.i("ZyreTest.MainActivity", "Start ZyreService");
        startService(new Intent(this, ZyreService.class));

        registerReceiver(messageReceiver, new IntentFilter(ZyreService.RECV_MESSAGE));

        // Refresh loop for timestamps
        updateHandler.postDelayed(updateInterval, 1000);
    }

    public void sendShout(String message_body) {
        Intent intent = new Intent(ZyreService.SEND_MESSAGE);
        intent.putExtra(ZyreService.MESSAGE_KIND, "SHOUT");
        intent.putExtra(ZyreService.MESSAGE_BODY, message_body);
        sendBroadcast(intent);
        Message message = new Message();
        message.created_at = Helpers.getTimestamp();
        message.kind = "SHOUT";
        message.sender = ZyreService.NODE_NAME;
        message.body = message_body;
        listAdapter.addMessage(message);
        updateMessages();
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String message_kind = bundle.getString(ZyreService.MESSAGE_KIND);
                String message_sender = bundle.getString(ZyreService.MESSAGE_SENDER);
                String message_body = bundle.getString(ZyreService.MESSAGE_BODY);
                receiveMessage(message_kind, message_sender, message_body);
            }
        }
    };

    public void receiveMessage(String message_kind, String message_sender, String message_body) {
        Log.i("ZyreTest.MainActivity.receiveMessage", message_kind + " from " + message_sender + ": " + message_body);
        Message message = new Message();
        message.created_at = Helpers.getTimestamp();
        message.kind = message_kind;
        message.body = message_body;
        message.sender = message_sender;
        listAdapter.addMessage(message);
        updateMessages();
    }

    public void updateMessages() {
        runOnUiThread(new Runnable() {
            public void run() {
                listAdapter.notifyDataSetChanged();
            }
        });
    }

    public Handler updateHandler = new Handler();
    public Thread updateInterval = new Thread(new Runnable() {
        @Override
        public void run() {
            updateMessages();
            updateHandler.postDelayed(updateInterval, 1000);
        }
    });

}
