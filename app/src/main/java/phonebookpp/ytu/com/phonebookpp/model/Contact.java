package phonebookpp.ytu.com.phonebookpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Contact")
public class Contact extends Model implements Serializable {
    @Column(name = "name")
    public String name;
    @Column(name = "surname")
    public String surname;
    @Column(name = "email")
    public String email;

    public int incomingCallDuration, outgoingCallDuration, missingCalls, sentMessages, recievedMessages;

    public List<Location> getLocations(){
        return getMany(Location.class, "holder");
    }

    public List<ContactNumber> getNumbers(){
        return getMany(ContactNumber.class, "holder");
    }

    public List<Call> getCalls(){
        List<Call> calls = new ArrayList<Call>();

        for(ContactNumber number : getNumbers()){
            calls.addAll(number.getCalls());
        }

        return calls;
    }

    public List<SMSMessage> getMessages(){
        List<SMSMessage> messages = new ArrayList<SMSMessage>();

        for(ContactNumber number : getNumbers()){
            messages.addAll(number.getMessages());
        }

        return messages;
    }

    public void load(){
        incomingCallDuration = outgoingCallDuration = missingCalls = sentMessages = recievedMessages = 0;
        List<Call> calls = this.getCalls();
        List<SMSMessage> messages = this.getMessages();

        // 2 Mysql query if we were to use sql
        for(Call call : calls){
            if(call.outgoing)
                outgoingCallDuration += call.duration;
            else
                incomingCallDuration += call.duration;
            if(call.completed == false) missingCalls += 1;
        }

        // 1 Mysql query if we were to use sql
        for(SMSMessage message : messages){
            if(message.outgoing)
                sentMessages += 1;
            else
                recievedMessages += 1;
        }
    }
}
