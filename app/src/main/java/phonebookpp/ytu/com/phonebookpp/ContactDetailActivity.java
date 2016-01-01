package phonebookpp.ytu.com.phonebookpp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;

import phonebookpp.ytu.com.phonebookpp.view.model.ContactViewHolder;
import phonebookpp.ytu.com.phonebookpp.model.Contact;

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.detail_menu_edit) {
            return true;
        }else if(id == R.id.detail_menu_add_location){
            return true;
        }else if(id == R.id.detail_menu_add_number){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
