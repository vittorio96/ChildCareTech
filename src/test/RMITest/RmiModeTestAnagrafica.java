package test.RMITest;

import main.*;

import main.Classes.NormalClasses.Anagrafica.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static org.junit.Assert.*;


public class RmiModeTestAnagrafica {

    private static Client c;
    private static SessionMode rmiMode;

    @BeforeClass
    public static void testSetUp() {

        RMIServerLauncher.main(null);

        try {
            c = new Client();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        rmiMode = new RmiMode(c);
    }

    @AfterClass
    public static void testSessionDisconnect() {
        rmiMode.disconnect();
    }


    @Test
    public void correctLogin(){
        Staff newMember = new Staff("POLIMIPOLIMI1863", "Giuseppe", "Festa", "peppe", "peppe","1996-10-10", User.UserTypeFlag.AMMINISTRATIVO);
        rmiMode.insertPersonIntoDb(newMember);

        assertNotNull(rmiMode.login(newMember.getUsername(), newMember.getPassword()));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Staff", newMember.getCodiceFiscale());
    }

    @Test
    public void notExistingUserLogin(){

        rmiMode.deleteSubjectFromDb("Staff", "POLIMIPOLIMI1863");

        assertNull(rmiMode.login("POLIMIPOLIMI1863", "ABABAB"));

    }

    @Test
    public void uncorrectLogin(){

        Staff newMember = new Staff("POLIMIPOLIMI1863", "Giuseppe", "Festa", "peppe", "peppe","1996-10-10", User.UserTypeFlag.AMMINISTRATIVO);
        rmiMode.insertPersonIntoDb(newMember);

        assertNull(rmiMode.login(newMember.getUsername(), "000"));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Staff", newMember.getCodiceFiscale());

    }

    @Test
    public void insertNotExistingStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());

        //Adding child
        assertTrue(rmiMode.insertPersonIntoDb(s));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void insertAlreadyExistingStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        rmiMode.insertPersonIntoDb(s);

        //Adding child
        assertFalse(rmiMode.insertPersonIntoDb(s));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void insertNotExistingContactIntoDb() {

        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");

        //Adding child
        assertTrue(rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA)));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
    }

    @Test
    public void insertAlreadyExistingContactIntoDb() {

        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding child
        assertFalse(rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA)));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
    }

    @Test
    public void insertNotExistingChildIntoDb() {

        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        assertTrue(rmiMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");

    }

    @Test
    public void insertAlreadyExistingChildIntoDb() {

        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        rmiMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000"));

        assertFalse(rmiMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void insertChildWithNotExistingParentIntoDb() {

        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");

        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));

        assertFalse(rmiMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void insertChildWithNotExistingPediatraIntoDb() {

        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");

        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));

        assertFalse(rmiMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000")));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void extractChildrenFromDb() {

        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        rmiMode.insertPersonIntoDb(new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000"));

        assertNotNull(rmiMode.extractChildrenFromDb());

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");

    }

    @Test
    public void insertNotExistingSupplierIntoDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");

        assertTrue(rmiMode.insertSupplierIntoDb(s));

        rmiMode.deleteSubjectFromDb("Contact", s.getpIva());
    }

    @Test
    public void insertAlreadyExistingSupplierIntoDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        rmiMode.insertSupplierIntoDb(s);//Double supplier are overwritten

        assertTrue(rmiMode.insertSupplierIntoDb(s));

        rmiMode.deleteSubjectFromDb("Staff", s.getpIva());
    }

    @Test
    public void updateContactIntoDb() {

        Contact c = new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA);
        rmiMode.insertPersonIntoDb(c);

        c.setNome("Alberto");

        assertTrue(rmiMode.updatePersonIntoDb(c));

        //House keeping operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
    }

    @Test
    public void updateNotExistingContactIntoDb() {

        Contact c = new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA);

        c.setNome("Alberto");

        assertFalse(rmiMode.updatePersonIntoDb(c));

    }

    @Test
    public void updateStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        rmiMode.insertPersonIntoDb(s);

        s.setNome("Elisa Gaia");

        assertTrue(rmiMode.updatePersonIntoDb(s));

        //House keeping operations
        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());
    }

    @Test
    public void updateNotExistingStaffIntoDb() {

        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale());

        s.setNome("Elisa Gaia");

        assertFalse(rmiMode.updatePersonIntoDb(s));

    }

    @Test
    public void updateSupplierIntoDb() {

        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        rmiMode.insertSupplierIntoDb(s);

        s.setNomeF("Gaja vini Piemonte");

        assertTrue(rmiMode.updateSupplierIntoDb(s));

        //House keeping operations
        rmiMode.deleteSubjectFromDb("Supplier", "12345578901");
    }

    @Test
    public void updateNotExistingSupplierIntoDb() {

        rmiMode.deleteSubjectFromDb("Supplier", "12345578901");
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");

        s.setNomeF("Gaja vini Piemonte");

        assertFalse(rmiMode.updateSupplierIntoDb(s));

    }

    @Test
    public void updateChildIntoDb() {

        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");
        rmiMode.insertPersonIntoDb(c);

        c.setNome("Carlo Alberto");
        assertTrue(rmiMode.updatePersonIntoDb(c));

        //House keeping operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void updateNotExistingChildIntoDb() {

        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");

        c.setNome("Carlo Alberto");

        assertFalse(rmiMode.updatePersonIntoDb(c));

    }

    @Test
    public void extractContactsFromDb() {

        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));

        assertNotNull(rmiMode.extractContactsFromDb());

        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
    }

    @Test
    public void extractSuppliersFromDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        rmiMode.insertSupplierIntoDb(s);
        assertNotNull(rmiMode.extractSuppliersFromDb());

        rmiMode.deleteSubjectFromDb("Supplier", "12345578901");
    }

    @Test
    public void extractStaffFromDb() {
        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);

        assertNotNull(rmiMode.extractStaffFromDb());

        rmiMode.deleteSubjectFromDb("Staff", "ABACUSABACUSQWER");
    }

    @Test
    public void extractParentsForChildFromDb() {
        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child

        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");
        rmiMode.insertPersonIntoDb(c);

        assertNotNull(rmiMode.extractParentsForChildFromDb(c.getCodiceFiscale()));

        //Clean up operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");
    }

    @Test
    public void deleteExistingContactFromDb() {
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));

        assertTrue(rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC"));
    }

    @Test
    public void deleteNotExistingContactFromDb() {
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");

        assertFalse(rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC"));
    }

    @Test
    public void deleteExistingChildFromDb() {
        //Adding a pediatra
        rmiMode.insertPersonIntoDb(new Contact("Attilio", "Maffini", "ATTILIOMAFFINIJK", "123456780", Contact.ContactTypeFlag.PEDIATRA));
        //Adding parents
        rmiMode.insertPersonIntoDb(new Contact("Gloria", "Grazioli", "GLORIA1234PIGREC", "023456780", Contact.ContactTypeFlag.GENITORE));
        rmiMode.insertPersonIntoDb(new Contact("Alberto", "Ferrari", "FERALB0000000000", "123458788", Contact.ContactTypeFlag.GENITORE));
        //Adding child
        Child c = new Child("Carlo", "Rossi", "CRCRCRCRCRCRCRCR", "1996-05-05", "ATTILIOMAFFINIJK","GLORIA1234PIGREC", "FERALB0000000000");
        rmiMode.insertPersonIntoDb(c);

        assertTrue(rmiMode.deleteSubjectFromDb("Child", c.getCodiceFiscale()));

        //House keeping operations
        rmiMode.deleteSubjectFromDb("Contact", "ATTILIOMAFFINIJK");
        rmiMode.deleteSubjectFromDb("Contact", "GLORIA1234PIGREC");
        rmiMode.deleteSubjectFromDb("Contact", "FERALB0000000000");
    }

    @Test
    public void deleteNotExistingChildFromDb() {
        rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR");

        assertFalse(rmiMode.deleteSubjectFromDb("Child", "CRCRCRCRCRCRCRCR"));
    }

    @Test
    public void deleteExistingSupplierFromDb() {
        Supplier s = new Supplier("12345578901", "Gaja vini", "034456847", "sales@gaja.com");
        rmiMode.insertSupplierIntoDb(s);

        assertTrue(rmiMode.deleteSubjectFromDb("Supplier", s.getpIva()));
    }

    @Test
    public void deleteNotExistingSupplierFromDb() {
        rmiMode.deleteSubjectFromDb("Supplier", "12345578901");

        assertFalse(rmiMode.deleteSubjectFromDb("Supplier", "12345578901"));
    }

    @Test
    public void deleteExistingStaffFromDb() {
        Staff s = new Staff("ABACUSABACUSQWER", "Elisa", "Zorzella", "elisa", "elisa", "1996-10-08", User.UserTypeFlag.MENSA);
        rmiMode.insertPersonIntoDb(s);

        assertTrue(rmiMode.deleteSubjectFromDb("Staff", s.getCodiceFiscale()));

    }

    @Test
    public void deleteNotExistingStaffFromDb() {

        rmiMode.deleteSubjectFromDb("Staff", "ABACUSABACUSQWER");

        assertFalse(rmiMode.deleteSubjectFromDb("Staff", "ABACUSABACUSQWER"));
    }

}