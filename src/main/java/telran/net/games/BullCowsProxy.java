package telran.net.games;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import telran.net.Request;
import telran.net.TcpClient;
import telran.net.games.model.MoveData;
import telran.net.games.model.GameGamerDto;
import telran.net.games.model.SequenceGameGamerDto;
import telran.net.games.service.BullsCowsService;

public class BullCowsProxy implements BullsCowsService {
    private final TcpClient tcpClient;

    public BullCowsProxy(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    @Override
    public long createGame() {
        String response = tcpClient.sendAndReceive(new Request("createGame", ""));
        return Long.parseLong(response);
    }

    @Override
    public List<String> startGame(long gameId) {
        String response = tcpClient.sendAndReceive(new Request("startGame", String.valueOf(gameId)));
        return Arrays.asList(response.split(";"));
    }

    @Override
    public void registerGamer(String username, LocalDate birthDate) {
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("birthDate", birthDate.toString());
        tcpClient.sendAndReceive(new Request("registerGamer", json.toString()));
    }

    @Override
    public void gamerJoinGame(long gameId, String username) {
        GameGamerDto gameGamerDto = new GameGamerDto(gameId, username);
        tcpClient.sendAndReceive(new Request("gamerJoinGame", gameGamerDto.toString()));
    }

    @Override
    public List<Long> getNotStartedGamesWithGamer(String username) {
        String response = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithGamer", username));
        return parseLongList(response);
    }

    @Override
    public List<Long> getNotStartedGamesWithNoGamer(String username) {
        String response = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithNoGamer", username));
        return parseLongList(response);
    }

    @Override
    public List<Long> getStartedGamesWithGamer(String username) {
        String response = tcpClient.sendAndReceive(new Request("getStartedGamesWithGamer", username));
        return parseLongList(response);
    }

    @Override
    public List<Long> getNotStartedGames() {
        String response = tcpClient.sendAndReceive(new Request("getNotStartedGames", ""));
        return parseLongList(response);
    }

    @Override
    public List<MoveData> moveProcessing(String sequence, long gameId, String username) {
        SequenceGameGamerDto sggd = new SequenceGameGamerDto(sequence, gameId, username);
        String response = tcpClient.sendAndReceive(new Request("moveProcessing", sggd.toString()));
        return Arrays.stream(response.split(";"))
                .map(s -> new MoveData(new JSONObject(s)))
                .toList();
    }

    @Override
    public boolean gameOver(long gameId) {
        String response = tcpClient.sendAndReceive(new Request("gameOver", String.valueOf(gameId)));
        return Boolean.parseBoolean(response);
    }

    @Override
    public List<String> getGameGamers(long gameId) {
        String response = tcpClient.sendAndReceive(new Request("getGameGamers", String.valueOf(gameId)));
        return Arrays.asList(response.split(";"));
    }

   
    private List<Long> parseLongList(String response) {
        return Arrays.stream(response.split(";"))
                .map(Long::parseLong)
                .toList();
    }
}
