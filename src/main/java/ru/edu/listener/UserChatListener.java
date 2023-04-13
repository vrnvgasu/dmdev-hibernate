package ru.edu.listener;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import ru.edu.entity.UserChat;

public class UserChatListener {

	@PostPersist
	public void postPersist(UserChat userChat) {
		var chat = userChat.getChat();
		chat.setCount(chat.getCount() + 1);
	}

	@PostRemove
	public void postRemove(UserChat userChat) {
		var chat = userChat.getChat();
		chat.setCount(chat.getCount() - 1);
	}

}
