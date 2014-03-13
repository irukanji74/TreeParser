package expressiontree;

import java.util.LinkedList;

import parserutilities.NodeVisitor;

public class MulDivNode implements Node {

	private LinkedList<PartExpression> partExpr;
	
	public MulDivNode(){
		partExpr = new LinkedList<PartExpression>();
	}
	
	public MulDivNode(Node expr, boolean up){
		partExpr = new LinkedList<PartExpression>();
		partExpr.add(new PartExpression(expr, up));
	}
	
	public void add(Node expr, boolean up){
		this.partExpr.add(new PartExpression(expr, up));
	}
	
	public int getType() {
		return Node.MULDIV_NODE;
	}

	public double getValue() {
		double product = 1.0;
		for(PartExpression pE : partExpr){
		if(pE.up)
			product *= pE.expr.getValue();
		else product /= pE.expr.getValue();
			
		}		
		return product;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	    for (PartExpression pe : partExpr)
	      pe.expr.accept(visitor);
	}

}
