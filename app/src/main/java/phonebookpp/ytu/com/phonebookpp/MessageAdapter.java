package phonebookpp.ytu.com.phonebookpp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import phonebookpp.ytu.com.phonebookpp.model.SMSMessage;
import phonebookpp.ytu.com.phonebookpp.view.model.MessageViewHolder;

/**
 * Created by DARK on 1/1/2016.
 */
public class MessageAdapter extends BaseAdapter {
    private List<SMSMessage> messages = null;
    private Activity context;

    public MessageAdapter(Activity context, List<SMSMessage> messages){
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getCount(){
        return this.messages.size();
    }

    @Override
    public Object getItem(int pos){
        return this.messages.get(pos);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null || convertView.getTag() == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_message, null);
            convertView.setTag( new MessageViewHolder(convertView) );
        }
        ((MessageViewHolder) convertView.getTag()).update((SMSMessage) this.getItem(position));
        return convertView;
    }

}
