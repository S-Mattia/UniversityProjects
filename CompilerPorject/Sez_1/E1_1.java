package Sez_1;

/*Copiare il codice in Listing 1, compilarlo e testarlo su un insieme significativo di
stringhe, per es. “010101”, “1100011001”, “10214”, ecc.
Come deve essere modificato il DFA in Figure 1 per riconoscere il linguaggio complementare,
ovvero il linguaggio delle stringhe di 0 e 1 che non contengono 3 zeri consecutivi? Progettare e
implementare il DFA modificato, e testare il suo funzionamento.*/
public class E1_1
{
    public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if(ch == '0')
                        state = 1;
                    else if(ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '0')
                        state = 2;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;

            }

        }

        return state >= 0;
    }

    public static void main(String[] args) {

        System.out.println(scan("001101") ? "ok" : "nope");
        System.out.println(scan("011110000101") ? "ok" : "nope");
        System.out.println(scan("01101110") ? "ok" : "nope");
        System.out.println(scan("11ES1101") ? "ok" : "nope");
        System.out.println(scan("") ? "ok" : "nope");
        System.out.println(scan("1") ? "ok" : "nope");
        System.out.println(scan("0") ? "ok" : "nope");
        System.out.println(scan("0010110011101111001101") ? "ok" : "nope");

    }
}