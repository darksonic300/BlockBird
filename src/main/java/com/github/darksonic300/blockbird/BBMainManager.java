package com.github.darksonic300.blockbird;

import com.github.darksonic300.blockbird.enums.IMAPSProvider;
import com.github.darksonic300.blockbird.screen.BBBaseToast;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class BBMainManager {

	private final IMAPSProvider provider;
	private final String username;
	private final String password;

	private Store store = null;
	private Date lastCheck;

	public BBMainManager(IMAPSProvider provider, String username, String password) {
		this.provider = provider;
		this.username = username;
		this.password = password;

		this.lastCheck = new Date();
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
			Minecraft.getInstance().getToasts()
					.addToast(new BBBaseToast(Component.translatable("gui.blockbird.toast.failed_to_connect")));
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
			var pairs = new ArrayList<Pair<String, String>>();
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			List<Message> messages = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				messages.add(emailFolder.getMessage(emailFolder.getMessageCount() - i));
			}

			messages = messages.stream().filter(msg -> {
				try {
					return msg.getReceivedDate().after(this.lastCheck);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}).toList();

			for (var msg : messages) {
				pairs.add(Pair.of(((InternetAddress) msg.getFrom()[0]).getPersonal(), msg.getSubject()));
			}
			emailFolder.close(false);
			this.lastCheck = new Date();
			return pairs;
		} catch (Exception e) {
			this.store = null;
			BlockBird.LOGGER.warn("Failed to read emails.");
			Minecraft.getInstance().getToasts()
					.addToast(new BBBaseToast(Component.translatable("gui.blockbird.toast.failed_to_read")));
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
}