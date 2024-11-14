package Sez_1;

/*Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
contengono un numero di matricola seguito (subito) da un cognome, dove la combinazione di
matricola e cognome corrisponde a studenti del turno 2 o del turno 3 del laboratorio di Linguaggi
Formali e Traduttori.*/
public class E1_3 {

    public static boolean scan(String s){

        int state = 0;
        int i = 0;
        String odd= "13579";
        String even="24680";
        String alphat1= "ABCDEFGHIJKabcdefghijk";
        String alphat2= "LMNOPQRSTUVWXYZlmnopqrstuvwxyz";

        while(state >= 0 & i < s.length()){
            final char ch = s.charAt(i++);
            boolean FirstAK = alphat1.indexOf(ch) != -1;
            boolean FirstLZ = alphat2.indexOf(ch) != -1;
            boolean Odd = odd.indexOf(ch) != -1;
            boolean Even = even.indexOf(ch) != -1;
            switch(state){
                case 0:{
                    if(Odd) {
                        state = 1;
                    }else if(Even) {
                        state = 2;
                    }else{
                        state = -1;
                    }break;
                } case 1:{
                    if(Odd){
                        state=1;
                    }else if(Even){
                        state=2;
                    }else if(FirstLZ){
                        state=3;
                    }else{
                        state = -1;
                    }break;
                } case 2:{
                    if(Odd){
                        state=1;
                    }else if(Even){
                        state=2;
                    }else if(FirstAK){
                        state=3;
                    }else{
                        state = -1;
                    }break;
                } case 3:{
                    if(FirstAK || FirstLZ){
                        state=3;
                    }else{
                        state = -1;
                    }break;
                }
            }
        }
        return state==3;
    }

    public static void main(String[] args) {

        System.out.println(scan("123456bianchi") ? "ok" : "nope");
        System.out.println(scan("654321rossi") ? "ok" : "nope");
        System.out.println(scan("1b") ? "ok" : "nope");
        System.out.println(scan("2s") ? "ok" : "nope");
        System.out.println(scan("2b") ? "ok" : "nope");
        System.out.println(scan("1s") ? "ok" : "nope");
        System.out.println(scan("12gato23") ? "ok" : "nope");
        System.out.println(scan("rossi") ? "ok" : "nope");
        System.out.println(scan("123456789") ? "ok" : "nope");


    }

}
