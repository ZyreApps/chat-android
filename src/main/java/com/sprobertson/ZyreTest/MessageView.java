package com.sprobertson.ZyreTest;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageView extends LinearLayout {
    Message message;

    public MessageView(Context context, Message message) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        this.message = message;

        setPadding(5, 10, 5, 10);

        TextView sender_text = new TextView(context);
        sender_text.setText(message.sender);
        sender_text.setTypeface(null, Typeface.BOLD);
        addView(sender_text);

        TextView body_text = new TextView(context);
        body_text.setText(message.body);
        addView(body_text);
    }
}

