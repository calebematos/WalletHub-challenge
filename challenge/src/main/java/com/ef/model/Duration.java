package com.ef.model;

public enum Duration {

		HOURLY("hourly"), DAILY("daily");

	private String desc;

	private Duration(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
