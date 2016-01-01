package phonebookpp.ytu.com.phonebookpp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import phonebookpp.ytu.com.phonebookpp.models.Contact;

/**
 * Created by DARK on 1/1/2016.
 */
public class ContactAdapter extends BaseAdapter {
    private List<Contact> contacts = null;
    private Activity context;

    public ContactAdapter(Activity context, List<Contact> contacts){
            this.contacts = contacts;
            this.context = context;
    }

    @Override
    public int getCount(){
        return this.contacts.size();
    }

    @Override
    public Object getItem(int pos){
        return this.contacts.get(pos);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null || convertView.getTag() == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_contact, null);
            convertView.setTag( new ContactViewHolder(convertView) );
        }
        ((ContactViewHolder) convertView.getTag()).update((Contact) this.getItem(position));
        return convertView;
    }

    class ContactViewHolder{
        private TextView fullNameView, mailView, callMissedView, newMessagesView, callsView;
        private LinearLayout contactLocationListLayout;

        public ContactViewHolder(View view){
            fullNameView = (TextView) view.findViewById(R.id.contact_full_name);
            mailView = (TextView) view.findViewById(R.id.contact_mail);
            callMissedView = (TextView) view.findViewById(R.id.contact_missed_calls);
            newMessagesView = (TextView) view.findViewById(R.id.contact_new_messages);
            callsView = (TextView) view.findViewById(R.id.contact_calls);
            contactLocationListLayout = (LinearLayout) view.findViewById(R.id.contact_location_list);
        }

        public void update(Contact contact){
            fullNameView.setText(contact.name + " " + contact.surname);
            mailView.setText(contact.email);
            contact.load();
            callMissedView.setText(String.valueOf(contact.missingCalls));
            newMessagesView.setText(contact.sentMessages + "s/" + contact.recievedMessages + "r");
            callsView.setText(contact.outgoingCallDuration + "s o/" + contact.incomingCallDuration + "s i");
        }
    }

}
