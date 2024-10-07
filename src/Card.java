public class Card {
    private final String suit;
    private final String value;

    public Card(String value, String suit){
        this.value = value;
        this.suit = suit;
    }

    public String getSuit(){
        return suit;
    }

    public String getValue(){
        return value;
    }

    public void printCard(){
        System.out.print(value + suit);
    }
}
