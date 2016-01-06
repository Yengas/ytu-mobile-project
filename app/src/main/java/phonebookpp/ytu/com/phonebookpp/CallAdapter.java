package phonebookpp.ytu.com.phonebookpp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import phonebookpp.ytu.com.phonebookpp.model.Call;
import phonebookpp.ytu.com.phonebookpp.view.model.CallViewHolder;

public class CallAdapter extends BaseAdapter {
    private List<Call> calls = null;
    private Activity context;

    public CallAdapter(Activity context, List<Call> calls){
            this.calls = calls;
            this.context = context;
    }

    @Override
    public int getCount(){
        return this.calls.size();
    }

    @Override
    public Object getItem(int pos){
        return this.calls.get(pos);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null || convertView.getTag() == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_call, null);
            convertView.setTag( new CallViewHolder(convertView) );
        }
        ((CallViewHolder) convertView.getTag()).update((Call) this.getItem(position));
        return convertView;
    }

}
