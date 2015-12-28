package phonebookpp.ytu.com.phonebookpp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by DARK on 12/28/2015.
 */
@Table(name = "Location")
public class Location extends Model {
    @Column(name = "type")
    public ContactInfoType type;
    @Column(name ="longtitude")
    public double longtitude;
    @Column(name = "latitude")
    public double latitude;
    @Column(name = "holder", index = true)
    public Contact holder;
}
