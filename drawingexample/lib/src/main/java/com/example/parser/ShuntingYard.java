package com.example.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import com.example.scanner.Token;
import com.example.scanner.TokenID;

public class ShuntingYard {

	/**
     * Infix, Postfix and Prefix notations are three different but equivalent ways
     * of writing expressions. The shunting yard algorithm is an algorithm used to
     * convert between the three.
     * <p>
     * This class implements the shunting yard algorithm using the tokens scanned from
     * a string by the tokenizer class. Each type of a token must be given a priority
     * on which the other forms of the Infix, Postfix and Prefix notation are constructed.
     * <p>
     * The left bracket must always have a priority of -1,
     * The right bracket must always have a priority of -2.
     * The comma must always have a priority of -3.
     * <p>
     * Each token with a priority of 0 will be added directly to the output,
     * Priorities bigger than zero will depend on their priority.
     * <p>
     * Each token must be evaluated either from left or right. They are stored in the
     * association table.
     * Ex:- 2^3^4 -> 2 3 4 ^ ^ : Right association
     * Ex:- 2+3+4 -> 2 3 + 4 + : Left association
     */

	// ============================================================
	// Variables
	// ============================================================
    private HashMap<TokenID, Integer> priorityTable;        // This hash Table maps each object to its priority.
    private HashMap<TokenID, Boolean> associationTable;     // This hash Table maps each object to its association either Left(false) or right(true).

    // ============================================================
    // Constructors
    // ============================================================
    public ShuntingYard(){
        priorityTable = new HashMap<>();
        associationTable = new HashMap<>();
    }
    
    // ============================================================
    // Methods
    // ============================================================
    public void addToken(TokenID tokenID, int priority, boolean rightAssociation) {
        priorityTable.put(tokenID, priority);
        associationTable.put(tokenID, rightAssociation);
    }

    public LinkedList<Token> getPostfix(LinkedList<Token> tokens) {
        LinkedList<Token> postfixTokens = new LinkedList<>();

        Stack<Token> oprStack = new Stack<>();
//        Stack<Integer> argsStack = new Stack<>();
//        int argsCounter = 1;

        // Loop through all the tokens.
        for (int i = 0; i < tokens.size(); i++) {
            Token cToken = tokens.get(i);
            int priority = priorityTable.get(cToken.getTokenID());

            // Check if the priority is 0.
            // If the priority is 0 add it directly to the output (postfix tokens).
            if (priority == 0) {
                postfixTokens.add(cToken);
            }

            // Check if the token is a left parenthesis.
            else if (priority == -1) {
                oprStack.add(cToken);

//                if(argsCounter == 0){
//                    argsStack.push(1);
//                }else if (argsCounter > 0) {
//                    argsStack.push(argsCounter);
//                    argsCounter = 1;
//                }
            }

            // Check if the token is a right parenthesis.
            else if (priority == -2) {
                Token opr = oprStack.pop();
                int pr = priorityTable.get(opr.getTokenID());
                while (pr != -1) {
                    postfixTokens.add(opr);
                    opr = oprStack.pop();
                    pr = priorityTable.get(opr.getTokenID());
                }

                // Add the number of arguments for this bracket.
//                postfixTokens.add(new Token(TokenID.NUMBER, String.valueOf(argsCounter)));
//                argsCounter = (argsStack.size() > 0) ? argsStack.pop() : 1;

                postfixTokens.add(new Token(TokenID.BRACKET, "()"));
            }

            // Check if the token is an operator or any others.
            else if (priority > 0) {

                // While the operator stack is not empty and the precedence of the operator in the
                // operator stack is higher than the precedence of the tokenID operator, pop the
                // operator of the stack and use it to evaluate the number in the output stack.
                boolean isAssociatedRight = associationTable.get(cToken.getTokenID());

                while (oprStack.size() > 0 && priorityTable.get(oprStack.peek().getTokenID()) > 0 &&
                        ((isAssociatedRight && priorityTable.get(oprStack.peek().getTokenID()) > priority) ||
                                (!isAssociatedRight && priorityTable.get(oprStack.peek().getTokenID()) >= priority))
                        ) {
                    postfixTokens.add(oprStack.pop());
                }

                oprStack.add(cToken);
            }
        }

        // Use all the operators left in the operator stack.
        while (oprStack.size() > 0) {
            postfixTokens.add(oprStack.pop());
        }

        return postfixTokens;
    }
}
