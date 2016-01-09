//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.sprobertson.ZyreTest;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import org.zeromq.czmq.Zmsg;
import org.zeromq.czmq.Zsock;
import org.zeromq.zyre.Zyre;

public class ZyreService extends IntentService {
    Boolean run = false;
    Zyre node;
    Zsock socket;

    public static final String NODE_NAME = "atest";
    public static final String NODE_GROUP = "testgroup";

    public static final String RECV_MESSAGE
    ="com.sprobertson.ZyreTest.RECV_MESSAGE";

    public static final String SEND_MESSAGE
    ="com.sprobertson.ZyreTest.SEND_MESSAGE";

    public static final String MESSAGE_KIND
    ="com.sprobertson.ZyreTest.MESSAGE_KIND";

    public static final String MESSAGE_BODY
    ="com.sprobertson.ZyreTest.MESSAGE_BODY";

    public static final String MESSAGE_SENDER
    ="com.sprobertson.ZyreTest.MESSAGE_SENDER";

    public ZyreService() {
        super("ZyreService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        node = new Zyre(NODE_NAME);
        node.join(NODE_GROUP);
        node.start();
        run = true;

        registerReceiver(sendMessageReceiver, new IntentFilter(ZyreService.SEND_MESSAGE));

        while (run) {
            // TODO: Figure out proper polling 
            Zmsg message = node.recv();
            String message_kind = message.popstr();
            Log.i("ZyreTest.runloop", "Got a message: " + message_kind);
            if (message_kind.equals("SHOUT") || message_kind.equals("WHISPER")) {
                String message_sender;
                String message_body;
                message.popstr(); // ID String
                message_sender = message.popstr(); // Sender
                message.popstr(); // Group
                message_body = message.popstr(); // Body
                messageReceived(message_kind, message_sender, message_body);
            }
        }
    }

    // Sending a message

    private BroadcastReceiver sendMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String message_kind = bundle.getString(ZyreService.MESSAGE_KIND);
                String message_body = bundle.getString(ZyreService.MESSAGE_BODY);
                dispatchSendMessage(message_kind, message_body);
            }
        }
    };

    private void dispatchSendMessage(final String message_kind, final String message_body) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage(message_kind, message_body);
            }
        }).start();
    }

    private void sendMessage(String message_kind, String message_body) {
        if (message_kind.equals("SHOUT")) {
            node.shouts(NODE_GROUP, message_body);
        } else {
            // TODO: Send other message types
        }
    }

    // Receiving a message

    protected void messageReceived(String message_kind, String message_sender, String message_body) {
        Intent intent = new Intent(RECV_MESSAGE);
        intent.putExtra(MESSAGE_KIND, message_kind);
        intent.putExtra(MESSAGE_BODY, message_body);
        intent.putExtra(MESSAGE_SENDER, message_sender);
        sendBroadcast(intent);
    }

}
