package controllers;

import java.util.Properties;

import javax.mail.Session;

import javax.mail.internet.MimeMessage;
import javax.mail.Transport;

public class SimpleEmail {

    public static void main(String[] args) {

        System.out.println("SimpleEmail Start");

        String smtpHostServer = "smtp.journaldev.com";
        String emailID = "pankaj@journaldev.com";

        Properties props = System.getProperties();

        props.put("mail.smtp.host", smtpHostServer);

        Session session = Session.getInstance(props, null);

        EmailUtil.sendEmail(session, emailID,"SimpleEmail Testing Subject", "SimpleEmail Testing Body");
    }

}
