package phonebookpp.ytu.com.phonebookpp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Contact")
public class Contact extends Model {
    @Column(name = "name")
    public String name;
    @Column(name = "surname")
    public String surname;
    @Column(name = "email")
    public String email;

    public List<Location> getLocations(){
        return getMany(Location.class, "holder");
    }

    public List<ContactNumber> getNumbers(){
        return getMany(ContactNumber.class, "holder");
    }

    public List<Call> getCalls(){
        return getMany(Call.class, "target");
    }

    public List<SMSMessage> getMessages(){
        return getMany(SMSMessage.class, "target");
    }
}
