package phonebookpp.ytu.com.phonebookpp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Call")
public class Call extends Model {
    @Column(name = "outgoing")
    public boolean outgoing;
    @Column(name = "completed")
    public boolean completed;
    @Column(name="duration")
    public int duration;
    @Column(name = "target", index = true)
    public Number target;
    @Column(name = "date")
    public Date date;
}
