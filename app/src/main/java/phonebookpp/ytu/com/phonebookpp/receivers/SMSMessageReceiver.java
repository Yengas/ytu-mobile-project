package phonebookpp.ytu.com.phonebookpp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.widget.Toast;
import android.telephony.SmsMessage;

import java.util.Date;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.model.SMSMessage;
import phonebookpp.ytu.com.phonebookpp.utils.PPCommon;

/**
 * Created by DARK on 1/2/2016.
 */
public class SMSMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        if(messages == null || messages.length <= 0) return;
        ActiveAndroid.initialize(context);

        for(SmsMessage message : messages){
            if(message == null) continue;
            Object contactNumber = new Select().from(ContactNumber.class).where("number = ?", PPCommon.sanitizeNumber(message.getDisplayOriginatingAddress())).executeSingle();

            if(contactNumber != null){
                SMSMessage ppMessage = new SMSMessage();

                ppMessage.outgoing = false;
                ppMessage.date = new Date();
                ppMessage.body = message.getMessageBody();
                ppMessage.addressee = (ContactNumber) contactNumber;
                ppMessage.save();
            }
        }
    }
}
