package com.phonebook.model;

public class PhonebookSorter {
    private static PhonebookSorter instance = null;
    private static IPhonebookSorter phonebookSorter;

    private PhonebookSorter(IPhonebookSorter phonebookSorter) {
        PhonebookSorter.phonebookSorter = phonebookSorter;
    }

    public static PhonebookSorter getInstance() {
        if(instance == null) {
            IPhonebookSorter phonebookSorter = new LastNamePhonebookSorter();
            instance = new PhonebookSorter(phonebookSorter);
        }
        return instance;
    }

    public static void setPhonebookSorter(IPhonebookSorter phonebookSorter) {
        PhonebookSorter.phonebookSorter = phonebookSorter;
    }

    public IPhonebookSorter getPhonebookSorter() {
        return phonebookSorter;
    }
}
