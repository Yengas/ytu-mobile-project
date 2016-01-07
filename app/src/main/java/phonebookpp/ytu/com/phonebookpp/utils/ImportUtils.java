package phonebookpp.ytu.com.phonebookpp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import phonebookpp.ytu.com.phonebookpp.model.Call;
import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;

public class ImportUtils {
    public static final String PREF_LAST_NAME = "LAST_ID_PREFX", LAST_CALL_LOG = "LAST_CALL_LOG_ID", LAST_CONTACT = "LAST_CONTACT_ID";

    public static class CallInfo{
        public final long id;
        public final String number;
        public final int type, duration;
        public final Date date;

        public CallInfo(Cursor cursor){
            this.id = cursor.getLong(cursor.getColumnIndex(CallLog.Calls._ID));
            this.number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            this.duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            this.type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            this.date = new Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)));
            Log.e("PPP_DEBUG", type + ": " + number);
        }
    }

    public static class ContactInfo{
        public final long id;
        public final String name, email;
        private final List<String> numbers = new ArrayList<String>();

        public ContactInfo(Context context, Cursor cursor){
            this.id = cursor.getLong(cursor.getColumnIndex(Contacts._ID));
            this.name = cursor.getString(cursor.getColumnIndex(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? Contacts.DISPLAY_NAME_PRIMARY : Contacts.DISPLAY_NAME));
            this.email = "";

            Log.e("PPP_DEBUG", id + ": " + name);
            if(cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 1){
                Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + this.id, null, null);

                while (phoneCursor.moveToNext()) {
                    numbers.add(PPCommon.sanitizeNumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                }
                phoneCursor.close();
            }
        }

        public List<String> getNumbers(){
            return numbers;
        }
    }

    private static long importCall(CallInfo info){
        Object addressee = new Select().from(ContactNumber.class).where("number = ?", PPCommon.sanitizeNumber(info.number)).executeSingle();

        if(addressee != null) {
            Call call = new Call();

            call.duration = info.duration;
            call.completed = info.type != CallLog.Calls.MISSED_TYPE && info.duration > 0;
            call.outgoing = info.type == CallLog.Calls.OUTGOING_TYPE;
            call.date = info.date;
            call.addressee = (ContactNumber) addressee;
            return call.save();
        }

        return -1;
    }

    private static long importContact(ContactInfo info){
        Contact contact = new Contact();
        String[] split = PPCommon.sanitizeName(info.name);

        contact.name = split[0];
        if(split.length > 1) contact.surname = split[1];
        contact.email = info.email;
        long result = contact.save();
        if(result == -1) return result; // Return if we couldn't save the contact.

        for(String number : info.getNumbers()){
            ContactNumber numberObj = new ContactNumber();

            numberObj.number = number;
            numberObj.type = ContactInfoType.MOBILE;
            numberObj.holder = contact;
            numberObj.save();
        }

        return result;
    }

    public static int getLatestCall(Context context){
        SharedPreferences prefs = context.getSharedPreferences(ImportUtils.PREF_LAST_NAME, Context.MODE_PRIVATE);
        String lastCallID = prefs.getString(LAST_CALL_LOG, "-1"), starting = lastCallID;
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls._ID + " > ?", new String[]{lastCallID}, CallLog.Calls._ID + " ASC");
        int total = 0;
        if(cursor.moveToFirst() == false) return total;
        long lastId = -1;

        ActiveAndroid.initialize(context);
        ActiveAndroid.beginTransaction();

        do {
            CallInfo info = new CallInfo(cursor);

            importCall(info);
            total += 1;
            lastId = info.id;
        }while(cursor.moveToNext());

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        cursor.close();
        prefs.edit().putString(ImportUtils.LAST_CALL_LOG, String.valueOf(lastId)).commit();
        return total;
    }

    public static int getLatestContact(Context context){
        SharedPreferences prefs = context.getSharedPreferences(ImportUtils.PREF_LAST_NAME, Context.MODE_PRIVATE);
        String lastCallID = prefs.getString(LAST_CONTACT, "-1"), starting = lastCallID;
        Cursor cursor = context.getContentResolver().query(Contacts.CONTENT_URI, null, ContactsContract.Contacts._ID + " > ?", new String[]{lastCallID}, Contacts._ID + " ASC");
        int total = 0;
        if(cursor.moveToFirst() == false) return total;
        long lastId = -1;

        ActiveAndroid.initialize(context);
        ActiveAndroid.beginTransaction();

        do {
            ContactInfo info = new ContactInfo(context, cursor);

            importContact(info);
            total += 1;
            lastId = info.id;
        }while(cursor.moveToNext());

        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        cursor.close();
        prefs.edit().putString(ImportUtils.LAST_CONTACT, String.valueOf(lastId)).commit();
        return total;
    }


}
