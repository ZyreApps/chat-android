//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.

package com.sprobertson.ZyreTest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {
    public ArrayList<Message> messages = new ArrayList<Message>();
    private Context context;

    public MessagesAdapter(Context c) {
        context = c;
    }

    public void clearMessages() {
        messages.clear();
    }

    public void addMessage(Message message) {
        messages.add(0, message);
    }

    public int getCount() {
        return messages.size();
    }

    public Object getItem(int position) {
        return messages.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = (Message) getItem(position);
        MessageView message_view = new MessageView(context, message);
        return message_view;
    }

}

