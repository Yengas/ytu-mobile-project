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
import phonebookpp.ytu.com.phonebookpp.utils.ImportUtils;
import phonebookpp.ytu.com.phonebookpp.utils.PPCommon;

public class PhoneStateReceiver extends BroadcastReceiver {
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
                    int count = ImportUtils.getLatestCall(context);

                    if(count > 0)
                        Toast.makeText(context, "Added some shit... Total: " + count, Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "Couldn't find bitch...", Toast.LENGTH_LONG).show();

                    context.getContentResolver().unregisterContentObserver(this);
                }
            });
        }
    }
}
