package phonebookpp.ytu.com.phonebookpp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "Location")
public class Location extends Model implements Serializable {
    @Column(name = "type")
    public ContactInfoType type;
    @Column(name ="longtitude")
    public double longtitude;
    @Column(name = "latitude")
    public double latitude;
    @Column(name = "holder", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    public Contact holder;
}
