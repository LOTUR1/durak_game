import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    protected String[] suit = {"♠", "♥", "♦", "♣"};
    protected String[] value = {"6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    protected List<Card> cards = new ArrayList<>();

    public void createCards(){
        for (String s : suit) {
            for (String v : value) {
                cards.add(new Card(v, s ));
            }
        }
        shuffleCards();
    }

    public void shuffleCards(){
        Random random = new Random();
        Card temp;
        int randNumber;
        for (int i = 0; i < cards.size(); i++){
            randNumber = random.nextInt(cards.size());
            temp = cards.get(randNumber);
            cards.set(randNumber, cards.get(i));
            cards.set(i, temp);
        }
    }

    public void printCards(){
        for (Card card : cards){
            card.printCard();
        }
    }

    public int findValueIndex(String v){
        int index = -1;
        for (int i = 0; i < value.length; i++){
            if (v.equals(value[i])){
                index = i;
                break;
            }
        }
        return index;
    }

    public String getTrump(){
        return cards.get(cards.size() - 1).getSuit();
    }
}