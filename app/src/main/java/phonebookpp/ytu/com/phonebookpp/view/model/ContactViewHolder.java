package phonebookpp.ytu.com.phonebookpp.view.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import phonebookpp.ytu.com.phonebookpp.R;
import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.Location;

public class ContactViewHolder {
    private TextView fullNameView, mailView, callMissedView, newMessagesView, callsView;
    private LinearLayout contactLocationListLayout;
    private View callView, messageView;
    private Contact contact;
    private Context context;

    public ContactViewHolder(Context context, View view, View.OnClickListener listener){
        this.context = context;
        fullNameView = (TextView) view.findViewById(R.id.contact_full_name);
        mailView = (TextView) view.findViewById(R.id.contact_mail);
        callMissedView = (TextView) view.findViewById(R.id.contact_missed_calls);
        newMessagesView = (TextView) view.findViewById(R.id.contact_new_messages);
        callsView = (TextView) view.findViewById(R.id.contact_calls);
        contactLocationListLayout = (LinearLayout) view.findViewById(R.id.contact_location_list);
        callView = (View) view.findViewById(R.id.contact_call_button);
        messageView = (View) view.findViewById(R.id.contact_message_button);

        callView.setOnClickListener(listener);
        messageView.setOnClickListener(listener);
    }

    public void update(View.OnClickListener listener, Contact contact){
        this.contact = contact;
        contact.load();

        fullNameView.setText(contact.name + (contact.surname == null ? "" : " " + contact.surname));
        mailView.setText(contact.email);
        callMissedView.setText(String.valueOf(contact.missingCalls));
        newMessagesView.setText(contact.sentMessages + "s/" + contact.recievedMessages + "r");
        callsView.setText(contact.outgoingCallDuration + "s o/" + contact.incomingCallDuration + "s i");
        callView.setTag(contact);
        messageView.setTag(contact);

        // TODO: You may re-use the LocationViewHolders inside this linearlayout, but aint no got time for that.
        contactLocationListLayout.removeAllViews();
        for(Location location : contact.getLocations()) {
            LocationViewHolder holder = new LocationViewHolder(context, location);

            holder.setOnClickListener(listener);
            contactLocationListLayout.addView(holder);
        }
    }

    class LocationViewHolder extends LinearLayout{
        private TextView locationNameView;
        private ImageView image;

        public LocationViewHolder(Context context, Location location){
            super(context);
            this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.setOrientation(LinearLayout.HORIZONTAL);
            this.setId(R.id.contact_location);
            this.locationNameView = new TextView(context);
            this.image = new ImageView(context);

            image.setImageResource(R.drawable.location);
            image.setLayoutParams(new LayoutParams(25, 25));
            image.setAdjustViewBounds(true);

            locationNameView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            this.addView(image);
            this.addView(locationNameView);
            this.update(location);
        }

        public void update(Location location){
            locationNameView.setText(location.type.toString());
            locationNameView.setTag(location);
            image.setTag(location);
            this.setTag(location);
        }
    }
}
