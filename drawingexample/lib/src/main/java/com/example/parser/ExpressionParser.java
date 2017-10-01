package com.example.parser;

import com.example.expression.Expression;
import com.example.scanner.Token;
import com.example.scanner.TokenID;
import com.example.scanner.Tokenizer;

import java.util.LinkedList;



public class ExpressionParser {
	
	// ===================================================================
    // Variables
    // ===================================================================
    private final static ShuntingYard shuntingYard = new ShuntingYard();

    static
    {
        shuntingYard.addToken(TokenID.COMMA, -3, false);
        shuntingYard.addToken(TokenID.RIGHT_BRACKET, -2, false);
        shuntingYard.addToken(TokenID.LEFT_BRACKET, -1, false);

        //
        shuntingYard.addToken(TokenID.NUMBER, 0, false);
        shuntingYard.addToken(TokenID.CONSTANT, 0, false);
        shuntingYard.addToken(TokenID.VARIABLE, 0, false);

        //
        shuntingYard.addToken(TokenID.ADDITION, 1, false);
        shuntingYard.addToken(TokenID.SUBTRACTION, 1, false);
        shuntingYard.addToken(TokenID.COMMA, 1, false);
        
        //
        shuntingYard.addToken(TokenID.MULTIPLICATION, 2, false);
        shuntingYard.addToken(TokenID.DIVISION, 2, false);

        //
        shuntingYard.addToken(TokenID.POWER, 3, true);

        //
        shuntingYard.addToken(TokenID.FACTORIAL, 4, false);
        shuntingYard.addToken(TokenID.SQRT, 4, true);
        shuntingYard.addToken(TokenID.NEGATIVE, 4, true);
        shuntingYard.addToken(TokenID.POSITIVE, 4, true);

        //
        shuntingYard.addToken(TokenID.FUNCTION, 5, true);
        shuntingYard.addToken(TokenID.LOG, 5, true);
    }

    // ===================================================================
    // Methods
    // ===================================================================
    protected static Expression parse(String expression) {
    	Tokenizer tokenizer = new Tokenizer();
        LinkedList<Token> tokens = tokenizer.getTokens(expression);

        // Get the post fix of the expression.
        LinkedList<Token> postfixExpressionToken = shuntingYard.getPostfix(tokens);
        LinkedList<Expression> nodes = new LinkedList<>();

        // Initialize nodes list.
        for (int i = 0; i < postfixExpressionToken.size(); i++) {
            Token token = postfixExpressionToken.get(i);
            Expression tree = new Expression(token);
            nodes.add(tree);
        }

        // Clear the post fix tokens list as it is not needed any more.
        postfixExpressionToken.clear();

        int i = 0;
        while (nodes.size() > 1) {
            Expression tree = nodes.get(i);
            Token token = tree.getToken();

            int argsNumber;
            switch (token.getTokenID()) {

                // Cases with two arguments.
                case POWER:
                case ADDITION:
                case DIVISION:
                case COMMA:
                case SUBTRACTION:
                case MULTIPLICATION:
                    argsNumber = 2;
                    break;

                // Cases with one argument.
                case SQRT:
                case NEGATIVE:
                case POSITIVE:
                case FACTORIAL:
                case FUNCTION:
                case BRACKET:
                    argsNumber = 1;
                    break;

                default:
                    argsNumber = 0;
            }

            Expression pNode = nodes.get(i);
            i -= argsNumber;

            int j = argsNumber;

            while (j > 0) {
                Expression node = nodes.get(i);
                nodes.remove(i);
                pNode.addOperand(node);
                j--;
            }

            i++;
        }

        //
        return nodes.getFirst();
    }
}
