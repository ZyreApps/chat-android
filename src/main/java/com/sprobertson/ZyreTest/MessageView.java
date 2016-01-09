//  This Source Code Form is subject to the terms of the Mozilla Public
//  License, v. 2.0. If a copy of the MPL was not distributed with this
//  file, You can obtain one at http://mozilla.org/MPL/2.0/.

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

        setPadding(5, 5, 5, 5);

        LinearLayout left_top = new LinearLayout(context);
        left_top.setOrientation(LinearLayout.HORIZONTAL);

        TextView sender_text = new TextView(context);
        sender_text.setText(message.sender);
        sender_text.setTypeface(null, Typeface.BOLD);
        sender_text.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        left_top.addView(sender_text);

        TextView time_text = new TextView(context);
        time_text.setText(Helpers.getTimeAgo(message.created_at));
        left_top.addView(time_text);

        addView(left_top);

        TextView body_text = new TextView(context);
        body_text.setText(message.body);
        addView(body_text);
    }
}

