package test.SocketTest;

import main.Classes.NormalClasses.Anagrafica.Child;
import main.Classes.NormalClasses.Anagrafica.Contact;
import main.Classes.NormalClasses.Anagrafica.Staff;
import main.Classes.NormalClasses.Anagrafica.Supplier;
import main.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class SocketModeTestAnagrafica {

    private static Client c;
    private static SessionMode socketMode;

    @BeforeClass
    public static void testSetUp() {

        SocketServer.main(null);

        try {
            c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        socketMode = new SocketMode(c);
    }

    @AfterClass
    public static void testSessionDisconnect() {
        socketMode.disconnect();
    }

    @Test
    public void correctLogin(){
        Staff newMember = new Staff("POLIMIPOLIMI1863", "Giuseppe", "Festa", "peppe", "peppe","1996-10-10", User.UserTypeFlag.AMMINISTRATIVO);
        socketMode.insertPersonIntoDb(newMember);

        assertNotNull(socketMode.login(newMember.getUsername(), newMember.getPassword()));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Staff", newMember.getCodiceFiscale());
    }

    @Test
    public void notExistingUserLogin(){

        socketMode.deleteSubjectFromDb("Staff", "POLIMIPOLIMI1863");

        assertNull(socketMode.login("POLIMIPOLIMI1863", "ABABAB"));

    }

    @Test
    public void uncorrectLogin(){

        Staff newMember = new Staff("POLIMIPOLIMI1863", "Giuseppe", "Festa", "peppe", "peppe","1996-10-10", User.UserTypeFlag.AMMINISTRATIVO);
        socketMode.insertPersonIntoDb(newMember);

        assertNull(socketMode.login(newMember.getUsername(), "000"));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Staff", newMember.getCodiceFiscale());

    }

    @Test
    public void insertNotExistingStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());

        //Adding child
        assertTrue(socketMode.insertPersonIntoDb(s));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void insertAlreadyExistingStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        socketMode.insertPersonIntoDb(s);

        //Adding child
        assertFalse(socketMode.insertPersonIntoDb(s));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void insertNotExistingContactIntoDb() {

        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");

        //Adding child
        assertTrue(socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA)));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
    }

    @Test
    public void insertAlreadyExistingContactIntoDb() {

        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding child
        assertFalse(socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA)));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
    }

    @Test
    public void insertNotExistingChildIntoDb() {

        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        assertTrue(socketMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");

    }

    @Test
    public void insertAlreadyExistingChildIntoDb() {

        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        socketMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000"));

        assertFalse(socketMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void insertChildWithUnexistingParentIntoDb() {

        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");

        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));

        assertFalse(socketMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void insertChildWithUnexistingPediatraIntoDb() {

        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");

        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));

        assertFalse(socketMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void extractChildrenFromDb() {

        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        socketMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000"));

        assertNotNull(socketMode.extractChildrenFromDb());

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");

    }

    @Test
    public void insertNotExistingSupplierIntoDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");

        assertTrue(socketMode.insertSupplierIntoDb(s));
    }

    @Test
    public void insertAlreadyExistingSupplierIntoDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        socketMode.insertSupplierIntoDb(s);//Double supplier are overwritten

        assertTrue(socketMode.insertSupplierIntoDb(s));
    }

    @Test
    public void updateContactIntoDb() {

        Contact c = new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA);
        socketMode.insertPersonIntoDb(c);

        c.setNome("Alberto");

        assertTrue(socketMode.updatePersonIntoDb(c));

        //House keeping operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
    }

    @Test
    public void updateNotExistingContactIntoDb() {

        Contact c = new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA);

        c.setNome("Alberto");

        assertFalse(socketMode.updatePersonIntoDb(c));

    }

    @Test
    public void updateStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        socketMode.insertPersonIntoDb(s);

        s.setNome("Elisa Gaia");

        assertTrue(socketMode.updatePersonIntoDb(s));

        //House keeping operations
        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void updateNotExistingStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());

        s.setNome("Elisa Gaia");

        assertFalse(socketMode.updatePersonIntoDb(s));

    }

    @Test
    public void updateSupplierIntoDb() {

        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        socketMode.insertSupplierIntoDb(s);

        s.setNomeF("Gaja vini Piemonte");

        assertTrue(socketMode.updateSupplierIntoDb(s));

        //House keeping operations
        socketMode.deleteSubjectFromDb("Supplier", "12345578901");
    }

    @Test
    public void updateNotExistingSupplierIntoDb() {

        socketMode.deleteSubjectFromDb("Supplier", "12345578901");
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");

        s.setNomeF("Gaja vini Piemonte");

        assertFalse(socketMode.updateSupplierIntoDb(s));

    }


    @Test
    public void updateChildIntoDb() {

        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");
        socketMode.insertPersonIntoDb(c);

        c.setNome("Carlo Alberto");
        assertTrue(socketMode.updatePersonIntoDb(c));

        //House keeping operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void updateNotExistingChildIntoDb() {

        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");

        c.setNome("Carlo Alberto");

        assertFalse(socketMode.updatePersonIntoDb(c));

    }

    @Test
    public void extractContactsFromDb() {

        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));

        assertNotNull(socketMode.extractContactsFromDb());

        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
    }

    @Test
    public void extractSuppliersFromDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");

        assertNotNull(socketMode.extractSuppliersFromDb());

        socketMode.deleteSubjectFromDb("Supplier", "12345578901");
    }

    @Test
    public void extractStaffFromDb() {
        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);

        assertNotNull(socketMode.extractStaffFromDb());

        socketMode.deleteSubjectFromDb("Staff", "ABACUSABACUSQWER");
    }

    @Test
    public void extractParentsForChildFromDb() {
        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child

        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");
        socketMode.insertPersonIntoDb(c);

        //assertNotNull(socketMode.extractParentsForChildFromDb(c.getCodiceFiscale()));

        //Clean up operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void deleteExistingContactFromDb() {
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));

        assertTrue(socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC"));
    }

    @Test
    public void deleteNotExistingContactFromDb() {
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");

        assertFalse(socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC"));
    }

    @Test
    public void deleteExistingChildFromDb() {
        //Adding a pediatra
        socketMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        socketMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        socketMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");
        socketMode.insertPersonIntoDb(c);

        assertTrue(socketMode.deleteSubjectFromDb("Child", c.getCodiceFiscale()));

        //House keeping operations
        socketMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        socketMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        socketMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
    }

    @Test
    public void deleteNotExistingChildFromDb() {
        socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");

        assertFalse(socketMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR"));
    }

    @Test
    public void deleteExistingSupplierFromDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        socketMode.insertSupplierIntoDb(s);

        assertTrue(socketMode.deleteSubjectFromDb("Supplier", s.getpIva()));
    }

    @Test
    public void deleteNotExistingSupplierFromDb() {
        socketMode.deleteSubjectFromDb("Supplier", "12345578901");

        assertFalse(socketMode.deleteSubjectFromDb("Supplier", "12345578901"));
    }

    @Test
    public void deleteExistingStaffFromDb() {
        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        socketMode.insertPersonIntoDb(s);

        assertTrue(socketMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale()));

    }

    @Test
    public void deleteNotExistingStaffFromDb() {

        socketMode.deleteSubjectFromDb("Staff", "ABACUSABACUSQWER");

        assertFalse(socketMode.deleteSubjectFromDb("Staff", "ABACUSABACUSQWER"));
    }

}