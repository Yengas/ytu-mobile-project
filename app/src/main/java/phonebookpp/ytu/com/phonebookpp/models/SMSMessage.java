package phonebookpp.ytu.com.phonebookpp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Message")
public class SMSMessage extends Model {
    @Column(name = "outgoing")
    public boolean outgoing;
    @Column(name = "body")
    public String body;
    @Column(name = "target", index = true)
    public Number target;
    @Column(name = "date")
    public Date date;
}
