package com.example.brenno.pse1617_consegna3;


public class C {
    public static final String LOG_TAG = "PSE1617_CONSEGNA3";

    static final String TARGET_BT_DEVICE_NAME = "HC-06";
    static final String TARGET_BT_DEVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    static final int ENABLE_BT_REQUEST = 1;

    static final String PRESENCE_MESSAGE = "Presenza veicolo - distanza: ";
    static final String CONTACT_MESSAGE = "contatto";

    public static final String END_SEEK_COMUNICATION = "fine";

    static final String FROM_USERNAME = "pse.student.unibo@gmail.com";
    static final String FROM_PASSWORD = "pse.student";
    static final String MAIL_SUBJECT = "Rilavato contatto";
    static final String BODY_MAIL = "E' stato rilevato un contatto.";

    public static final String ACCESA_MOV = "Accesa in movimento";
    public static final String SPENTA_NON_PARC = "Spenta non in parcheggio";
    public static final String SPENTA_PARC = "Spenta in parcheggio";

    static final String  MAIL_SENT = "E-mail sent";
    public static final String EMAIL_ERROR = "E-mail not valid";
}
