package phonebookpp.ytu.com.phonebookpp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.Location;
import phonebookpp.ytu.com.phonebookpp.utils.DatabaseFiller;

public class ContactListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this, true);
        setContentView(R.layout.activity_contact_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(DatabaseFiller.filledBefore() == false){
            DatabaseFiller.fill();
        }
    }

    public void update(){
        List<Contact> contacts =  new Select().from(Contact.class).where("deleted = ?", false).execute();
        ((ListView) this.findViewById(R.id.contact_listview)).setAdapter(new ContactAdapter(this, this, contacts));
    }

    @Override
    public void onResume(){
        super.onResume();
        update();
    }

    @Override
    public void onClick(View view){
        if(view.getTag() instanceof Contact) {
            Contact contact = (Contact) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact", contact);

            switch (view.getId()) {
                case R.id.contact_call_button: {
                    Toast.makeText(this, "Call button clicked!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, ContactDetailActivity.class);

                    intent.putExtras(bundle);
                    intent.putExtra("contact_id", contact.getId());
                    startActivity(intent);
                    break;
                }
                case R.id.contact_message_button: {
                    Toast.makeText(this, "MSG button clicked!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, MessagingActivity.class);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.list_menu_add) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
                // Name
                LinearLayout layout_name = new LinearLayout(this);
                layout_name.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_name = new TextView(this);
                    label_name.setText("Name: ");
                layout_name.addView(label_name);
                    final EditText input_name = new EditText(this);
                    input_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                layout_name.addView(input_name);
            layout.addView(layout_name);

                // Surname
                LinearLayout layout_surname = new LinearLayout(this);
                layout_surname.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_surname = new TextView(this);
                    label_surname.setText("Surname: ");
                layout_surname.addView(label_surname);
                    final EditText input_surname = new EditText(this);
                    input_surname.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                layout_surname.addView(input_surname);
            layout.addView(layout_surname);
            
                // Email
                LinearLayout layout_email = new LinearLayout(this);
                layout_email.setOrientation(LinearLayout.HORIZONTAL);
                    TextView label_email = new TextView(this);
                    label_email.setText("Email: ");
                layout_email.addView(label_email);
                    final EditText input_email = new EditText(this);
                    input_email.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                layout_email.addView(input_email);
            layout.addView(layout_email);

            // Setting Dialog
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Add Number");
            alertDialog.setView(layout);
            alertDialog.setPositiveButton("ADD",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String name = input_name.getText().toString();
                            String surname = input_surname.getText().toString();
                            String email = input_email.getText().toString();

                            if (name.compareTo("") != 0) {
                                Contact con = new Contact();
                                con.name = name;
                                con.surname = surname;
                                con.email = email;
                                con.save();

                                Toast.makeText(getApplicationContext(),
                                        "New contact \"" + con.name + " " + con.surname + "\" is saved.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            alertDialog.show(); // Showing Alert Message
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
