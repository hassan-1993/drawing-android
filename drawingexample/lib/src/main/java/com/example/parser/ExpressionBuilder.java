package com.example.parser;

import com.example.expression.Expression;
import com.example.scanner.Token;
import com.example.scanner.TokenID;

import java.util.ArrayList;
import java.util.Iterator;



public class ExpressionBuilder {
	
	public Expression build(String expression){
		Expression exp = ExpressionParser.parse(expression);
		exp = collapse(TokenID.ADDITION, exp);
		exp = collapse(TokenID.MULTIPLICATION, exp);
		exp = collapse(TokenID.COMMA, exp);
		remove(TokenID.COMMA, exp);
		return exp;
	}

	private static void remove(TokenID tokenID, Expression expression){
		Iterator<Expression> operands = expression.getOperands().iterator();
		ArrayList<Expression> childrenToAdd = new ArrayList<Expression>();
		
		//
		while(operands.hasNext()){
			Expression exp = operands.next();
			
			if(exp.getToken().getTokenID() == TokenID.COMMA){				
				operands.remove();
				
				//
				childrenToAdd.addAll(exp.getOperands());
			} else {
				remove(tokenID, exp);
			}
		}
			
		//
		for(Expression exp : childrenToAdd){
			remove(tokenID, exp);
			expression.getOperands().add(exp);
		}
	}
	
	/**
     * Collapse the levels of the tree on a specific tokens.
     *
     * Example:-
     *
     *           +                  +
     *          / \          	  / | \
     *         2   +      => 	 2  3  4 
     *            3 4
     *
     * @param tokenID
     * @param expression
     * @return
     */
	private static Expression collapse(TokenID tokenID, Expression expression){
        Expression tree = new Expression(new Token(expression.getToken().getTokenID(), expression.getToken().getSequence()));

        for(int i = 0; i < expression.getOperandSize(); i++)
            tree.addOperand(collapse(tokenID, expression.getOperand(i)));

        if (tree.getToken().getTokenID() == tokenID) {
            ArrayList<Expression> u = new ArrayList<>();

            for(int i = 0; i < tree.getOperandSize(); i++){
                Expression child = tree.getOperand(i);

                if (child.getToken().getTokenID() == tokenID) {
                    u.addAll(child.getOperands());
                }else{
                    u.add(child);
                }
            }

            Expression opr = new Expression(new Token(tokenID, tree.getToken().getSequence()));
            return construct(opr, u);
        }

        return tree;
    }
    
    public static Expression construct(Expression operator, ArrayList<Expression> operands) {
        for (Expression opr : operands)
            operator.addOperand(opr.clone());

        return operator;
    }
}
