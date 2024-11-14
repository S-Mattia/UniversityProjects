package Sez_Fc;

/*Esercizio 1.10
Progettare e implementare un DFA che riconosca il linguaggio di stringhe
che contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione
di un carattere del nome con un altro qualsiasi. Ad esempio, nel caso di uno
studente che si chiama Paolo, il DFA deve accettare la stringa "Paolo"
(cioè il nome scritto correttamente), ma anche le stringhe "Pjolo", "caolo",
"Pa%lo", "Paola" e "Parlo" (il nome dopo la sostituzione di un carattere),
ma non "Eva", "Perro", "Pietro" oppure "P*o*o".
*/

public class E1_10 {

    public static boolean scan(String s){
        int state=0;
        int i = 0;
        boolean different = true;

        while(state>=0 & i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0: {
                    if (ch == 'm') {
                        state = 1;
                    } else if (different) {
                        different = false;
                        state = 1;
                    } else {
                        state = -1;
                    }
                    break;
                }
                case 1: {
                    if (ch == 'a') {
                        state = 2;
                    } else if (different) {
                        different = false;
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                }
                case 2: {
                    if (ch == 't') {
                        state = 3;
                    } else if (different) {
                        different = false;
                        state = 3;
                    } else {
                        state = -1;
                    }
                    break;
                }
                case 3: {
                    if (ch == 't') {
                        state = 4;
                    } else if (different) {
                        different = false;
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;
                }
                case 4: {
                    if (ch == 'i') {
                        state = 5;
                    } else if (different) {
                        different = false;
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                }
                case 5: {
                    if (ch == 'a') {
                        state = 6;
                    } else if (different) {
                        different = false;
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                }
                case 6: {
                        state = -1;
                    break;
                }
            }
        }
        return state == 6;
    }

    public static void main(String[] args) {

        System.out.println(scan("mattia") ? "ok" : "nope");
        System.out.println(scan("sattia") ? "ok" : "nope");
        System.out.println(scan("muttia") ? "ok" : "nope");
        System.out.println(scan("mlitia") ? "ok" : "nope");
        System.out.println(scan("ma%tia") ? "ok" : "nope");
        System.out.println(scan("mattiaaaa") ? "ok" : "nope");
        System.out.println(scan("aba") ? "ok" : "nope");
        System.out.println(scan("bba") ? "ok" : "nope");

    }

}
