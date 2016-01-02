package phonebookpp.ytu.com.phonebookpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Call")
public class Call extends Model implements Serializable {
    @Column(name = "outgoing")
    public boolean outgoing;
    @Column(name = "completed")
    public boolean completed;
    @Column(name="duration")
    public int duration;
    @Column(name = "addressee", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public ContactNumber addressee;
    @Column(name = "date")
    public Date date;
}
