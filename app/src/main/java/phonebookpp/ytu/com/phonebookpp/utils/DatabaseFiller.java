package phonebookpp.ytu.com.phonebookpp.utils;

import com.activeandroid.query.Select;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.model.Location;
import phonebookpp.ytu.com.phonebookpp.model.Call;
import phonebookpp.ytu.com.phonebookpp.model.SMSMessage;
import java.util.Date;

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

        Call yengasCall = new Call(), daktarCall = new Call();

        yengasCall.duration = 5;
        yengasCall.outgoing = true;
        yengasCall.date = new Date();
        yengasCall.addressee = yengasMobile;
        yengasCall.save();

        daktarCall.duration = 5;
        daktarCall.outgoing = false;
        daktarCall.date = new Date();
        daktarCall.addressee = daktarHome;
        daktarCall.save();

        SMSMessage yengasSMS = new SMSMessage(), daktarSMS = new SMSMessage();

        yengasSMS.body = "Hello";
        yengasSMS.outgoing = false;
        yengasSMS.date = new Date();
        yengasSMS.addressee = yengasMobile;
        yengasSMS.save();

        daktarSMS.body = "Hello";
        daktarSMS.outgoing = true;
        daktarSMS.date = new Date();
        daktarSMS.addressee = daktarHome;
        daktarSMS.save();

    }

    public static boolean filledBefore(){
        return new Select().from(Contact.class).execute().size() > 0;
    }
}
