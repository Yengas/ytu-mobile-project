package phonebookpp.ytu.com.phonebookpp.view.model;

import phonebookpp.ytu.com.phonebookpp.R;
import phonebookpp.ytu.com.phonebookpp.model.Call;

import java.text.SimpleDateFormat;

import android.view.View;
import android.widget.TextView;

/**
 * Created by DARK on 1/1/2016.
 */
public class CallViewHolder {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private TextView dateView, durationView, typeView;

    public CallViewHolder(View view){
        this.dateView = (TextView) view.findViewById(R.id.call_date);
        this.durationView = (TextView) view.findViewById(R.id.call_duration);
        this.typeView = (TextView) view.findViewById(R.id.call_type);
    }

    public void update(Call call){
        this.dateView.setText(formatter.format(call.date));
        this.durationView.setText(call.duration > 0 || call.completed ? (call.duration + " seconds") : ( call.outgoing ? "Not Answered" : "Missed" ));
        this.typeView.setText(call.outgoing ? "OUT" : "INC");
    }
}
