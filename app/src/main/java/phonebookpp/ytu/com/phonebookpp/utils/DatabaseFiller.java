package phonebookpp.ytu.com.phonebookpp.utils;

import com.activeandroid.query.Select;

import phonebookpp.ytu.com.phonebookpp.model.Contact;
import phonebookpp.ytu.com.phonebookpp.model.ContactInfoType;
import phonebookpp.ytu.com.phonebookpp.model.ContactNumber;
import phonebookpp.ytu.com.phonebookpp.model.Location;
import phonebookpp.ytu.com.phonebookpp.model.Call;
import phonebookpp.ytu.com.phonebookpp.model.SMSMessage;
import java.util.Date;

public class DatabaseFiller {
    public static void fill(){
        Contact testContact = new Contact();
        
        testContact.name = "Tolga";
        testContact.surname = "Ay";
        testContact.email = "contact@gmail.com";
        testContact.save();

        Location contactWork = new Location();

        contactWork.type = ContactInfoType.WORK;
        contactWork.latitude = 42.0;
        contactWork.longtitude = 0.43;
        contactWork.holder = testContact;
        contactWork.save();

        ContactNumber contactHome = new ContactNumber();
        contactHome.type = ContactInfoType.HOME;
        contactHome.number = "+905362223344";
        contactHome.holder = testContact;
        contactHome.save();

        Call contactCall = new Call();

        contactCall.duration = 5;
        contactCall.outgoing = false;
        contactCall.date = new Date();
        contactCall.addressee = contactHome;
        contactCall.save();

        SMSMessage contactSMS = new SMSMessage();
        contactSMS.body = "Hello";
        contactSMS.outgoing = true;
        contactSMS.date = new Date();
        contactSMS.addressee = contactHome;
        contactSMS.save();
    }

    public static boolean filledBefore(){
        return new Select().from(Contact.class).execute().size() > 0;
    }
}
