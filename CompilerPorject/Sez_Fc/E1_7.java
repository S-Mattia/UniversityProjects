package Sez_Fc;

/*------------ESERCIZIO FACOLTATIVO N°1.7
Modificare l’automa dell’esercizio 1.3 (esercizi obbligatori su DFA) in modo che riconosca le combinazioni di
matricola e cognome di studenti del turno 2 o del turno 3 del laboratorio, dove il numero di matricola e il cognome
possono essere separati da una sequenza di spazi, e possono essere precedute e/o seguite da sequenze eventualmente
vuote di spazi. Per esempio, l’automa deve accettare la stringa “654321 Rossi” e “ 123456 Bianchi ” (dove, nel secondo esempio,
ci sono spazi prima del primo carattere e dopo l’ultimo carattere), ma non “1234 56Bianchi” e “123456Bia nchi”.
Per questo esercizio, i cognomi composti (con un numero arbitrario di parti) possono essere accettati: per esempio,
la stringa “123456De Gasperi” deve essere accettata. Modificare l’implementazione Java dell’automa di conseguenza.*/
public class E1_7 {
    public static boolean scan(String s){
        int state = 0;
        int i = 0;

        String odd= "13579";
        String even="24680";
        String AK= "ABCDEFGHIJKabcdefghijk";
        String LZ= "LMNOPQRSTUVWXYZlmnopqrstuvwxyz";

        while(state >= 0 & i < s.length()){

            final char ch = s.charAt(i++);

            boolean FirstLZ = LZ.indexOf(ch) != -1 && Character.isUpperCase(ch);
            boolean FirstAK = AK.indexOf(ch) != -1 && Character.isUpperCase(ch);
            boolean Odd = odd.indexOf(ch) != -1;
            boolean Even = even.indexOf(ch) != -1;

            switch(state){
                case 0:{
                    if(Odd) {
                        state = 1;
                    }else if(Even) {
                        state = 2;
                    }else if(ch == ' ') {
                        state = 0;
                    }else{
                        state = -1;
                    }break;
                }
                case 1:{
                    if(Odd){
                        state=1;
                    }else if(Even){
                        state=2;
                    }else if(FirstLZ){
                        state=5;
                    }else if(ch == ' ') {
                        state = 3;
                    }else{
                        state = -1;
                    }break;
                }
                case 2:{
                    if(Odd){
                        state=1;
                    }else if(Even){
                        state=2;
                    }else if(FirstAK){
                        state=5;
                    }else if(ch == ' ') {
                        state = 4;
                    } else{
                        state = -1;
                    }break;
                }
                case 3:{
                    if(FirstLZ){
                        state=5;
                    }else if(ch == ' ') {
                        state = 3;
                    } else{
                        state = -1;
                    }break;
                }
                case 4:{
                    if(FirstAK){
                        state=5;
                    }else if(ch == ' ') {
                        state = 4;
                    } else{
                        state = -1;
                    }break;
                }
                case 5:{
                    if(Character.isLetter(ch)){
                        state = 5;
                    }else if(ch == ' '){
                        state = 6;
                    }else{
                        state = -1;
                    }break;
                }
                case 6:{
                    if(FirstAK|FirstLZ){
                        state = 5;
                    }else if(ch == ' '){
                        state = 6;
                    }else{
                        state = -1;
                    }
                }
            }
        }
        return state==5 | state == 6;
    }

    public static void main(String[] args) {

        System.out.println(scan("   123456   Bianchi   ") ? "ok" : "nope");
        System.out.println(scan("    654321     Rossi  ") ? "ok" : "nope");
        System.out.println(scan("4De Gregorio  ") ? "ok" : "nope");
        System.out.println(scan("2  s") ? "ok" : "nope");
        System.out.println(scan("2 b") ? "ok" : "nope");
        System.out.println(scan("1 s") ? "ok" : "nope");
        System.out.println(scan("12giorgi23") ? "ok" : "nope");
        System.out.println(scan("rossi") ? "ok" : "nope");
        System.out.println(scan("123456789") ? "ok" : "nope");


    }
}
