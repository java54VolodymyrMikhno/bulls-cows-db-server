package telran.net.games.exceptions;
@SuppressWarnings("serial")
public class GameGamerNotFoundException extends IllegalArgumentException {
	public GameGamerNotFoundException(long gameId, String username) {
		super(String.format("Not found gamer %s in game %d", username, gameId));
	}
}