package com.example.scanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Tokenizer {

	// ============================================================
	// Variables
	// ============================================================
	private LinkedHashMap<Token, Character> simpleForm;
    private LinkedHashMap<Character, Token> reverseSimpleForm;
    private List<Token> tokensInfo = new ArrayList<>();
    
	// ============================================================
	// Method
	// ============================================================
    private void initializeTokensInfo() {
        addToken(TokenID.FUNCTION, "asinh acosh atanh asech acoth acsch acos asin atan asec acot acsc sinh cosh tanh sech coth csch cos sin tan csc cot sec ln stepTo stepFrom log");

        addToken(TokenID.FACTORIAL, "!");
        addToken(TokenID.COMMA, ",");
        addToken(TokenID.SQRT, "√");
        addToken(TokenID.LEFT_BRACKET, "( [ <");
        addToken(TokenID.RIGHT_BRACKET, ") ] >");
        addToken(TokenID.CONSTANT, "A B C D E F G H I J K L M N O P Q R S T U V W X Y Z a b c d e f g h i j k l m n o p q r s t u v w x y z π");
        addToken(TokenID.FUNCTION, "sin cos tan log");
        
        addToken(TokenID.ADDITION, "+");
        addToken(TokenID.SUBTRACTION, "-");
        addToken(TokenID.MULTIPLICATION, "*");
        addToken(TokenID.DIVISION, "/");
        addToken(TokenID.POWER, "^");

        simpleForm = new LinkedHashMap<>();
        reverseSimpleForm = new LinkedHashMap<>();

        // split tokens with space.
        for (int i = 0; i < tokensInfo.size(); i++) {
            Token c = tokensInfo.get(i);
            if (c.getSequence().contains(" ") && !c.getSequence().equals(" ")) {
                tokensInfo.remove(i);
                String[] splits = c.getSequence().split(" ");
                for (int e = 0; e < splits.length; e++) {
                    tokensInfo.add(e + i, new Token(c.getTokenID(), splits[e]));
                }
                i = splits.length + i - 1;
            }
        }


        Collections.sort(tokensInfo, new Comparator<Token>() {
            @Override
            public int compare(Token token, Token t1) {
                /*sort by length ,if same length than sort by characters*/
                if (token.getSequence().length() < t1.getSequence().length()) {
                    return 1;
                } else if (token.getSequence().length() > t1.getSequence().length()) {
                    return -1;
                }
                return token.getSequence().compareTo(t1.getSequence());
            }
        });


        List<Character> temp = new ArrayList<>();
        // generating a character to use for each token
        for (Token token : tokensInfo) {
            if (token.getSequence().length() == 1) {
                simpleForm.put(token, token.getSequence().charAt(0));
                reverseSimpleForm.put(token.getSequence().charAt(0),token);

                temp.add(token.getSequence().charAt(0));
            }
        }
        int currentID = 0;
        for (Token token : tokensInfo) {
            if (token.getSequence().length() != 1) {
                while (temp.contains((char) currentID))
                    currentID++;

                //
                simpleForm.put(token, (char) currentID);
                reverseSimpleForm.put((char) currentID,token);
                temp.add((char) currentID);
            }
        }
        
    }
    
    public LinkedList<Token> getTokens(String expression){
        if(tokensInfo.size()==0)
            initializeTokensInfo();

        LinkedList<Token> tokens=new LinkedList<>();
        String letters = "";
        int length = expression.length();

        //
        for (int i = 0; i < length; i++) {
            char c = expression.charAt(i);
            
            //case 5
            if (c == '$') {
                tokens.add(new Token(TokenID.VARIABLE, String.valueOf(expression.charAt(i + 1))));
                i++;
            } else
            if ((!Character.isDigit(c) || c==' ')) { //case 2
                letters += c;
                if (i == expression.length() - 1) {
                    tokens.add(new Token(null, letters));
                    break;
                }
                for (int j = i + 1; j < length; j++) {
                    c = expression.charAt(j);
                    if ((!Character.isDigit(c) || c==' ') && c!='$') { //if still letter
                        letters += c;

                        if (j == expression.length() - 1) {
                            tokens.add(new Token(null, letters));
                            letters = "";
                            i = j;
                        }
                    } else {

                        tokens.add(new Token(null, letters));
                        letters = "";
                        i = j - 1;


                        break;
                    }
                }
            } else if (Character.isDigit(c)) {//case 4  (constant
                letters += c;
                if (i == expression.length() - 1) {
                    tokens.add(new Token(TokenID.NUMBER, letters));
                    break;
                }

                for (int j = i + 1; j < length; j++) {
                    c = expression.charAt(j);
                    if (Character.isDigit(c) || c == '.' || c == 'E') { //if still constant
                        letters += c;

                        if(c=='E' && j<expression.length()-1){
                            if(expression.charAt(j+1)=='-'){
                                letters+='-';
                                j++;
                            }
                        }

                        if (j == expression.length() - 1) {
                            tokens.add(new Token(TokenID.NUMBER, letters));
                            letters = "";
                            i = j;
                        }
                    } else {
                        tokens.add(new Token(TokenID.NUMBER, letters));
                        letters = "";
                        i = j - 1;
                        break;
                    }
                }
            }
        }
       
        //
        split(tokens);

        //
        return tokens;
    }
    
    //If we have something like acos asech in one of the tokens than split it
    public void split(List<Token> tokens) {

        //triq must be less than 126 in length
        //note that maximum number in triq array can be only 255
        //replace with char rather than

        //1 11 11 22 22 23 22
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);

            if (token.getTokenID() == null) {
                String result = token.getSequence();  //acos asin atanh
                for (int d = 0; d < tokensInfo.size(); d++) {
                    result = result.replace(tokensInfo.get(d).getSequence(), simpleForm.get(tokensInfo.get(d)) + "~");
                }
                
                String[] splits = result.split("~");//the split is fast since only one char (no regular experssion will be used unless bigger than 1 char)
                int lastindex = 0;
                for (int a = 0; a < splits.length; a++) {
                    char fo = splits[a].charAt(0);
                    if(fo>tokens.size()){
                        // means it points to only one character which is itself
                        tokens.add(i + a, reverseSimpleForm.get(fo).clone());
                    }else{
                        tokens.add(i + a, reverseSimpleForm.get(fo).clone());
                    }

                    lastindex = i + a + 1;

                }

                tokens.remove(lastindex);
                i = lastindex;
            }
        }
    }
    
    private void addToken(TokenID tokenID, String sequence) {
        addToken(new Token(tokenID, sequence));
    }
    
    private void addToken(Token token){
    	tokensInfo.add(token);
    }
}
