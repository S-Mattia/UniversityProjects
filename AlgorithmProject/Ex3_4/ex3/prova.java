package ex3;

import java.util.Comparator;

public class prova{
    static public void main(String[] args) {

        Comparator<String> Comparator = new ComparatorString();
        PriorityQueue<String> prova = new PriorityQueue<String>(Comparator);
        String parola = "zzanluca";
        String parola1 = "xxila";
        String parola2 = "ppietro";
        String parola3 = "llama";
        String parola4 = "estebaldo";
        String parola5 = "yysolde";
        String parola6 = "zovanni";
        String parola7 = "mirco";
        String parola8 = "alessandro";
        String parola9 = "crasimiro";
        String parola10 = "gino";
        String parola11 = "crazi";
        System.out.println(prova.push(parola4));
        System.out.println(prova.push(parola2));
        System.out.println(prova.push(parola1));
        System.out.println(prova.push(parola5));
        System.out.println(prova.push(parola));
        System.out.println(prova.push(parola3));
        System.out.println(prova.push(parola3));
        System.out.println(prova.push(parola6));
        System.out.println(prova.push(parola8));
        System.out.println(prova.push(parola7));
        System.out.println(prova.push(parola9));
        System.out.println(prova.push(parola10));
        System.out.println(prova.push(parola11));
        System.out.println(prova.push(parola7));

        
        
        
        for(int i = 0; i<12;i++){
            System.out.println((String) prova.top());
            prova.pop();
        }
        
       
    }

}