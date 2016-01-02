package phonebookpp.ytu.com.phonebookpp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.database.Cursor;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.model.Location;
import phonebookpp.ytu.com.phonebookpp.view.model.ContactViewHolder;

/**
 * Created by DARK on 1/1/2016.
 */
public class ContactDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ContactViewHolder contactDetails;
    private ListView callListView;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this, true);
        setContentView(R.layout.activity_contact_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Contact contact = (Contact) Contact.load(Contact.class, this.getIntent().getLongExtra("contact_id", -1));
        callListView = (ListView) this.findViewById(R.id.call_listview);
        this.contactDetails = new ContactViewHolder(this, this.findViewById(R.id.contact_detail_fragment), this);
        this.contact = contact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public void onClick(View view){
        if(view.getTag() instanceof Contact) {
            Contact contact = (Contact) view.getTag();

            switch (view.getId()) {
                case R.id.contact_call_button: {
                    final Spinner spinner = ContactDetailActivity.getSpinner(this, contact, ContactNumber.class);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setTitle("Make call");
                    alertDialog.setView(spinner);
                    alertDialog.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + ((String) spinner.getSelectedItem()).split("-")[1].trim()));
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                    break;
                }
                case R.id.contact_message_button: {
                    Toast.makeText(this, "MSG button clicked!", Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(this, MessagingActivity.class);

                    bundle.putSerializable("contact", contact);
                    intent.putExtras(bundle);
                    intent.putExtra("contact_id", contact.getId());
                    startActivity(intent);
                    break;
                }
            }
        }else if(view.getTag() instanceof Location){
            Location location = (Location) view.getTag();

            switch(view.getId()) {
                case R.id.contact_location: {
                    Toast.makeText(this, "Location clicked! " + location.latitude + "," + location.longtitude, Toast.LENGTH_LONG).show();
                    break;
                }
            }
        }
    }

    public void updateActivityView(){
        contactDetails.update(ContactDetailActivity.this, contact);
        callListView.setAdapter(new CallAdapter(this, contact.getCalls()));
    }

    @Override
    public void onResume(){
        super.onResume();

        updateActivityView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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

            // Setting Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.detail_menu_add_location);
            alertDialog.setView(layout);
            alertDialog.setPositiveButton("ADD",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String longtitude = input_longtitude.getText().toString();
                            String latitude = input_latitude.getText().toString();
                            if (longtitude.compareTo("") != 0 && latitude.compareTo("") != 0) {
                                Location loc = new Location();
                                loc.holder = contact;
                                loc.longtitude = Double.parseDouble(longtitude);
                                loc.latitude = Double.parseDouble(latitude);
                                loc.type = (ContactInfoType) input_info_type.getSelectedItem();
                                loc.save();

                                Toast.makeText(getApplicationContext(), "Location saved for " + contact.name + " " + contact.surname + ".", Toast.LENGTH_SHORT).show();
                                updateActivityView();
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
            alertDialog.setTitle(R.string.detail_menu_add_number);
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
                                updateActivityView();
                            }
                        }
                    });

            alertDialog.show(); // Showing Alert Message
            return true;
        }else if(id == R.id.detail_menu_delete_number){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            // Info Type
            LinearLayout layout_info_type = new LinearLayout(this);
            layout_info_type.setOrientation(LinearLayout.HORIZONTAL);
            TextView label_info_type = new TextView(this);
            label_info_type.setText("Number: ");
            layout_info_type.addView(label_info_type);
            final Spinner input_info_type = getSpinner(this, contact, ContactNumber.class);
            if(input_info_type.getAdapter().getCount() == 0){
                Toast.makeText(getApplicationContext(), "There is not any phone number entry to delete.", Toast.LENGTH_SHORT).show();
            }
            else {
                layout_info_type.addView(input_info_type);
                layout.addView(layout_info_type);

                // Setting Dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.detail_menu_delete_number);
                alertDialog.setView(layout);
                alertDialog.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String line = (String) input_info_type.getSelectedItem();
                                String[] parts = line.split(" - ");

                                Object number = new Select().from(ContactNumber.class).where("holder = ? AND type = ? AND number = ?", contact.getId(), ContactInfoType.valueOf(parts[0]), parts[1]).executeSingle();
                                if(number != null) {
                                    ((ContactNumber)number).deletex();
                                    updateActivityView();

                                    Toast.makeText(getApplicationContext(),
                                            "Removed the phone number of " + contact.name + " " + contact.surname + ".", Toast.LENGTH_SHORT).show();
                                }else
                                    Toast.makeText(ContactDetailActivity.this, "DB fail!", Toast.LENGTH_SHORT).show();
                            }
                        });

                alertDialog.show(); // Showing Alert Message
            }
            return true;
        }else if(id == R.id.detail_menu_delete_location){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            // Info Type
            LinearLayout layout_info_type = new LinearLayout(this);
            layout_info_type.setOrientation(LinearLayout.HORIZONTAL);
            TextView label_info_type = new TextView(this);
            label_info_type.setText("Location: ");
            layout_info_type.addView(label_info_type);
            final Spinner input_info_type = getSpinner(this, contact, Location.class);
            if(input_info_type.getAdapter().getCount() == 0){
                Toast.makeText(getApplicationContext(), "There is not any location entry to delete.", Toast.LENGTH_SHORT).show();
            }
            else {
                layout_info_type.addView(input_info_type);
                layout.addView(layout_info_type);

                // Setting Dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.detail_menu_delete_location);
                alertDialog.setView(layout);
                alertDialog.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String line = (String) input_info_type.getSelectedItem();
                                String[] parts = line.split(" - ");
                                String[] subline =  parts[1].split(",");
                                //Toast.makeText(ContactDetailActivity.this, line + Arrays.toString(parts) + Arrays.toString(subline), Toast.LENGTH_LONG).show();
                                /*Cursor cursor = ActiveAndroid.getDatabase().rawQuery("PRAGMA table_info(ContactNumber)", null);
                                cursor.moveToFirst();

                                do{
                                    Toast.makeText(ContactDetailActivity.this, cursor.getString(1) + " - wtf", Toast.LENGTH_LONG).show();
                                }while(cursor.moveToNext());
                                Toast.makeText(ContactDetailActivity.this, " this is weird... - wtf", Toast.LENGTH_LONG);*/ 
                                new Delete().from(Location.class).where("holder = ? AND type = ? AND latitude = ? AND longtitude = ?", contact.getId(), ContactInfoType.valueOf(parts[0]), Double.parseDouble(subline[0]), Double.parseDouble(subline[1])).execute();

                                updateActivityView();

                                Toast.makeText(getApplicationContext(),
                                        "Removed the location of " + contact.name + " " + contact.surname + ".", Toast.LENGTH_SHORT).show();
                            }
                        });
                alertDialog.show(); // Showing Alert Message
            }
            return true;
        }else if(id == R.id.detail_menu_delete_contact){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);

            // Setting Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.detail_menu_delete_contact);
            alertDialog.setMessage("Are you sure to remove " + contact.name + " " + contact.surname + "?");
            alertDialog.setView(layout);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            contact.deleted = true;
                            contact.save();

                            finish();
                            Toast.makeText(getApplicationContext(), "Removed the contact.", Toast.LENGTH_SHORT).show();
                        }
                    });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} });
            alertDialog.show(); // Showing Alert Message
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Spinner getSpinner(Context context, Contact contact, Class<?> cls){
        ArrayList<String> spinnerArray = new ArrayList<>();

        if(cls == ContactNumber.class){
            List<ContactNumber> list = contact.getNumbers();
            for(ContactNumber it : list) spinnerArray.add(it.type + " - " + it.number);
        }
        else if(cls == Location.class){
            List<Location> list = contact.getLocations();
            for(Location it : list) spinnerArray.add(it.type + " - " + it.latitude + "," + it.longtitude);
        }

        Spinner spinner = new Spinner(context);
        spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, spinnerArray));
        return spinner;
    }
}