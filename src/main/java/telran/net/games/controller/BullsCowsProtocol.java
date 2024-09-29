package telran.net.games.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

import telran.net.*;
import telran.net.games.model.*;
import telran.net.games.service.BullsCowsService;
public class BullsCowsProtocol implements Protocol {
	private BullsCowsService bcService;
	
	public BullsCowsProtocol(BullsCowsService bcService) {
		this.bcService = bcService;
	}

	@Override
	public Response getResponse(Request request) {
		String requestType = request.requestType();
		String requestData = request.requestData();
		Response response;
		try {
			response = switch (requestType) {
			case "createGame" -> createGame(requestData);
			case "startGame" -> startGame(requestData);
			case "registerGamer" -> registerGamer(requestData);
			case "gamerJoinGame" -> gamerJoinGame(requestData);
			case "getNotStartedGames" -> getNotStartedGames(requestData);
			case "getNotStartedGamesWithGamer" -> getNotStartedGamesWithGamer(requestData);
			case "getNotStartedGamesWithNoGamer" -> getNotStartedGamesWithNoGamer(requestData);
			case "getStartedGamesWithGamer" -> getStartedGamesWithGamer(requestData);
			case "moveProcessing" -> moveProcessing(requestData);
			case "gameOver" -> gameOver(requestData);
			case "getGameGamers" -> getGameGamers(requestData);
			case "loginGamer" -> loginGamer(requestData);
			default -> new Response(ResponseCode.WRONG_REQUEST_TYPE,
					requestType);
			};
		} catch (Exception e) {
			response = new Response(ResponseCode.WRONG_REQUEST_DATA,
					e.getMessage());
		}
		return response;
	}
	Response loginGamer(String requestData) {
		String username = bcService.loginGamer(requestData);
		return getResponseOk(username);
	}

	Response getNotStartedGamesWithGamer(String requestData) {
		List<Long> games = bcService.getNotStartedGamesWithGamer(requestData);
		String responseString = resultsToJSON(games);
		return getResponseOk(responseString);
	}

	Response getNotStartedGamesWithNoGamer(String requestData) {
		List<Long> games = bcService.getNotStartedGamesWithNoGamer(requestData);
		String responseString = resultsToJSON(games);
		return getResponseOk(responseString);
	}

	Response getStartedGamesWithGamer(String requestData) {
		List<Long> games = bcService.getStartedGamesWithGamer(requestData);
		String responseString = resultsToJSON(games);
		return getResponseOk(responseString);
	}

	Response createGame(String requestData) {
		long gameId = bcService.createGame();
		String responseString = Long.toString(gameId);
		return getResponseOk(responseString );
	}
	Response startGame(String requestData) {
		long gameId = Long.parseLong(requestData);
		List<String> gamers = bcService.startGame(gameId);
		String responseString = resultsToJSON(gamers);
		return getResponseOk(responseString );
	}
	Response registerGamer(String requestData) {
        GamerDto gamer = new GamerDto(new JSONObject(requestData));
		bcService.registerGamer(gamer.username(),gamer.birthdate());
		String responseString = "";
		return getResponseOk(responseString );
	}
	Response gamerJoinGame(String requestData) {
		GameGamerDto gameGamer = new GameGamerDto(new JSONObject(requestData)) ;
		bcService.gamerJoinGame(gameGamer.gameId(), gameGamer.username());
		String responseString = "";
		return getResponseOk(responseString );
	}
	Response getNotStartedGames(String requestData) {
		List<Long> notStartedGames = bcService.getNotStartedGames();
		String responseString = resultsToJSON(notStartedGames);
		return getResponseOk(responseString );
	}
	Response moveProcessing(String requestData) {
		SequenceGameGamerDto sggd =
				new SequenceGameGamerDto(new JSONObject(requestData));
		String moveSequence = sggd.sequence();
		long gameId = sggd.gameId();
		String username = sggd.username();
		List<MoveData> results = bcService.moveProcessing(moveSequence, gameId, username);
		String responseString = resultsToJSON(results);
		return getResponseOk(responseString );
	}
	Response gameOver(String requestData) {
		long gameId = Long.parseLong(requestData);
		boolean isOver = bcService.gameOver(gameId);
		String responseString = Boolean.toString(isOver);
		return getResponseOk(responseString );
	}
	Response getGameGamers(String requestData) {
		long gameId = Long.parseLong(requestData);
		List<String> getGameGamer =bcService.getGameGamers(gameId);
		String responseString = resultsToJSON(getGameGamer);
		return getResponseOk(responseString );
	}

	private Response getResponseOk(String responseString) {
		
		return new Response(ResponseCode.OK, responseString);
	}
private <T> String resultsToJSON(List<T> res) {
		
		return res.stream().map(T::toString)
				.collect(Collectors.joining(";"));
	}

}
