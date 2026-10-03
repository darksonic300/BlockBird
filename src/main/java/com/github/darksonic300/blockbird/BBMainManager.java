package com.github.darksonic300.blockbird;

import com.github.darksonic300.blockbird.enums.EmailProvider;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;

import java.util.Properties;

public class BBMainManager {

    private final EmailProvider provider;
    private final String username;
    private final String password;

    public BBMainManager(EmailProvider provider, String username, String password) {
        this.provider = provider;
        this.username = username;
        this.password = password;
    }

    public void check() {
        try {
            Properties properties = new Properties();

            properties.put("mail.smtp.host", provider.host);
            properties.put("mail.smtp.port", provider.port);
            properties.put("mail.smtp.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            Store store = emailSession.getStore("smtp");

            store.connect(provider.host, username, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }

            emailFolder.close(false);
            store.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
