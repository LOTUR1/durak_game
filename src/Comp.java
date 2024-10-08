import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Comp extends Player{
    Random random = new Random();

    public Card makeMove(Deck deck, String trump){
        int index = evaluateCards(deck, trump);
        return removeCard(index);
    }

    public int evaluateCards(Deck deck, String trump){
        List<Float> cardValues = new ArrayList<>();
        List<Float> temp;
        float value;
        double value1;
        double value2;

        sortCards(deck, trump);
        for (int i = 0; i < cards.size(); i++){
            if (i + 1 > cards.size() / 2){
                value1 = 1;
                value2 = (cards.size() * Math.pow(2, Math.abs((cards.size() / 2 - 1) - i + 1)));
            } else{
                value1 = (float) Math.pow(2, ((cards.size() / 2) - i)) - 1;
                value2 = (cards.size() * Math.pow(2, Math.abs((cards.size() / 2 - 1) - i)));
            }

            value = (float) (value1 / value2);

            cardValues.add(value);
        }

        temp = variants(cardValues);

        float rand = random.nextFloat();

        return checker(temp, rand);
    }

    private int checker(List<Float> temp, float rand){
        for (int i = 1; i < temp.size(); i++){
            if (temp.get(i - 1) < rand && temp.get(i) > rand){
                return i;
            }
        }
        return 0;
    }

    private List<Float> variants(List<Float> cardValues){
        List<Float> temp = new ArrayList<>();
        float tempValue = 0;

        for (int i = 0; i < cardValues.size(); i++){
            tempValue += cardValues.get(i);
            temp.add(tempValue);
        }

        return temp;
    }

    public Card makeMove(Set<Card> possibleCards){
        if (generateDecision()){
            List<Card> cardList = new ArrayList<>(possibleCards);
            int randomIndex = getRandomIndex(cardList.size());
            Card selectedCard = cardList.get(randomIndex);
            cards.remove(selectedCard);
                possibleCards.remove(selectedCard);
            return selectedCard;
        } else{
            return null;
        }
    }

    private boolean generateDecision(){
        int number = random.nextInt(10);
        return number > 2;
    }

    public int getRandomIndex(int index){
        return random.nextInt(index);
    }

    public Card compMove(Deck deck, String trump, Card userCard){
        List<Card> tempList = getPossibleMoves(deck, trump, userCard);

        System.out.print("Comp's possible cards: ");
        for (Card card : tempList) {
            card.printCard();
            System.out.print(" ");
        }
        System.out.println();

        Card moveCard = tempList.get(0);
        if (tempList.size() == 1){
            moveCard = tempList.remove(0);
        } else{
            for (int i = 1; i < tempList.size(); i++){
                if (moveCard.getSuit().equals(trump) && !tempList.get(i).getSuit().equals(trump)){
                    moveCard = tempList.get(i);
                } else if(deck.findValueIndex(moveCard.getValue()) > deck.findValueIndex(tempList.get(i).getValue())){
                    moveCard = tempList.get(i);
                }
            }
        }

        moveCard = cards.remove(getIndex(moveCard));
        return moveCard;
    }

    private List<Card> getPossibleMoves(Deck deck, String trump, Card userCard){
        List<Card> tempList = new ArrayList<>();
        for (Card compCard : cards){
            if (userCard.getSuit().equals(trump) && compCard.getSuit().equals(trump)){
                if (deck.findValueIndex(compCard.getValue()) > deck.findValueIndex(userCard.getValue())){
                    tempList.add(compCard);
                }
            } else{
                if (compCard.getSuit().equals(trump) && !userCard.getSuit().equals(trump)){
                    tempList.add(compCard);
                } else if (compCard.getSuit().equals(userCard.getSuit())){
                    if (deck.findValueIndex(compCard.getValue()) > deck.findValueIndex(userCard.getValue())){
                        tempList.add(compCard);
                    }
                }
            }
        }

        return tempList;
    }
}
