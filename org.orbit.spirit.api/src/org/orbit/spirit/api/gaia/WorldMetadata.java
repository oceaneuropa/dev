package org.orbit.spirit.api.gaia;

public interface WorldMetadata {

	String getGaiaId();

	void setGaiaId(String gaiaId);

	String getEarthId();

	void setEarthId(String earthId);

	String getGameId();

	void setGameId(String gameId);

	String[] getAccountIds();

	void setAccountIds(String[] accountId);

}
