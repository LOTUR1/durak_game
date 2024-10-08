import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class Game {
    Player user = new Player();
    Comp comp = new Comp();
    Deck deck = new Deck();
    List<Card> cards;
    String trump;
    boolean isUserMove;
    boolean allowExit;

    public void gameStart(){
        deck.createCards();
        cards = deck.cards;

        user.getCards(cards);
        comp.getCards(cards);

        trump = deck.getTrump();
        System.out.println("Trump: " + trump + "\n");

        System.out.print("User cards: ");
        user.printCards();
        System.out.print("\nComp cards: ");
        comp.printCards();

        System.out.println("\n");
        isUserMove = isUserFirst();
        System.out.println("User first: " + isUserMove);
        System.out.println("\n");

        gameProcess();
    }

    public void gameProcess(){
        int roundCount = 0;
        while(!cards.isEmpty() || !user.cards.isEmpty() || !comp.cards.isEmpty()){
            roundCount++;
            for (int i = 0; i < 100; i++){
                System.out.print("-");
                if (i == 50){
                    System.out.print("Round " + roundCount);
                }
            }

            if (!cards.isEmpty()){
                user.getCards(cards);
                comp.getCards(cards);
            }

            System.out.println("\nCards in deck " + cards.size());

            table();

            if (cards.isEmpty()){
                if(comp.cards.isEmpty() && user.cards.isEmpty()){
                    System.out.println("Draw");
                    break;
                } else if(comp.cards.isEmpty()){
                    System.out.print("Comp wins!");
                    break;
                } else if(user.cards.isEmpty()){
                    System.out.println("User wins!");
                    break;
                }
            }
        }
    }

    public void table(){
        Card userCard;
        Card compCard;
        Set<Card> possibleCards = new HashSet<>();
        List<Card> movedCards = new ArrayList<>();
        allowExit = false;

        while (!user.cards.isEmpty() && !comp.cards.isEmpty()){
            if (allowExit && possibleCards.isEmpty()) {
                System.out.println("No more cards to move");
                break;
            }

            user.sortCards(deck, trump);
            System.out.print("\nUser cards: ");
            user.printCards();

            comp.sortCards(deck, trump);
            System.out.print("\nComp cards: ");
            comp.printCards();
            System.out.print("\nPossible cards: ");
            for (Card ca : possibleCards){
                ca.printCard();
                System.out.print(", ");
            }
            System.out.println("\nTrump: " + trump);
            System.out.println();

            if (isUserMove){
                userCard = user.makeMove(allowExit);

                if (userCard == null){
                    break;
                }

                if (allowExit){
                    while (!checkPossibleCards(userCard, possibleCards)){
                        System.out.println("You picked wrong card. Try again");
                        userCard = user.makeMove(allowExit);
                    }
                }

                userCard = user.cards.remove(user.cards.indexOf(userCard));

                System.out.print("User moves with card: ");
                userCard.printCard();
                System.out.println();
                movedCards.add(userCard);

                if (hasCardsToMove(userCard, comp.cards)){
                    compCard = comp.compMove(deck, trump, userCard);
                    System.out.print("Comp moves with card: ");
                    compCard.printCard();
                    System.out.println();
                    movedCards.add(compCard);

                    addPossibleCards(userCard, user.cards, possibleCards);
                    addPossibleCards(compCard, user.cards, possibleCards);
                } else{
                    System.out.println("Comp has no cards to move");
                    comp.cards.addAll(movedCards);
                    isUserMove = !isUserMove;
                    break;
                }
            } else{
                if (allowExit){
                    compCard = comp.makeMove(possibleCards);
                    if (compCard == null){
                        break;
                    }
                } else{
                    compCard = comp.makeMove(deck, trump);
                }
                System.out.print("Comp moves with card: " );
                compCard.printCard();
                System.out.println();

                movedCards.add(compCard);
                if (hasCardsToMove(compCard, user.cards)){
                    userCard = user.makeMove();

                    while (userCard != null && !isBeats(compCard, userCard)){
                        System.out.println("You pick wrong card. Try again");
                        userCard = user.makeMove();
                    }

                    if (userCard == null){
                        user.cards.addAll(movedCards);
                        isUserMove = !isUserMove;
                        break;
                    }

                    System.out.print("User moves with card: ");
                    userCard.printCard();
                    System.out.println();

                    movedCards.add(userCard);

                    addPossibleCards(userCard, comp.cards, possibleCards);
                    addPossibleCards(compCard, comp.cards, possibleCards);

                    user.removeCard(user.getIndex(userCard));
                } else{
                    System.out.println("You have no cards to move");
                    user.cards.addAll(movedCards);
                    isUserMove = !isUserMove;
                    break;
                }

                System.out.println();
            }
            allowExit = true;
        }
        isUserMove = !isUserMove;
    }

    public boolean checkPossibleCards(Card card, Set<Card> possibleCards){
        for (Card c : possibleCards){
            if (c.equals(card)) return true;
        }
        return false;
    }

    public void addPossibleCards (Card card, List<Card> attackCards, Set<Card> possibleCards){
        for (Card attackCard : attackCards){
            if (attackCard.getSuit().equals(card.getSuit()) || attackCard.getValue().equals(card.getValue())){
                possibleCards.add(attackCard);
            }
        }
    }

    private boolean isBeats(Card attackCard, Card defendCard){
        if (defendCard.getSuit().equals(attackCard.getSuit())){
            return deck.findValueIndex(defendCard.getValue()) > deck.findValueIndex(attackCard.getValue());
        }
        return defendCard.getSuit().equals(trump) && !attackCard.getSuit().equals(trump);
    }

    private boolean hasCardsToMove(Card attackCard, List<Card> defendCards){
        for (Card defendCard : defendCards){
            if (isBeats(attackCard, defendCard)) return true;
        }

        return false;
    }

    public boolean isUserFirst(){
        int userIndex = findSmallestTrumpCard(user.cards);
        int compIndex = findSmallestTrumpCard(comp.cards);
        return userIndex < compIndex;
    }

    private int findSmallestTrumpCard(List<Card> cc){
        int index = 15;

        for (Card card : cc) {
            if (card.getSuit().equals(trump)) {
                index = deck.findValueIndex(card.getValue());
            }
        }
        return index;
    }
}
