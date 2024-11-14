
//EX 3.2 parser per il controllo della correttezza della grammatica per un semplice linguaggio di programmazione;

package Sez_3;

import Sez_2.Lexer;
import Sez_2.Tag;
import Sez_2.Token;
import Sez_2.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Parser1 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser1(Lexer l, BufferedReader br) {
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


    public void prog() {
        statlist();
        match(Tag.EOF);
    }


    private void statlist() {
        stat();
        statlistp();
    }

    private void statlistp(){
        if(look.tag== Word.semicolon.tag){
            match(Word.semicolon.tag);
            stat();
            statlistp();
        }
    }

    private void stat(){
        switch(look.tag){
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                assignlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist();
                match(')');
                break;
            case Tag.FOR:
                match(Tag.FOR);
                match('(');
                if(look.tag==Tag.ID){
                    match(Tag.ID);
                    match(Tag.INIT);
                    expr();
                    match(Word.semicolon.tag);
                }
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;

            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                if(look.tag==Tag.ELSE){
                    match(Tag.ELSE);
                    stat();
                }
                match(Tag.END);
                break;
            case '{':
                match('{');
                statlist();
                match('}');
                break;
            default:
                error("Syntax error <stat> ");
        }
    }

    private void assignlist(){
        if(look.tag == '['){
            match('[');
            expr();
            match(Tag.TO);
            idlist();
            match(']');
            assignlistp();
        }else{
            error("Expected [ <expr> to <idlist>]");
        }

    }

    private void assignlistp(){
        if(look.tag == '['){
            match('[');
            expr();
            match(Tag.TO);
            idlist();
            match(']');
            assignlistp();
        }

    }

    private void idlist(){
        match(Tag.ID);
        idlistp();
    }

    private void idlistp(){
        if(look.tag == Token.comma.tag) {
            match(Token.comma.tag);
            match(Tag.ID);
            idlistp();
        }
    }

    private void bexpr(){
        match(Tag.RELOP);
        expr();
        expr();

    }

    private void expr() {
        switch(look.tag){
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                 break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
        }
    }

    private void exprlist(){
        expr();
        exprlistp();
    }

    private void exprlistp(){
        if(look.tag == Word.comma.tag){
            match(Word.comma.tag);
            expr();
            exprlistp();
        }
    }



    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/Sez_3/Scratch1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser1 parser = new Parser1(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}