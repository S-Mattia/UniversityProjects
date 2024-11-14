
/*EX 4.1 Valutatore per semplici operazioni aritmetiche, che calcola il valore finale
dell'operazione partendo da una espressione aritmetica. Utilizzando una grammatica L-attribuita*/

package Sez_4;

import java.io.*;
import Sez_2.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
	lex = l; 
	pbr = br;
	move(); 
    }
   
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void start() { 
	    int expr_val;
    	expr_val = expr();
	    match(Tag.EOF);
        System.out.println(expr_val);
    }

    private int expr() { 
	    int term_val, exprp_val;
    	term_val = term();
	    exprp_val = exprp(term_val);
	    return exprp_val;
    }

    private int exprp(int exprp_i) {
	int term_val;
    int exprp_val=exprp_i;
	switch (look.tag) {
        case '+':
            match('+');
            term_val = term();
            exprp_val = exprp(exprp_val + term_val);
            break;
        case '-':
            match('-');
            term_val = term();
            exprp_val = exprp(exprp_val - term_val);
            break;
	}
    return exprp_val;
    }

    private int term() {
        int fact_val, termp_val;
        fact_val = fact();
        termp_val = termp(fact_val);
        return termp_val;
    }
    
    private int termp(int termp_i) {
        int fact_val;
        int termp_val=termp_i;
        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_val * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_val / fact_val);
                break;
        }
        return termp_val;
    }
    
    private int fact() {
        int factV=0;
        switch(look.tag){
            case Tag.NUM:
                factV = ((NumberTok) look).number;
                match(Tag.NUM);
                return factV;
            case '(':
                match('(');
                factV = expr();
                match(')');
                return factV;
            default:
                error("Expected (expr) or NUM - procedure fact ");
                return factV;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/Sez_4/Scratch.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
