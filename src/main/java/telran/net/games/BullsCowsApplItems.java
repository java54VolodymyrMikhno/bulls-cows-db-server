package telran.net.games;

import telran.net.games.service.BullsCowsService;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BullsCowsApplItems {
    static BullsCowsService service;
    static String username;
    static long gameId;

    public static List<Item> getMenu(BullsCowsService service) {
        BullsCowsApplItems.service = service;
        List<Item> menu = new ArrayList<>();
        menu.add(Item.of("Login", BullsCowsApplItems::loginGamer));
        menu.add(Item.of("Register", BullsCowsApplItems::registerGamer));
        return menu;
    }

    static void loginGamer(InputOutput io) {
        username = io.readString("Enter your name");
        io.writeLine("Logged in " + username);
        showMenuGame(io);
    }


    private static void registerGamer(InputOutput io) {
        username = io.readString("Enter your name");
        LocalDate birthDate = io.readIsoDateRange("Enter your BirthDate",
                "Wrong BirthDate", LocalDate.MIN, LocalDate.now().plusDays(1));
        service.registerGamer(username, birthDate);
        io.writeLine("Gamer registered successfully: " + username);
        showMenuGame(io);
    }

    private static void showMenuGame(InputOutput io) {
        Menu gameMenu = new Menu("Game menu",
                new Item[]{Item.of("Start Game", item -> startGame(io)),
                        Item.of("Continue Game",item -> JoinGame(io)),
                        Item.of("Join Game", item-> continueGame(io)),
                        Item.ofExit()
                }
        );
        gameMenu.perform(io);
    }


    private static void startGame(InputOutput io) {
        gameId = service.createGame();
        service.gamerJoinGame(gameId, username);
        service.startGame(gameId);
        playGame(io);
    }

    private static void playGame(InputOutput io) {
        Menu menuMoves = new Menu("Moves", new Item[]{
                Item.of("Enter your move ", BullsCowsApplItems::game),
                Item.ofExit()
        }
        );
        menuMoves.perform(io);
    }

    private static void game(InputOutput io) {
        String move = io.readString("Enter your move ");
         service.moveProcessing(move,gameId,username).forEach(io::writeLine);
        if(service.gameOver(gameId)) {
            io.writeLine("You Win!!! Game over!");
            showMenuGame(io);
        }
        playGame(io);
    }


    private static void JoinGame(InputOutput io) {

    }

    private static void continueGame(InputOutput io) {
    }


}
