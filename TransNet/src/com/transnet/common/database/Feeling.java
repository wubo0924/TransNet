package com.transnet.common.database;

public enum Feeling {
	Sad(1),Happy(2),Bored(3),Excited(4),Scared(5),Safe(6),Angry(7),Peaceful(8);
	
	private int FeelingID;

	Feeling(int FeelingID) {
	    this.FeelingID = FeelingID;
	}
	public int getKey() {
		return FeelingID;
	}
	
	
	

}
