package Sez_1;

/*Modificare l’automa dell’esercizio precedente in modo che riconosca il linguaggio
di stringhe (sull’alfabeto {/, *, a}) che contengono “commenti” delimitati da /* e * /, ma con
la possibilita di avere stringhe prima e dopo*/
public class E1_6 {

    public static boolean scan(String s){

        int state = 0;
        int i = 0;

        while(state >= 0 & i < s.length()){
            final char ch = s.charAt(i++);
            switch(state){
                case 0:{
                    if(ch=='/') {
                        state = 1;
                    }else if(ch=='a'|ch=='*'){
                        state = 0;
                    }else{
                        state = -1;
                    }break;
                } case 1:{
                    if(ch=='*'){
                        state=2;
                    }else if(ch=='/'){
                        state=1;
                    }else if(ch=='a'){
                        state=0;
                    }else{
                        state = -1;
                    }break;
                } case 2:{
                    if(ch=='a'){
                        state=3;
                    }else if(ch=='*'){
                        state=4;
                    }else{
                        state = -1;
                    }break;

                } case 3:{
                    if(ch=='/' | ch=='a'){
                        state=3;

                    }else if(ch=='*'){
                        state=4;

                    }else{
                        state = -1;

                    }break;
                }case 4:{
                    if(ch =='a'){
                        state=3;

                    }else if(ch =='*'){
                        state=4;

                    }else if(ch=='/'){
                        state=0;

                    }else{
                        state = -1;

                    }break;
                }

            }

        }

        return state==0 || state ==1;
    }

    public static void main(String[] args) {

        System.out.println(scan("aa**/**/aaa") ? "ok" : "nope");
        System.out.println(scan("///aaa/*aa/aa//*/") ? "ok" : "nope");
        System.out.println(scan("aa/*aa/aa//*/") ? "ok" : "nope");
        System.out.println(scan("*aa/aa//*/") ? "ok" : "nope");
        System.out.println(scan("/**a/*/") ? "ok" : "nope");
        System.out.println(scan("/***/*/") ? "ok" : "nope");
        System.out.println(scan("aa/*a*a*/") ? "ok" : "nope");
        System.out.println(scan("aaa") ? "ok" : "nope");
        System.out.println(scan("aa*****///*aa/aa*/**aa//") ? "ok" : "nope");
        System.out.println(scan("///aaa**aa*/***a/aaa///aaa/*///a**a/") ? "ok" : "nope");
        System.out.println(scan("aa//**/aaa///*aaa//a*/aaa//a***/") ? "ok" : "nope");



    }

}