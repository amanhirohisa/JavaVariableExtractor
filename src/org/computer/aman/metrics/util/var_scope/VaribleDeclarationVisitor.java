package org.computer.aman.metrics.util.var_scope;

import java.util.ArrayList;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VaribleDeclarationVisitor 
extends VoidVisitorAdapter<String>
{
	public VaribleDeclarationVisitor() 
	{
		varList = new ArrayList<Variable>();
	}
	
	public ArrayList<Variable> getVariableList()
	{
		return varList;
	}
	
	@Override
	public void visit(Parameter n, String arg) 
	{
		int declareLine = n.getRange().get().begin.line;
		Node parent = n.getParentNode().get();
		int begin = parent.getRange().get().begin.line;
		int end = parent.getRange().get().end.line;
		int kind = ( parent instanceof NodeWithBlockStmt ) ? Variable.LOCAL_VAR : Variable.METHOD_PARAM;
		Variable v = new Variable(n.getName().toString(), n.getType().toString(), kind, declareLine, begin, end);
		varList.add(v);
		super.visit(n, arg);
	}

	@Override
	public void visit(VariableDeclarator n, String arg) 
	{
		int declareLine = n.getRange().get().begin.line;
		Node parent = n.getParentNode().get();
		int kind = ( parent instanceof FieldDeclaration ) ? Variable.FIELD : Variable.LOCAL_VAR;
		int begin = declareLine;
		if ( parent instanceof FieldDeclaration ) {
			parent = parent.getParentNode().get();
			begin = parent.getRange().get().begin.line;
		}
		else {
			while ( !(parent instanceof BlockStmt) && !(parent instanceof ForStmt) && !(parent instanceof ForEachStmt) ) {
				parent = parent.getParentNode().get();
			}
		}
		int end = parent.getRange().get().end.line;

		Variable v = new Variable(n.getName().getIdentifier(), n.getType().asString(), kind, declareLine, begin, end);
		varList.add(v);
		super.visit(n, arg);
	}

	private ArrayList<Variable> varList;
}
