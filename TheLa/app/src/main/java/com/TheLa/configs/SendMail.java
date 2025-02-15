package com.TheLa.configs;

import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;

public class SendMail {
    private static String email = "craftcornerfurniture@gmail.com";
    private static String password = "mhzh cegr tcgb onyh";

    public static boolean sendEmail(String toEmail, String subject, String messageBody) {
        boolean isValid = false;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            isValid = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static String getRandom() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }


}