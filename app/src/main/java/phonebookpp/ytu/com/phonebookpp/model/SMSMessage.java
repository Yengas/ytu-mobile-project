package phonebookpp.ytu.com.phonebookpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Message")
public class SMSMessage extends Model implements Serializable {
    @Column(name = "outgoing")
    public boolean outgoing;
    @Column(name = "body")
    public String body;
    @Column(name = "addressee")
    public ContactNumber addressee;
    @Column(name = "date")
    public Date date;
}