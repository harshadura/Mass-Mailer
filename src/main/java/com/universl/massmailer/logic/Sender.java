package com.universl.massmailer.logic;

import org.apache.log4j.Logger;
import com.universl.massmailer.db.DBConnect;
import com.universl.massmailer.db.PropertyLoader;
import java.util.Date;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Sender {

    private static Logger logger = Logger.getLogger(Sender.class);
    private int bulkCount = 0;

    public String[] gatherData(String to, String start, String end) {

        int s = Integer.parseInt(start);
        int e = Integer.parseInt(end);
        String EmailAddressList[] = new String[e - s + 1];

        try {
            DBConnect dbConnect = new DBConnect();
            Connection con = dbConnect.connect();
            Statement stm = con.createStatement();
            //select email from Group1 where tuple>=1 AND tuple<=2
            String sql3 = "SELECT email from " + to + " where tuple>=" + start + " AND tuple<=" + end;
            ResultSet rst2 = stm.executeQuery(sql3);
            logger.info("Ready to Send Emails..");

            int i = 0;
            while (rst2.next()) {
                EmailAddressList[i] = rst2.getString(1);
                logger.info("Email Address : " + EmailAddressList[i]);
                i++;
            }
            rst2.close();
            System.out.println("********test here" + EmailAddressList[0]);
            return EmailAddressList;
        } catch (Exception ee) {
            logger.error("Exception Occurred while sending the Message : " + ee);
            ee.printStackTrace();
            return null;
        }
    }

    public int gatherEmailData(String to) {
        try {
            DBConnect dbConnect = new DBConnect();
            Connection con = dbConnect.connect();
            Statement stm = con.createStatement();
            ResultSet results = stm.executeQuery("select count(*) cc from " + to);
            while (results.next()) {
                bulkCount = Integer.parseInt(results.getString("cc"));
            }
            return bulkCount;
        } catch (Exception ee) {
            logger.error("Exception Occurred while sending the Message : " + ee);
            ee.printStackTrace();
            return 0;
        }
    }

    public boolean Send(String to, String subject, String message, String path, String start, String end) throws Throwable {

        System.out.println("********" + start + end + path + message + subject + to);
        String EmailAddressList[] = gatherData(to, start, end);
        String smtpUser = PropertyLoader.smtpUser;
        String smtpPass = PropertyLoader.smtpPass;
        String smtpHost = PropertyLoader.smtpHost;
        String smtpPort = PropertyLoader.smtpPort;
        java.util.Properties props = System.getProperties();
        props.put("mail.smtp.user", smtpUser);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new com.universl.massmailer.logic.Auth(smtpUser, smtpPass));
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(smtpUser));
            System.out.println(EmailAddressList.length + " | " + EmailAddressList[0] + EmailAddressList[1]);
            javax.mail.internet.InternetAddress[] addressTo = new javax.mail.internet.InternetAddress[EmailAddressList.length];
            for (int i = 0; i < EmailAddressList.length; i++) {
                addressTo[i] = new javax.mail.internet.InternetAddress(EmailAddressList[i]);
            }
            msg.setRecipients(javax.mail.Message.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            message = message + "\n\n\n\n\n________________________________\n\nUniverSL Software";
            msg.setText(message);
            if (!path.equals("0")) {
                msg.setContent(addAttachment(path, message));
            }
            Transport.send(msg);
            logger.info("Emails Sent Successfully");
            return true;
        } catch (Exception ee) {
            ee.printStackTrace();
            logger.error("Exception Occurred while sending the Message : " + ee);
            return false;
        }
    }

    private Multipart addAttachment(String filename, String message) {
        try {
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(message);
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            return multipart;
        } catch (Exception ee) {
            System.out.println("******" + ee.getMessage());
            logger.error("Exception Occurred while sending the Message : " + ee);
            ee.printStackTrace();
            return null;
        }
    }
}
