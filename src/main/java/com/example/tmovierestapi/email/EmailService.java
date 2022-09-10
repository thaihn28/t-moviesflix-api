package com.example.tmovierestapi.email;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{
    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    @Override
    @Async
    public void send(String recipient, String name, String link) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(AppConstants.USERNAME_MAIL_SENDER, AppConstants.PASSWORD_MAIL_SENDER);
                        }
                    });
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "<p>Link will expire in 15 minutes.<p><br>"
                    + "Thank you <3";
            content = content.replace("[[name]]", name);
            content = content.replace("[[URL]]", link);

            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message);

            mimeMessageHelper.setFrom(AppConstants.USERNAME_MAIL_SENDER);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSubject(
                    "Confirm registration!");

            Transport.send(message);
            System.out.println("Mail sent succesfully!");
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to send email");
        }
    }

//
//    @Override
//    @Async
//    public void send(String[] to, String email) {
//        try {
//            Properties props = new Properties();
//            props.put("mail.smtp.host" , "smtp.gmail.com");
//            props.put("mail.stmp.user" , "thaihn28012@gmail.com");
//
//            //To use TLS
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.starttls.enable", "true");
//            props.put("mail.smtp.password", "..,Hnt282001,..");
//            //To use SSL
//            props.put("mail.smtp.socketFactory.port", "465");
//            props.put("mail.smtp.socketFactory.class",
//                    "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.port", "465");
//
//            javax.mail.Message message = new MimeMessage(Session
//                    .getDefaultInstance(props, new javax.mail.Authenticator(){
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication(
//                                    "XXXX@gmail.com", "XXXXX");// Specify the Username and the PassWord
//                        }
//                    }));
//
////            MimeMessage mimeMessage = mailSender.createMimeMessage();
////            MimeMessageHelper helper =
////                    new MimeMessageHelper(mimeMessage, "utf-8");
////            mimeMessage.setText(email);
////            mimeMessage.setTo(to);
////            helper.setSubject("Complete Registration!");
////            helper.setFrom("thaihn2801@gmail.com");
////            mailSender.send(mimeMessage);
////            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
//            InternetAddress[] addressTo = new InternetAddress[to.length];
//            for (int i = 0; i < to.length; i++) {
//                addressTo[i] = new InternetAddress(to[i]);
//            }
//            message.setRecipients(Message.RecipientType.TO, addressTo);
//            message.setSubject(subject);
//            message.setText(text);
//            Transport.send(message);
//        } catch (MessagingException e) {
//            LOGGER.error("failed to send email", e);
//            throw new IllegalStateException("failed to send email");
//        }
//    }
}
