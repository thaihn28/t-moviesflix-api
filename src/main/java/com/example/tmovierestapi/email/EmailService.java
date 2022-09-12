package com.example.tmovierestapi.email;

import com.example.tmovierestapi.exception.APIException;
import com.example.tmovierestapi.utils.AppConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
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

    private JavaMailSender javaMailSender;

    @Override
    @Async
    public void send(String recipient, String name, String link) {
        try {
            String content = "Dear [[name]],<br>"
                    + "Please click the link below to verify your registration:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "<p>Link will expire in 15 minutes.<p><br>"
                    + "Thank you <3";
            content = content.replace("[[name]]", name);
            content = content.replace("[[URL]]", link);

            // Creating a mime message
            MimeMessage mimeMessage
                    = javaMailSender.createMimeMessage();


            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(AppConstants.USERNAME_MAIL_SENDER);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSubject(
                    "Confirm registration!");

            javaMailSender.send(mimeMessage);
            System.out.println("Mail sent succesfully!");
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to send email");
        }
    }

    @Override
    @Async
    public void notifyNewMovie(String recipient, String movieName, String link) {
        try {
            String content = "Dear all,<br>"
                    + "<strong>The best new movie: <em>[[movieName]]</em> was added:</strong><br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">WATCH THE NEW MOVIE IN HERE</a></h3>"
                    + "<p>Have a nice weekend<p><br>"
                    + "Thank you <3";
            content = content.replace("[[URL]]", link);
            content = content.replace("[[movieName]]", movieName);

            // Creating a mime message
            MimeMessage mimeMessage
                    = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(AppConstants.USERNAME_MAIL_SENDER);
            mimeMessageHelper.setTo(recipient);
            mimeMessageHelper.setText(content, true);
            mimeMessageHelper.setSubject(
                    "TMOVIE: NEW BEST MOVIES");

            javaMailSender.send(mimeMessage);
            System.out.println("Mail sent succesfully!");
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new APIException(HttpStatus.INTERNAL_SERVER_ERROR,"Failed to send email");
        }
    }

}
