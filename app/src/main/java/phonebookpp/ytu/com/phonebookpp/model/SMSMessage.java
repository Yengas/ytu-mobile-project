package phonebookpp.ytu.com.phonebookpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "Message")
public class SMSMessage extends Model implements Serializable {
    @Column(name = "outgoing")
    public boolean outgoing;
    @Column(name = "body")
    public String body;
    @Column(name = "addressee", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public ContactNumber addressee;
    @Column(name = "date")
    public Date date;
}
