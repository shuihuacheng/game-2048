package com.leo.game.number.rank;

import java.io.Serializable;

public class RankingListItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7624655657281105900L;

	private String score;
	private String display_name;
	private String username;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
