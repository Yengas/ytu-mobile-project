package phonebookpp.ytu.com.phonebookpp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import phonebookpp.ytu.com.phonebookpp.view.model.ContactViewHolder;
import phonebookpp.ytu.com.phonebookpp.model.Contact;

/**
 * Created by DARK on 1/1/2016.
 */
public class MessagingActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView messageListView;
    private MessageAdapter messagingAdapter;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this, true);
        setContentView(R.layout.activity_messaging);

        this.contact = (Contact) Contact.load(Contact.class, this.getIntent().getLongExtra("contact_id", -1));
        this.messageListView = (ListView) this.findViewById(R.id.messaging_listview);

        this.update();
        ((Button) this.findViewById(R.id.messaging_send_button)).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.messaging_contact_name)).setText(contact.name + " " + contact.surname);

        filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(998);

        receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent){
                update();
            }
        };
    }

    public void update(){
        messageListView.setAdapter(this.messagingAdapter = new MessageAdapter(this, contact.getMessages()));
    }

    @Override
    public void onResume(){
        super.onResume();
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void onPause(){
        super.onPause();
        this.unregisterReceiver(receiver);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.messaging_send_button:{
                Toast.makeText(this, "Message send button clicked!", Toast.LENGTH_SHORT);
                break;
            }
        }
    }
}
