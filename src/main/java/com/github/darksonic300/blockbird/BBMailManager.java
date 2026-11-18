package com.github.darksonic300.blockbird;

import com.github.darksonic300.blockbird.enums.IMAPSProvider;
import com.github.darksonic300.blockbird.util.BBToasts;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class BBMailManager {

	private static final int NEW_MSG_COUNT = 5;

	private IMAPSProvider provider;
	private String username;
	private String password;

	private static BBMailManager instance = null;
	private Store store = null;
	private Date lastCheck;

	private BBMailManager(IMAPSProvider provider, String username, String password) {
		this.provider = provider;
		this.username = username;
		this.password = password;

		this.lastCheck = new Date();
	}

	public static BBMailManager getInstance(IMAPSProvider provider, String username, String password) {
		if (instance == null) {
			instance = new BBMailManager(provider, username, password);
		}
		return instance;
	}

	public void setProvider(IMAPSProvider provider) {
		this.provider = provider;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isInitialized() {
		return store != null;
	}

	public void initialize() {
		Properties properties = new Properties();

		properties.put("mail.imaps.host", provider.host);
		properties.put("mail.imaps.port", provider.port);
		properties.put("mail.imaps.starttls.enable", "true");
		Session emailSession = Session.getDefaultInstance(properties);

		if (username.isEmpty() || password.isEmpty()) {
			return;
		}

		try {
			this.store = emailSession.getStore("imaps");
			this.store.connect(provider.host, username, password);
		} catch (Exception e) {
			this.store = null;
			BlockBird.LOGGER.warn("Failed to connect to the email server.");
			BBToasts.info(Component.translatable("gui.blockbird.toast.failed_to_connect"));
		}

		if (store == null)
			return;
		BlockBird.LOGGER.info("Connected to the email server.");
	}

	public List<Pair<String, String>> check() {
		if (store == null || lastCheck == null) {
			this.lastCheck = new Date();
			BlockBird.LOGGER.warn("Manager has not been initialized yet.");
			return List.of();
		}

		try {
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);

			List<Message> messages = getRecentMessages(emailFolder);
			var pairs = getMappedPairs(messages);

			emailFolder.close(false);
			this.lastCheck = new Date();
			return pairs;
		} catch (Exception e) {
			this.store = null;
			BlockBird.LOGGER.warn("Failed to read emails.");
			BBToasts.info(Component.translatable("gui.blockbird.toast.failed_to_read"));
		}
		return List.of();
	}

	public void close() {
		if (store == null)
			return;
		try {
			store.close();
		} catch (Exception e) {
			BlockBird.LOGGER.warn("The store was not closed properly???.");
		}
	}

	public Date getLastCheck() {
		return lastCheck;
	}


	private static @NotNull ArrayList<Pair<String, String>> getMappedPairs(List<Message> messages) throws MessagingException {
		var pairs = new ArrayList<Pair<String, String>>();
		if (!messages.isEmpty()) {
			for (var msg : messages) {
				pairs.add(Pair.of(((InternetAddress) msg.getFrom()[0]).getPersonal(), msg.getSubject()));
			}
		}
		return pairs;
	}

	private @NotNull List<Message> getRecentMessages(Folder emailFolder) throws MessagingException {
		List<Message> messages = new ArrayList<>();
		for (int i = 0; i < NEW_MSG_COUNT; i++) {
			var current = emailFolder.getMessage(emailFolder.getMessageCount() - i);
			if (!current.getReceivedDate().after(this.lastCheck) || current.isExpunged())
				continue;
			messages.add(current);
		}
		return messages;
	}
}