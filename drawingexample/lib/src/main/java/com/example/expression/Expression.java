package com.example.expression;


import com.example.scanner.Token;

import java.util.ArrayList;



public class Expression {
	
	// ============================================================
	// Variables
	// ============================================================
	private Token token;
	private Expression parent;
	private ArrayList<Expression> operandList;
	
    // ============================================================
    // Constructors
    // ============================================================
	public Expression(Token token){
		this.token = token;
		this.operandList = new ArrayList<Expression>();
	}
	
	// ============================================================
    // Methods
    // ============================================================
	public void addOperand(Expression operand){
		operand.parent = this;
		operandList.add(operand);
	}
	
	public void addOperand(int index, Expression operand) {
		operand.parent = this;
        operandList.add(index, operand);
    }
	
	public Expression removeOperand(int index){
        return operandList.remove(index);
	}
	
	public Expression clone(){
		Expression expression = new Expression(getToken().clone());
        clone(this, expression);
        return expression;
	}
	
	public void clone(Expression tree, Expression clone){
        for(int i = 0; i < tree.getOperandSize(); i++){
            Expression child = tree.getOperand(i);
            Expression copy = new Expression(child.getToken().clone());
            clone.addOperand(copy);
            clone(tree.getOperand(i), clone.getOperand(i));
        }
    }
	
	// ============================================================
    // GETTERS / SETTERS
    // ============================================================	
	public ArrayList<Expression> getOperands(){
	    return operandList;
	}
	
	public int getOperandSize(){
	    return operandList.size();
	}
	  
	public Expression getOperand(int i){
	     return operandList.get(i);
	}
	  
	public Token getToken(){
		return token;
	}
	
	public Expression getParent(){
		return parent;
	}
}
