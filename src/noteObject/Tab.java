package noteObject;

public class Tab {

	private int tabID;
	private String tabName;
	private String tabKeyword;

	public Tab(int tabID, String tabName, String tabKeyword) {
		this.tabID = tabID;
		this.tabName = tabName;
		this.tabKeyword = tabKeyword;
	}

	public Tab(String tabName, String tabKeyword) {
		this(-1, tabName, tabKeyword);
	}

	public int getTabID() {
		return tabID;
	}

	public void setTabID(int tabID) {
		this.tabID = tabID;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public void setTabKeyword(String tabKeyword) {
		this.tabKeyword = tabKeyword;
	}

	public String getTabName() {
		return tabName;
	}

	public String getTabKeyword() {
		return tabKeyword;
	}
}
