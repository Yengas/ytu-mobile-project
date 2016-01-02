package phonebookpp.ytu.com.phonebookpp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import java.util.ArrayList;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.model.Location;
import phonebookpp.ytu.com.phonebookpp.view.model.ContactViewHolder;

/**
 * Created by DARK on 1/1/2016.
 */
public class ContactDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this, true);
        setContentView(R.layout.activity_contact_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Contact contact = (Contact) this.getIntent().getExtras().getSerializable("contact");
        ListView listView = (ListView) this.findViewById(R.id.call_listview);

        new ContactViewHolder(this.findViewById(R.id.contact_detail_fragment), this).update(contact);
        listView.setAdapter(new CallAdapter(this, contact.getCalls()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public void onClick(View view){
        Contact contact = (Contact) view.getTag();

        switch(view.getId()){
            case R.id.contact_call_button:{
                Toast.makeText(this, "Make the call! With number selection!", Toast.LENGTH_LONG).show();
                //TODO: start call activity with dialog selection of numbers.
                break;
            }
            case R.id.contact_message_button:{
                Toast.makeText(this, "MSG button clicked!", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, MessagingActivity.class);

                bundle.putSerializable("contact", contact);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final Contact contact = (Contact) this.getIntent().getExtras().getSerializable("contact");

        //noinspection SimplifiableIfStatement
        if (id == R.id.detail_menu_edit) {
            return true;
        }else if(id == R.id.detail_menu_add_location){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
                // Info Type
                LinearLayout layout_info_type = new LinearLayout(this);
                layout_info_type.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_info_type = new TextView(this);
                    label_info_type.setText("Type: ");
                layout_info_type.addView(label_info_type);
                    ArrayList<ContactInfoType> spinnerArray = new ArrayList<>();
                    spinnerArray.add(ContactInfoType.HOME);
                    spinnerArray.add(ContactInfoType.WORK);
                    final Spinner input_info_type = new Spinner(this);
                    ArrayAdapter<ContactInfoType> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    input_info_type.setAdapter(spinnerArrayAdapter);
                layout_info_type.addView(input_info_type);
            layout.addView(layout_info_type);

                // Longtitude
                LinearLayout layout_longtitude = new LinearLayout(this);
                layout_longtitude.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_longtitude = new TextView(this);
                    label_longtitude.setText("Longtitute: ");
                layout_longtitude.addView(label_longtitude);
                    final EditText input_longtitude = new EditText(this);
                    input_longtitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    input_longtitude.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                layout_longtitude.addView(input_longtitude);
            layout.addView(layout_longtitude);

                // Latitude
                LinearLayout layout_latitude = new LinearLayout(this);
                layout_latitude.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_latitude = new TextView(this);
                    label_latitude.setText("Latitude: ");
                layout_latitude.addView(label_latitude);
                    final EditText input_latitude = new EditText(this);
                    input_latitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    input_latitude.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                layout_latitude.addView(input_latitude);
            layout.addView(layout_latitude);

            // Setting Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Add Location");
            alertDialog.setView(layout);
            alertDialog.setPositiveButton("ADD",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String longtitude = input_longtitude.getText().toString();
                            String latitude = input_latitude.getText().toString();
                            if (longtitude.compareTo("") == 0 && latitude.compareTo("") == 0) {
                                Location loc = new Location();
                                loc.holder = contact;
                                loc.longtitude = Double.parseDouble(longtitude);
                                loc.latitude = Double.parseDouble(latitude);
                                loc.type = (ContactInfoType) input_info_type.getSelectedItem();
                                loc.save();

                                Toast.makeText(getApplicationContext(),
                                        "Location saved for " + contact.name + " " + contact.surname + ".", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


            alertDialog.show(); // Showing Alert Message
            return true;
        }else if(id == R.id.detail_menu_add_number){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
                // Info Type
                LinearLayout layout_info_type = new LinearLayout(this);
                layout_info_type.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_info_type = new TextView(this);
                    label_info_type.setText("Type: ");
                layout_info_type.addView(label_info_type);
                    ArrayList<ContactInfoType> spinnerArray = new ArrayList<>();
                    spinnerArray.add(ContactInfoType.MOBILE);
                    spinnerArray.add(ContactInfoType.HOME);
                    spinnerArray.add(ContactInfoType.WORK);
                    final Spinner input_info_type = new Spinner(this);
                    ArrayAdapter<ContactInfoType> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
                    input_info_type.setAdapter(spinnerArrayAdapter);
                layout_info_type.addView(input_info_type);
            layout.addView(layout_info_type);

                // Number
                LinearLayout layout_number = new LinearLayout(this);
                layout_number.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_number = new TextView(this);
                    label_number.setText("Number: ");
                layout_number.addView(label_number);
                    final EditText input_number = new EditText(this);
                    input_number.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    input_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                layout_number.addView(input_number);
            layout.addView(layout_number);

            // Setting Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Add Number");
            alertDialog.setView(layout);
            alertDialog.setPositiveButton("ADD",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String number = input_number.getText().toString();

                            if (number.compareTo("") != 0) {
                                ContactNumber cnumber = new ContactNumber();
                                cnumber.holder = contact;
                                cnumber.number = number;
                                cnumber.type = (ContactInfoType) input_info_type.getSelectedItem();
                                cnumber.save();

                                Toast.makeText(getApplicationContext(),
                                        "Phone number saved for " + contact.name + " " + contact.surname + ".", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            alertDialog.show(); // Showing Alert Message
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
