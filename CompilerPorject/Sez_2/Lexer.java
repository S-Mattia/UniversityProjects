
/*EX 2.2/2.3 Lexer per la conversione in token partendo da porzioni di codice di un semplice linguaggio di programmazione;
Con implementazione d'identificatori contenenti il simbolo terminale '_' e commenti mono/multi riga; */

package Sez_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    public boolean comment = false;
    public boolean commentOnLine = false;
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        //Verifico se il carattere iniziale è uno / che possa dare inizio a un commento
        if (peek == '/') {
            readch(br);
            if (peek == '*') {
                comment = true;
            } else if (peek == '/') {
                commentOnLine = true;
            } else {
                return Token.div;
            }
        }

        //Continuo a scartare tutti i caratteri contenuti nel commento o i caratteri fuori dal commento ma non importanti
        while(comment | commentOnLine | peek == ' ' | peek == '\t' | peek == '\n'  | peek == '\r' ) {
            switch(peek){
                case '\n':
                    commentOnLine = false;
                    line++;
                    readch(br);
                    break;
                case '*':
                    readch(br);
                    if(peek=='/'){
                        comment = false;
                        readch(br);
                    }
                    break;
                case (char)-1:
                    if(comment) {
                        System.err.println("Erroneus comment declaration: " + peek);
                        return null;
                    }else{
                        commentOnLine = false;
                    }
                    break;
                default:
                    readch(br);
                    break;
            }

            //verifico, se ho precedentemente chiuso il commento che non ce ne sia un altro contiguo al precedente;(solo quando non sono in un commento)
            if (peek == '/' & !comment & !commentOnLine){
                readch(br);
                if (peek == '*') {
                    comment = true;
                } else if (peek == '/') {
                    commentOnLine = true;
                } else {
                    return Token.div;
                }
            }
        }

        switch (peek) {
            
            // ... gestire i casi di ! ( ) [ ] { } + - * / ; , ... //
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case ',':
                peek = ' ';
                return Token.comma;
            case ';':
                peek = ' ';
                return Token.semicolon;

            // ... gestisco i casi di && || < > <= >= == <> ... //

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if(peek == '>'){
                    peek = ' ';
                    return Word.ne;
                }else{
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after & '=' "  + peek );
                    return null;
                }
            case ':':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.init;
                } else {
                    System.err.println("Erroneous character"
                            + " after ':' : "  + peek );
                    return null;
                }
          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                // ... gestire il caso degli identificatori e delle parole chiave //
                if (Character.isLetter(peek) || peek=='_') {
                    boolean onlyunderscore = (peek=='_');
                    String keyid = String.valueOf(peek);
                    readch(br);
                    while( peek=='_' || Character.isLetter(peek) || Character.isDigit(peek)){
                        onlyunderscore &= peek == '_';
                        keyid+=(String.valueOf(peek));
                        readch(br);
                    }
                    if(onlyunderscore){System.err.println("Erroneous declaration: id must also consist of at least one letter or number;");return null;}

                    switch(keyid.toUpperCase()){
                        case "ASSIGN":
                            return Word.assign;
                        case "TO":
                            return Word.to;
                        case "IF":
                            return Word.iftok;
                        case "ELSE":
                            return Word.elsetok;
                        case "DO":
                            return Word.dotok;
                        case "FOR":
                            return Word.fortok;
                        case "BEGIN":
                            return Word.begin;
                        case "END":
                            return Word.end;
                        case "PRINT":
                            return Word.print;
                        case "READ":
                            return Word.read;
                        default:
                            return new Word(Tag.ID,keyid);
                    }

                // ... gestire il caso dei numeri ... //
                }else if (Character.isDigit(peek)) {
                    String number= String.valueOf(peek);
                    readch(br);
                    while(Character.isDigit(peek)){
                        number+=(String.valueOf(peek));
                        readch(br);
                    }return new NumberTok(number);

                } // ... gestire tutti gli eventuali errori ... //
                else {
                        System.err.println("Erroneous character: "
                                + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "src/Sez_2/Scratch.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
