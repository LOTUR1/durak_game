public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.gameStart();
        Player player = game.user;
        player.printCards();
        System.out.println();
        player.sortCards(game.deck, game.trump);
        player.printCards();
    }
}