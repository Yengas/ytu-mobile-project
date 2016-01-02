package phonebookpp.ytu.com.phonebookpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "ContactNumber")
public class ContactNumber extends Model implements Serializable {
    @Column(name = "type")
    public ContactInfoType type;
    @Column(name = "number")
    public String number;
    @Column(name = "holder")
    public Contact holder;

    public List<Call> getCalls(){
        return getMany(Call.class, "addressee");
    }

    public List<SMSMessage> getMessages(){
        return getMany(SMSMessage.class, "addressee");
    }
}