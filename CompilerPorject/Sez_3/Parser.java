
//EX 3.1 parser per il controllo della correttezza della grammatica per semplici espressioni aritmetiche;
package Sez_3;

import java.io.*;
import Sez_2.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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
        expr();
	    match(Tag.EOF);
    }

    private void expr() {
	    term();
        exprp();
    }

    private void exprp() {
	switch (look.tag) {
        case '+':
            match('+');
            term();
            exprp();
            break;
        case '-':
            match('-');
            term();
            exprp();
            break;
	}
    }

    private void term() {
            fact();
            termp();
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                term();
                exprp();
                break;
            case '/':
                match('/');
                term();
                exprp();
                break;
        }
    }

    private void fact() {
        switch(look.tag){
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case '(':
                match('(');
                expr();
                match(')');
                break;
            default:
                error("Expected (expr) or NUM - Procedure Fact ");
                break;
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/Sez_3/Scratch1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}