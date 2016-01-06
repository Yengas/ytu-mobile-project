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

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.view.model.ContactViewHolder;

public class ContactAdapter extends BaseAdapter {
    private List<Contact> contacts = null;
    private Activity context;
    private View.OnClickListener listener;

    public ContactAdapter(Activity context, View.OnClickListener listener, List<Contact> contacts){
            this.contacts = contacts;
            this.context = context;
            this.listener = listener;
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
            convertView.setTag( new ContactViewHolder(context, convertView, listener) );
        }
        ((ContactViewHolder) convertView.getTag()).update(listener, (Contact) this.getItem(position));
        return convertView;
    }

}
