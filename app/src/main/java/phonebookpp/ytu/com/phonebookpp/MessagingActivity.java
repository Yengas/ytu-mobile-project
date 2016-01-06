package phonebookpp.ytu.com.phonebookpp;

import android.content.DialogInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Date;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.model.SMSMessage;

public class MessagingActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView messageListView;
    private MessageAdapter messagingAdapter;
    private EditText my_message;
    ContactNumber phoneNumber;
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

        my_message = (EditText) findViewById(R.id.messaging_body);
    }

    public void sendSMS(String message){
        SMSMessage newSMS = new SMSMessage();
        newSMS.body = message;
        newSMS.outgoing = true;
        newSMS.addressee = phoneNumber;
        newSMS.date = new Date();
        newSMS.save();

        android.telephony.SmsManager sms=android.telephony.SmsManager.getDefault();

        sms.sendTextMessage(phoneNumber.number, null, message, null, null);

        // Reset my message
        my_message.setText("");
        Toast.makeText(getApplicationContext(), "Sent SMS to " + phoneNumber.number, Toast.LENGTH_SHORT).show();

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
                final String message = my_message.getText().toString();
                if(message.compareTo("") != 0) { // If not empty,
                    if(phoneNumber == null){ // Do we need to select phone number?
                        LinearLayout layout = new LinearLayout(this);
                        layout.setOrientation(LinearLayout.VERTICAL);

                        // Info Type
                        LinearLayout layout_info_type = new LinearLayout(this);
                        layout_info_type.setOrientation(LinearLayout.HORIZONTAL);
                        TextView label_info_type = new TextView(this);
                        label_info_type.setText("Number: ");
                        layout_info_type.addView(label_info_type);
                        final Spinner input_info_type = ContactDetailActivity.getSpinner(this, contact, ContactNumber.class);
                        if(input_info_type.getAdapter().getCount() == 0){
                            Toast.makeText(getApplicationContext(), "There is not any phone number entry to message.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            layout_info_type.addView(input_info_type);
                            layout.addView(layout_info_type);

                            // Setting Dialog
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                            alertDialog.setTitle("Send SMS to");
                            alertDialog.setView(layout);
                            alertDialog.setPositiveButton("Choose",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            String line = (String) input_info_type.getSelectedItem();
                                            String[] parts = line.split(" - ");

                                            Object number = new Select().from(ContactNumber.class).where("holder = ? AND type = ? AND number = ?", contact.getId(), ContactInfoType.valueOf(parts[0]), parts[1]).executeSingle();
                                            if (number != null) {
                                                phoneNumber = (ContactNumber) number;
                                                sendSMS(message);
                                            } else
                                                Toast.makeText(getApplicationContext(), "DB fail!", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            alertDialog.show(); // Showing Alert Message
                        }
                    }
                    else {
                        sendSMS(message);
                    }
                }
                break;
            }
        }
    }
}
