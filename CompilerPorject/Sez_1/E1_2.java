package Sez_1;

/*Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il `
simbolo di “underscore” _ che non comincia con un numero e che non puo essere composto solo `
dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x 1”, “lft lab”, “ temp”, “x 1 y 2”,
“x ”, “5”
Esempi di stringhe non accettate: “5”, “221B”, “123”, “9 to 5”, “ ”*/
public class E1_2 {

    public static boolean scan(String s){
        int state=0;
        int i = 0;
        final String number = "123456789";
        final String alpha = "abcdefghijklmnopqrstuvwxyz";

        while(state>=0 & i < s.length()){
            final char ch = s.charAt(i++);
            boolean b = (alpha.indexOf(ch) != -1) || (number.indexOf(ch) != -1) || ch == '_';
            switch (state){
                case 0: {
                    if (ch == '_')
                        state = 1;
                    else if (alpha.indexOf(ch) != -1)
                        state = 2;
                    else
                        state = -1;
                    break;

                }case 1: {
                    if (ch == '_')
                        state = 1;
                    else if (b)
                        state = 2;

                    else
                        state = -1;
                    break;

                }
                case 2:{
                    if (b)
                        state = 2;
                    else
                        state = -1;
                    break;

                }
            }

        }

        return state == 2;
    }

    public static void main(String[] args) {

        System.out.println(scan("flag1") ? "ok" : "nope");
        System.out.println(scan("123_abc67") ? "ok" : "nope");
        System.out.println(scan("___abc67") ? "ok" : "nope");
        System.out.println(scan("__") ? "ok" : "nope");
        System.out.println(scan("_ab.&c67") ? "ok" : "nope");
        System.out.println(scan("x") ? "ok" : "nope");
        System.out.println(scan("9_to_5") ? "ok" : "nope");
        System.out.println(scan("___1234") ? "ok" : "nope");



    }
}
