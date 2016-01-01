package phonebookpp.ytu.com.phonebookpp.view.model;

import phonebookpp.ytu.com.phonebookpp.R;
import phonebookpp.ytu.com.phonebookpp.model.SMSMessage;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by DARK on 1/2/2016.
 */
public class MessageViewHolder {
    private TextView bodyView;

    public MessageViewHolder(View view){
        bodyView = (TextView) view.findViewById(R.id.message_body);
    }

    public void update(SMSMessage message){
        bodyView.setText(message.body);
        bodyView.setGravity(message.outgoing ? Gravity.RIGHT : Gravity.LEFT);
    }
}
