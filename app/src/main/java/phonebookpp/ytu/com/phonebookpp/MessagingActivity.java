package phonebookpp.ytu.com.phonebookpp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this, true);
        setContentView(R.layout.activity_messaging);

        Contact contact = (Contact) this.getIntent().getExtras().getSerializable("contact");
        this.messageListView = (ListView) this.findViewById(R.id.messaging_listview);

        messageListView.setAdapter(this.messagingAdapter = new MessageAdapter(this, contact.getMessages()));
        ((Button) this.findViewById(R.id.messaging_send_button)).setOnClickListener(this);
        ((TextView) this.findViewById(R.id.messaging_contact_name)).setText(contact.name + " " + contact.surname);
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
