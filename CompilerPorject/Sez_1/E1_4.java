package Sez_1;


/*Progettare e implementare un DFA che riconosca il linguaggio delle costanti numeriche in virgola mobile utilizzando
la notazione scientifica dove il simbolo e indica la funzione esponenziale con base 10. L’alfabeto del DFA
contiene i seguenti elementi: le cifre numeriche 0, 1, . . . , 9, il segno . (punto) che precede una eventuale parte decimale,
i segni + (piu) e ` - (meno) per indicare positivita o negatività, e il simbolo ` e*/
public class E1_4 {

    public static boolean scan(String s) {

        int state = 0;
        int i = 0;
        String Num = "1234567890";

        while (state >= 0 & i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0: {
                    if (ch == '-' | ch == '+') {
                        state = 1;

                    } else if (Num.indexOf(ch) != -1) {
                        state = 2;

                    } else if (ch == '.') {
                        state = 3;

                    } else {
                        state = -1;

                    }
                    break;
                }
                case 1: {
                    if (Num.indexOf(ch) != -1) {
                        state = 2;

                    } else if (ch == '.') {
                        state = 3;

                    } else {
                        state = -1;

                    }
                    break;
                }
                case 2: {
                    if (Num.indexOf(ch) != -1) {
                        state = 2;

                    } else if (ch == '.') {
                        state = 3;

                    } else if (ch == 'e') {
                        state = 4;

                    } else {
                        state = -1;

                    }
                    break;

                }
                case 3: {
                    if (Num.indexOf(ch) != -1) {
                        state = 8;

                    } else {
                        state = -1;

                    }
                    break;
                }
                case 4: {
                    if (ch == '-' | ch == '+') {
                        state = 5;

                    } else if (Num.indexOf(ch) != -1) {
                        state = 6;

                    } else if (ch == '.') {
                        state = 7;

                    } else {
                        state = -1;

                    }
                    break;
                }
                case 5, 6: {
                    if (Num.indexOf(ch) != -1) {
                        state = 6;

                    } else if (ch == '.') {
                        state = 7;

                    } else {
                        state = -1;

                    }
                    break;

                }
                case 7, 9: {
                    if (Num.indexOf(ch) != -1) {
                        state = 9;
                    } else {
                        state = -1;

                    }
                    break;

                }
                case 8: {
                    if (Num.indexOf(ch) != -1) {
                        state = 8;
                    } else if (ch == 'e') {
                        state = 4;

                    } else {
                        state = -1;

                    }
                    break;

                }

            }

        }

        return (state == 2 || state == 6 || state == 9 || state == 8);
    }

    public static void main(String[] args) {

        System.out.println(scan("+123") ? "ok" : "nope");
        System.out.println(scan("-123.65") ? "ok" : "nope");
        System.out.println(scan("123e10") ? "ok" : "nope");
        System.out.println(scan("123.5e10") ? "ok" : "nope");
        System.out.println(scan("123.5e10.5") ? "ok" : "nope");
        System.out.println(scan("123.e10") ? "ok" : "nope");
        System.out.println(scan("-.3") ? "ok" : "nope");
        System.out.println(scan("-e4") ? "ok" : "nope");
        System.out.println(scan("-.7e5") ? "ok" : "nope");
        System.out.println(scan("-1e5e7") ? "ok" : "nope");
        System.out.println(scan("-1.789e-.9") ? "ok" : "nope");


    }
}