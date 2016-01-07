package phonebookpp.ytu.com.phonebookpp;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.Select;

import java.util.List;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.Location;
import phonebookpp.ytu.com.phonebookpp.utils.DatabaseFiller;
import phonebookpp.ytu.com.phonebookpp.utils.ImportUtils;
import phonebookpp.ytu.com.phonebookpp.view.model.ContactViewHolder;

/**
 * Created by DARK on 1/7/2016.
 */
public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActiveAndroid.initialize(this, true);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layout = (LinearLayout) this.findViewById(R.id.report_view);
    }

    @Override
    public void onResume(){
        super.onResume();
        this.update();
    }

    public void update(){
        layout.removeAllViews();
        Cursor cursor = Cache.openDatabase().rawQuery("SELECT addressee, COUNT(id) as times FROM Call WHERE (completed = 1 OR duration <= 0) AND outgoing = 0 GROUP BY addressee ORDER BY times DESC LIMIT 1", null);

        if(cursor.moveToFirst()) {
            this.add("Most missing calls", (Contact) Contact.load(Contact.class, cursor.getLong(0)));
        }

        cursor = Cache.openDatabase().rawQuery("SELECT addressee, SUM(duration) as total FROM Call WHERE outgoing = 1 GROUP BY addressee ORDER BY total DESC LIMIT 1", null);
        if(cursor.moveToFirst()) {
            this.add("Most outgoing duration", (Contact) Contact.load(Contact.class, cursor.getLong(0)));
        }

        cursor = Cache.openDatabase().rawQuery("SELECT addressee, SUM(duration) as total FROM Call WHERE outgoing = 0 GROUP BY addressee ORDER BY total DESC LIMIT 1", null);
        if(cursor.moveToFirst()) {
            this.add("Most incoming duration", (Contact) Contact.load(Contact.class, cursor.getLong(0)));
        }

        cursor = Cache.openDatabase().rawQuery("SELECT addressee, SUM(duration) as total FROM Call GROUP BY addressee ORDER BY total DESC LIMIT 1", null);
        if(cursor.moveToFirst()) {
            this.add("Most duration", (Contact) Contact.load(Contact.class, cursor.getLong(0)));
        }
    }

    public void add(String title, Contact contact){
        TextView textView = new TextView(this);
        View contactView = getLayoutInflater().inflate(R.layout.fragment_contact, null);

        textView.setText(title);
        textView.setTextSize(24);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        new ContactViewHolder(this, contactView, this).update(this, contact);
        layout.addView(textView);
        layout.addView(contactView);
    }

    @Override
    public void onClick(View view){
        if(view.getTag() instanceof Contact) {
            Contact contact = (Contact) view.getTag();
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact", contact);

            switch (view.getId()) {
                case R.id.contact_call_button: {
                    Intent intent = new Intent(this, ContactDetailActivity.class);

                    intent.putExtras(bundle);
                    intent.putExtra("contact_id", contact.getId());
                    startActivity(intent);
                    break;
                }
                case R.id.contact_message_button: {
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
}
