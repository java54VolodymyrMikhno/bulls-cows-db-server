package telran.net.games.exceptions;

public class GameNotStartedException extends IllegalStateException {
	public GameNotStartedException(long gameId) {
		super("Game not started"+gameId);
	}
}
