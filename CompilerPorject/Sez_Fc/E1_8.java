package Sez_Fc;

/*Progettare e implementare un DFA che, come in Esercizio 1.3 (esercizi obbligatori su DFA),
riconosca il linguaggio di stringhe che contengono matricola e cognome di studenti del turno 2
o del turno 3 del laboratorio, ma in cui il cognome precede il numero di matricola (in altre parole,
le posizioni del cognome e matricola sono scambiate rispetto all’Esercizio 1.3).*/
public class E1_8 {

    public static boolean scan(String s){

        int state = 0;
        int i = 0;
        String odd= "13579";
        String even="24680";
        String AK= "ABCDEFGHIJKabcdefghijk";
        String LZ= "LMNOPQRSTUVWXYZlmnopqrstuvwxyz";


        while(state >= 0 & i < s.length()){
            final char ch = s.charAt(i++);
            boolean FirstLZ = LZ.indexOf(ch) != -1 ;
            boolean FirstAK = AK.indexOf(ch) != -1 ;
            boolean Odd = odd.indexOf(ch) != -1;
            boolean Even = even.indexOf(ch) != -1;
            switch(state){
                case 0:{
                    if(FirstAK){
                        state=1;
                    }else if(FirstLZ){
                        state=2;
                    }else{
                        state = -1;
                    }break;
                }
                case 1:{
                    if(Character.isLetter(ch)){
                        state=1;
                    }else if(Even) {
                        state = 3;
                    }else if(Odd) {
                        state = 5;
                    }else{
                        state = -1;
                    }break;
                }case 2:{
                    if(Character.isLetter(ch)){
                        state=2;
                    }else if(Odd){
                        state=4;
                    }else if(Even){
                        state=6;
                    }else{
                        state = -1;
                    }break;
                }case 3:{
                    if(Even) {
                        state = 3;
                    }else if(Odd) {
                        state = 5;
                    }else{
                        state = -1;
                    }break;
                } case 4:{
                    if(Odd) {
                        state = 4;
                    }else if(Even) {
                        state = 6;
                    }else{
                        state = -1;
                    }break;
                }case 5:{
                    if(Odd) {
                        state = 5;
                    }else if(Even) {
                        state = 3;
                    }else{
                        state = -1;
                    }break;
                }case 6:{
                    if(Even) {
                        state = 6;
                    }else if(Odd) {
                        state = 4;
                    }else{
                        state = -1;
                    }break;
                }

            }

        }
        return state==4 | state == 3;
    }

    public static void main(String[] args) {

        System.out.println(scan("bianchi123456") ? "ok" : "nope");
        System.out.println(scan("654321rossi") ? "ok" : "nope");
        System.out.println(scan("rossi654321") ? "ok" : "nope");
        System.out.println(scan("1b") ? "ok" : "nope");
        System.out.println(scan("2s") ? "ok" : "nope");
        System.out.println(scan("2b") ? "ok" : "nope");
        System.out.println(scan("1s") ? "ok" : "nope");
        System.out.println(scan("gabo22") ? "ok" : "nope");
        System.out.println(scan("rossi") ? "ok" : "nope");
        System.out.println(scan("123456789") ? "ok" : "nope");


    }

}
