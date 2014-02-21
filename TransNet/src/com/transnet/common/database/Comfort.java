package com.transnet.common.database;

public enum Comfort {
	Hot(1),Cold(2),Noisy(3),Quite(4),Full(5),Empty(6),Comfort(7);
	
	private int ComfortID;

	Comfort(int ComfortID) {
	    this.ComfortID = ComfortID;
	}
	public int getKey() {
		return ComfortID;
	}

}
