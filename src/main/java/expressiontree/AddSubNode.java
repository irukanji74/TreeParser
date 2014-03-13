package expressiontree;

import java.util.LinkedList;
import parserutilities.NodeVisitor;

public class AddSubNode implements Node {

	private LinkedList<PartExpression> partExpr;
	
	public AddSubNode(){
		this.partExpr = new LinkedList<PartExpression>();
	}
	
	public AddSubNode(Node expr, boolean up){
		this.partExpr = new LinkedList<PartExpression>();
		this.partExpr.add(new PartExpression(expr, up));
	}
	
	public void add(Node expr, boolean up){
		this.partExpr.add(new PartExpression(expr, up));
	}
	public int getType() {
		return Node.ADDSUB_NODE;
	}

	public double getValue() {
		double sum = 0.0;
		for(PartExpression pE : partExpr){
			if(pE.up)
				sum += pE.expr.getValue();
			else sum -= pE.expr.getValue();
		}
		return sum;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	    for (PartExpression pe : partExpr)
	      pe.expr.accept(visitor);
	}

}
