import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    List<Card> cards = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public void getCards(List<Card> tableCards){
        while (cards.size() < 6){
            cards.add(tableCards.remove(0));
        }
    }

    public Card getCard(int index){
        return cards.get(index);
    }

    public void printCards(){
        for (int i = 0; i < cards.size(); i ++){
            cards.get(i).printCard();
            if (i < cards.size() - 1){
                System.out.print(", ");
            }
        }
    }

    public Card removeCard(int index){
        return cards.remove(index);
    }

    public Card makeMove(boolean allowExit){
        System.out.print("\nChoose the index of card from 1 to " + cards.size());

        if (allowExit){
            System.out.print("\nPrint 0 if you want to exit");
        }
        System.out.print( ": ");

        int index = scanner.nextInt();
        while (index < (allowExit ? 0 : 1) || index > cards.size()){
            System.out.print("You entered incorrect value. Try again: ");
            index = scanner.nextInt();
        }

        if (index == 0){
            return null;
        }

        return getCard(index - 1);
    }

    public Card makeMove(){
        System.out.println("\nPrint 0 if you to take card");
        System.out.print("Choose the index of card from 1 to " + cards.size() +": ");
        int index = scanner.nextInt();
        while (index < 0 || index > cards.size()){
            System.out.print("You entered incorrect value. Try again: ");
            index = scanner.nextInt();
        }

        if (index == 0){
            return null;
        }

        return getCard(index - 1);
    }

    public int getIndex(Card card){
        int index = -1;
        for (Card c : cards){
            index++;
            if (c == card) return index;
        }
        return -1;
    }
    
    protected void sortCards(Deck deck, String trump){
        cards.sort((card1, card2) ->{
            boolean isCard1Trump = card1.getSuit().equals(trump);
            boolean isCard2Trump = card2.getSuit().equals(trump);

            if (isCard1Trump && isCard2Trump){
                return Integer.compare(deck.findValueIndex(card1.getValue()), deck.findValueIndex(card2.getValue()));
            } else if (isCard1Trump){
                return 1;
            } else if (isCard2Trump){
                return -1;
            } else{
                return Integer.compare(deck.findValueIndex(card1.getValue()), deck.findValueIndex(card2.getValue()));
            }
        });
    }

}