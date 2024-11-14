package Sez_Fc;

/*Esercizio 1.9
Progettare e implementare un DFA con alfabeto {a, b} che riconosca il linguaggio
delle stringhe tali che a occorre almeno una volta in una delle ultime tre posizioni
 della stringa. Il DFA deve accettare anche stringhe che contengono meno di tre simboli
 (ma almeno uno dei simboli deve essere a). Esempi di stringhe accettate: “abb”, “bbaba”,
 “baaaaaaa”, “aaaaaaa”, “a”, “ba”, “bba”, “aa”, “bbbababab” Esempi di stringhe non accettate:
 “abbbbbb”, “bbabbbbbbbb”, “b”.*/
public class E1_9 {

    public static boolean scan(String s){
        int state=0;
        int i = 0;

        while(state>=0 & i < s.length()){
            final char ch = s.charAt(i++);
            switch (state){
                case 0, 3: {
                    if (ch == 'a')
                        state = 1;
                    else if (ch == 'b')
                        state = 0;
                    else
                        state = -1;
                    break;
                }
                case 1: {
                    if (ch == 'a')
                        state = 1;
                    else if (ch=='b')
                        state = 2;
                    else
                        state = -1;
                    break;
                }
                case 2: {
                    if (ch == 'a')
                        state = 1;
                    else if (ch=='b')
                        state = 3;
                    else
                        state = -1;
                    break;
                }
            }
        }
        return state == 1 || state == 2 | state == 3;
    }

    public static void main(String[] args) {

        System.out.println(scan("abb") ? "ok" : "nope");
        System.out.println(scan("") ? "ok" : "nope");
        System.out.println(scan("bbbbbb") ? "ok" : "nope");
        System.out.println(scan("aaaabb") ? "ok" : "nope");
        System.out.println(scan("bbbbbbbba") ? "ok" : "nope");
        System.out.println(scan("abbb") ? "ok" : "nope");
        System.out.println(scan("aba") ? "ok" : "nope");
        System.out.println(scan("bba") ? "ok" : "nope");



    }

}
