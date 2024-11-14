package Sez_Fc;
//EX 5.2 Translator con implementazione delle operazioni logiche all'interno delle condizioni booleane; ( BoolOP );
import Sez_2.*;
import Sez_5.CodeGenerator;
import Sez_5.OpCode;
import Sez_5.SymbolTable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class TranslatorFC1 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public TranslatorFC1(Lexer l, BufferedReader br) {
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

    /*/COSTRUTTO PROG(): Chiamata del costrutto statlist() che mediante l'utilizzo di
    una linked list di Instruction e di una hasmap come Symbol table tradurra
    i token letti in sequenza in jasmin.*/
    public void prog() {
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emit(OpCode.GOto,lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
        	code.toJasmin();
        }
        catch(IOException e) {
        	System.out.println("IO error\n");
        }
    }

    /*COSTRUTTO STATLIST(): Lista di comandi, formata da un costrutto stat(), che può eventualmente
    essere seguito da un altro costrutto statlistp()(facoltativo) che viene eseguito al riconoscimento di ';'
    alla fine del primo stat.
    */
    private void statlist(int lcorrent){
        stat(lcorrent);
        int lnext_prog = code.newLabel();
        statlistp(lnext_prog);

    }
    private void statlistp(int lcorrent){
        if(look.tag == Word.semicolon.tag){
            match(Word.semicolon.tag);
            code.emit(OpCode.GOto,lcorrent);
            code.emitLabel(lcorrent);
            stat(lcorrent);
            int lnext_prog = code.newLabel();
            statlistp(lnext_prog);

        }

    }


    /*COSTRUTTO STAT():
    */

    public void stat(int lcorrent) {
        switch(look.tag) {
            case Tag.READ:
                readlist();
                break;
            case Tag.PRINT:
                printlist();
                break;
            case Tag.ASSIGN:
                assignlist();
                break;
            case Tag.FOR:
                int loop = lcorrent;
                match(Tag.FOR);
                match('(');
                if(forinit() || lcorrent==0 ){
                    loop = code.newLabel();
                    code.emit(OpCode.GOto,loop);
                    code.emitLabel(loop);
                }
                int startLoop = code.newLabel();
                int endloop = code.newLabel();
                boolOP(startLoop,endloop);
                match(')');
                match(Tag.DO);
                code.emitLabel(startLoop);
                stat(startLoop);
                code.emit(OpCode.GOto,loop);
                code.emitLabel(endloop);
                break;
            case Tag.IF:
                match(Tag.IF);
                match('(');
                int iTrue = code.newLabel();
                int iFalse = code.newLabel();
                boolOP(iTrue,iFalse);
                match(')');
                code.emitLabel(iTrue);
                stat(iTrue);
                if (look.tag==Tag.ELSE){
                    int end = code.newLabel();
                    code.emit(OpCode.GOto,end);
                    match(Tag.ELSE);
                    code.emitLabel(iFalse);
                    stat(iFalse);
                    code.emit(OpCode.GOto,end);
                    code.emitLabel(end);
                }else{
                    code.emit(OpCode.GOto,iFalse);
                    code.emitLabel(iFalse);
                }
                match(Tag.END);
                break;
            case '{':
                int statgroup = lcorrent;
                if(statgroup==0){
                    statgroup = code.newLabel();
                    code.emit(OpCode.GOto,statgroup);
                    code.emitLabel(statgroup);
                }
                match('{');
                statlist(statgroup);
                match('}');
                break;
            default:
                error("Instruction Error... ");
                break;
        }
     }

     /*COSTRUTTO READLIST():
     */
    private void readlist(){
        match(Tag.READ);
        match('(');
        code.emit(OpCode.invokestatic,0);
        id();
        readlistp();
        match(')');
    }

    private void readlistp(){
        if (look.tag == Word.comma.tag) {
            match(Word.comma.tag);
            code.emit(OpCode.invokestatic,0);
            id();
            readlistp();
        }
    }


    /*COSTRUTTO PRINTLIST():
     */
    private void printlist(){
        match(Tag.PRINT);
        match('(');
        expr();
        code.emit(OpCode.invokestatic,1);
        printlistp();
        match(')');
    }

    private void printlistp(){
        if (look.tag == Word.comma.tag) {
            match(Word.comma.tag);
            expr();
            code.emit(OpCode.invokestatic,1);
            printlistp();
        }
    }


    /*COSTRUTTI ASSIGNLIST() e IDLIST():
     */
    private void assignlist(){
        match(Tag.ASSIGN);
        assign();
        assignlistp();
    }

    private void assignlistp(){
        if(look.tag == '['){
            assign();
            assignlistp();
        }
    }

    private void assign(){
        match('[');
        expr();
        match(Tag.TO);
        idlist();
        match(']');
    }

    private void idlist() {
        id();
        idlistp();
    }
    private void idlistp() {
        if(look.tag == Word.comma.tag){
            match(Word.comma.tag);
            id();
            idlistp();
        }
    }
    private void id() {
        switch(look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
                code.emit(OpCode.istore,id_addr);
                match(Tag.ID);
        }
    }

    /*COSTRUTTO FORINIT();
    */
    private boolean forinit(){
        if(look.tag==Tag.ID){
            String id = ((Word)look).lexeme;
            if(st.lookupAddress(id)==-1){
                st.insert(id,count++);
            }
            match(Tag.ID);
            match(Tag.INIT);
            expr();
            code.emit(OpCode.istore,st.lookupAddress(id));
            match(Word.semicolon.tag);
            return true;
        }return false;

    }


    /*COSTRUTTi BoolOP e Brxpr:
    */
    private void boolOP(int iTrue,int iFalse){
        switch(look.tag) {
            case '!':
                match('!');
                boolOP(iFalse,iTrue);
                break;
            case Tag.AND:
                match(Tag.AND);
                int allTrue = code.newLabel();
                boolOP(allTrue,iFalse);
                code.emitLabel(allTrue);
                boolOP(iTrue,iFalse);
                break;
            case Tag.OR:
                match(Tag.OR);
                int allFalse = code.newLabel();
                boolOP(iTrue,allFalse);
                code.emitLabel(allFalse);
                boolOP(iTrue,iFalse);
                break;
            case Tag.RELOP:
                bexpr(iTrue,iFalse);
                break;
        }
        }

    private void bexpr(int iTrue,int iFalse){
        if(look.tag!=Tag.RELOP) match(Tag.RELOP);
        String relop = ((Word)look).lexeme;
        match(Tag.RELOP);
        expr();
        expr();
        switch(relop) {
            case "<":
                code.emit(OpCode.if_icmplt, iTrue);
                break;
            case "<=":
                code.emit(OpCode.if_icmple, iTrue);
                break;
            case ">":
                code.emit(OpCode.if_icmpgt, iTrue);
                break;
            case ">=":
                code.emit(OpCode.if_icmpge, iTrue);
                break;
            case "<>":
                code.emit(OpCode.if_icmpne, iTrue);
                break;
            case "==":
                code.emit(OpCode.if_icmpeq, iTrue);
                break;
        }
        code.emit(OpCode.GOto,iFalse);
    }
    private void exprList(char sign) {
        match('(');
        expr();
        exprListp(sign);
        match(')');

    }
    private void exprListp(char sign) {
        if(look.tag == Word.comma.tag){
            match(Word.comma.tag);
            switch(sign) {
                case '+':
                    expr();
                    code.emit(OpCode.iadd);
                    exprListp(sign);
                    break;
                case '*':
                    expr();
                    code.emit(OpCode.imul);
                    exprListp(sign);
                    break;
                default:
                    error("Expected Expr()...");
                    break;
        }
        }
    }
    private void expr() {
        switch(look.tag) {
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '+':
                match('+');
                exprList('+');
                break;
            case '*':
                match('*');
                exprList('*');
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc,((NumberTok)look).number);
                match(Tag.NUM);
                break;
            case Tag.ID:
                if(st.lookupAddress(((Word)look).lexeme) == -1) error("Variable ".concat(((Word)look).lexeme.concat(" Not inizialized")));
                code.emit(OpCode.iload, st.lookupAddress(((Word)look).lexeme));
                match(Tag.ID);
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/Sez_5/Scratch1.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            TranslatorFC1 Translator = new TranslatorFC1(lex, br);
            Translator.prog();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
