package me.mrmango404.hooks;

public enum BSkyblockFlag {
	CONTAINER_VIEW_PROTECTION("CONTAINER_VIEW_PROTECTION");

	final String flagName;

	BSkyblockFlag(String flagName) {
		this.flagName = flagName;
	}

	public String getFlagName() {
		return flagName;
	}
}
