package phonebookpp.ytu.com.phonebookpp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Date;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import phonebookpp.ytu.com.phonebookpp.model.Call;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.utils.PPCommon;

public class PhoneStateReceiver extends BroadcastReceiver {
    static class CallInfo{
        public final String number;
        public final int type, duration;

        public CallInfo(String number, int duration, int type){
            this.number = number;
            this.duration = duration;
            this.type = type;
        }
    }
    @Override
    public void onReceive(final Context context, Intent intent){
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals("OFFHOOK")){
            context.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, new ContentObserver(new Handler()) {
                @Override
                public boolean deliverSelfNotifications() {
                    return true;
                }

                @Override
                public void onChange(boolean selfChange) {
                    super.onChange(selfChange);
                    CallInfo info = getLatestCall(context);

                    ActiveAndroid.initialize(context);
                    Object addressee = new Select().from(ContactNumber.class).where("number = ?", PPCommon.sanitizeNumber(info.number)).executeSingle();

                    if(addressee != null) {
                        Call call = new Call();

                        call.duration = info.duration;
                        call.completed = info.type != CallLog.Calls.MISSED_TYPE && info.duration > 0;
                        call.outgoing = info.type == CallLog.Calls.OUTGOING_TYPE;
                        call.date = new Date();
                        call.addressee = (ContactNumber) addressee;
                        call.save();
                        Toast.makeText(context, String.format("%s for %s type was: %d", info.number, info.duration, info.type), Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context, "Couldn't find bitch...", Toast.LENGTH_LONG).show();
                    }

                    context.getContentResolver().unregisterContentObserver(this);
                }
            });
        }
    }

    public static CallInfo getLatestCall(Context context){
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC LIMIT 1");
        if(cursor.moveToFirst() == false) return null;
        CallInfo info = new CallInfo(
                cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))
        );

        cursor.close();
        return info;
    }
}
