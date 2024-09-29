package telran.net.games;

import telran.net.TcpClient;
import telran.net.games.client.BullsCowsApplItems;
import telran.net.games.client.BullsCowsProxy;
import telran.net.games.service.BullsCowsService;
import telran.view.Item;
import telran.view.Menu;
import telran.view.SystemInputOutput;

import java.util.List;

import static telran.net.games.BullsCowsServerAppl.PORT;

public class BullsCowsClientAppl {
    public static void main(String[] args) {
        TcpClient tcpClient = new TcpClient("localhost",PORT);
        BullsCowsService service = new BullsCowsProxy(tcpClient);
        List<Item> menuItems = BullsCowsApplItems.getMenu(service);
        menuItems.add(Item.of("Exit & close connection", io -> tcpClient.close(),true));
        Menu menu = new Menu("Bulls and Cows Main Menu",menuItems.toArray(Item[]::new));
        menu.perform(new SystemInputOutput());
    }


}
