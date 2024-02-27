package org.harry.email.service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.harry.email.config.EmailConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import static org.harry.email.utils.EmailUtils.getEmailMessage;
import static org.harry.email.utils.EmailUtils.getVerificationUrl;


@Component
@AllArgsConstructor
public class EmailServiceImpl implements EmailService{
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "New user account verification";
    public static final String EMAIL_TEMPLATE = "emailTemplate";
    public static final String TEXT_HTML_ENCODING = "text/html";
    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    public static final String UTF_8_ENCODING = "UTF-8";

    private EmailConfig emailConfig;


    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(emailConfig.getFromEmail());
            message.setTo(to);
            message.setText(getEmailMessage(name, emailConfig.getHost(), token));
            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachment(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(to);
            helper.setText(getEmailMessage(name, emailConfig.getHost(), token));
            //Add attachment
            FileSystemResource initial = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/Harry Nwa Initials.jpeg"));
            FileSystemResource logo = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/Tranxact on.png"));
            helper.addAttachment(initial.getFilename(), initial);
            helper.addAttachment(logo.getFilename(), logo);
            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(to);
            helper.setText(getEmailMessage(name, emailConfig.getHost(), token));
            //Add attachment
            FileSystemResource initial = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/Harry Nwa Initials.jpeg"));
            FileSystemResource logo = new FileSystemResource(new File(System.getProperty("user.home") + "/Downloads/Tranxact on.png"));
            helper.addInline(getContentId(initial.getFilename()), initial);
            helper.addInline(getContentId(logo.getFilename()), logo);
            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
//            context.setVariable("name", name);
//            context.setVariable("url", getVerificationUrl(host, token));
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(emailConfig.getHost(), token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
            helper.setPriority(1);
            helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            helper.setFrom(emailConfig.getFromEmail());
            helper.setTo(to);
//            helper.setText(text, true);
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(emailConfig.getHost(), token)));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);

            //Add HTML email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
            mimeMultipart.addBodyPart(messageBodyPart);

            //Add images to the email body
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(System.getProperty("user.home") + "/Downloads/Tranxact only.png");
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(messageBodyPart);

            message.setContent(mimeMultipart);

            emailSender.send(message);
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }
}


