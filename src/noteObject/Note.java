package noteObject;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int noteID;
	String content;
	Boolean isRemindedOndesk;
	Boolean isReminder;
	int tabID;
	Date timeToRemind;

	public Note(int noteID, String content, Boolean isRemindedOndesk,
			Boolean isReminder, int tabID, Date timeToRemind) {
		this.noteID = noteID;
		this.content = content;
		this.isRemindedOndesk = isRemindedOndesk;
		this.isReminder = isReminder;
		this.tabID = tabID;
		this.timeToRemind = timeToRemind;
	}

	public Note(String content, Boolean isRemindedOndesk, Boolean isReminder,
			int tabID, Date timeToRemind) {
		this(-1, content, isRemindedOndesk, isReminder, tabID, timeToRemind);
	}

	public Note(String content, int tabID) {
		this(content, false, false, tabID, null);
	}

	public Note(String content) {
		this(content, -1);
	}

	public int getNoteID() {
		return noteID;
	}

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsRemindedOndesk(Boolean isRemindedOndesk) {
		this.isRemindedOndesk = isRemindedOndesk;
	}

	public void setIsReminder(Boolean isReminder) {
		this.isReminder = isReminder;
	}

	public Date getTimeToRemind() {
		return timeToRemind;
	}

	public void setTimeToRemind(Date timeToRemind) {
		this.timeToRemind = timeToRemind;
	}

	public void setTabID(int tabID) {
		this.tabID = tabID;
	}

	public String getContent() {
		return content;
	}

	public Boolean getIsRemindedOndesk() {
		return isRemindedOndesk;
	}

	public Boolean getIsReminder() {
		return isReminder;
	}

	public int getTabID() {
		return tabID;
	}

}
