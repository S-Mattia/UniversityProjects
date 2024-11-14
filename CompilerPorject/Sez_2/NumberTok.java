//Classe supporto EX 2: implementazione di token per i numeri;

package Sez_2;

public class NumberTok extends Token {
	public int number;
    public NumberTok(String n) { super(Tag.NUM); number = Integer.parseInt(n); }

    public String toString() { return "<" + tag + ", " + number + ">"; }


}
