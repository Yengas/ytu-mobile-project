package phonebookpp.ytu.com.phonebookpp.utils;

import com.activeandroid.query.Select;

import phonebookpp.ytu.com.phonebookpp.models.Contact;
import phonebookpp.ytu.com.phonebookpp.models.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.models.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.models.Location;

/**
 * Created by DARK on 12/28/2015.
 */
public class DatabaseFiller {
    public static void fill(){
        Contact yengas = new Contact(), daktar = new Contact();

        yengas.name = "Yiğitcan";
        yengas.surname = "UÇUM";
        yengas.email = "yigitcan@hotmail.com.tr";
        yengas.save();

        daktar.name = "Tolga";
        daktar.surname = "AY";
        daktar.email = "daktar@gmail.com";
        daktar.save();

        Location yengasHome = new Location(), daktarWork = new Location();

        yengasHome.type = ContactInfoType.HOME;
        yengasHome.latitude = 73.0;
        yengasHome.longtitude = 0.73;
        yengasHome.holder = yengas;
        yengasHome.save();

        daktarWork.type = ContactInfoType.WORK;
        daktarWork.latitude = 42.0;
        daktarWork.longtitude = 0.43;
        daktarWork.holder = daktar;
        daktarWork.save();

        ContactNumber yengasMobile = new ContactNumber(), daktarHome = new ContactNumber();

        yengasMobile.type = ContactInfoType.MOBILE;
        yengasMobile.number = "+907377377373";
        yengasMobile.holder = yengas;
        yengasMobile.save();

        daktarHome.type = ContactInfoType.HOME;
        daktarHome.number = "+904244244242";
        daktarHome.holder = daktar;
        daktarHome.save();
    }

    public static boolean filledBefore(){
        return new Select().from(Contact.class).execute().size() > 0;
    }
}
