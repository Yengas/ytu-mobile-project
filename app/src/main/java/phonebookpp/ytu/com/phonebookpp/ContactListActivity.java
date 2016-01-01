package phonebookpp.ytu.com.phonebookpp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ListView;
import android.content.Intent;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.List;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
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
        List<Contact> contacts =  new Select().from(Contact.class).execute();
        ((ListView) this.findViewById(R.id.contact_listview)).setAdapter(new ContactAdapter(this, this, contacts));
    }

    @Override
    public void onClick(View view){
        Contact contact = (Contact) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", contact);

        switch(view.getId()){
            case R.id.contact_call_button:{
                Toast.makeText(this, "Call button clicked!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, ContactDetailActivity.class);

                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case R.id.contact_message_button:{
                Toast.makeText(this, "MSG button clicked!", Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
