package phonebookpp.ytu.com.phonebookpp.view.model;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import phonebookpp.ytu.com.phonebookpp.R;
import phonebookpp.ytu.com.phonebookpp.model.Contact;

/**
 * Created by DARK on 1/1/2016.
 */
public class ContactViewHolder {
    private TextView fullNameView, mailView, callMissedView, newMessagesView, callsView;
    private LinearLayout contactLocationListLayout;
    private View callView, messageView;
    private Contact contact;

    public ContactViewHolder(View view, View.OnClickListener listener){
        fullNameView = (TextView) view.findViewById(R.id.contact_full_name);
        mailView = (TextView) view.findViewById(R.id.contact_mail);
        callMissedView = (TextView) view.findViewById(R.id.contact_missed_calls);
        newMessagesView = (TextView) view.findViewById(R.id.contact_new_messages);
        callsView = (TextView) view.findViewById(R.id.contact_calls);
        contactLocationListLayout = (LinearLayout) view.findViewById(R.id.contact_location_list);
        callView = (View) view.findViewById(R.id.contact_call_button);
        messageView = (View) view.findViewById(R.id.contact_message_button);

        callView.setOnClickListener(listener);
        messageView.setOnClickListener(listener);
    }

    public void update(Contact contact){
        this.contact = contact;
        contact.load();

        fullNameView.setText(contact.name + " " + contact.surname);
        mailView.setText(contact.email);
        callMissedView.setText(String.valueOf(contact.missingCalls));
        newMessagesView.setText(contact.sentMessages + "s/" + contact.recievedMessages + "r");
        callsView.setText(contact.outgoingCallDuration + "s o/" + contact.incomingCallDuration + "s i");
        callView.setTag(contact);
        messageView.setTag(contact);
    }
}
