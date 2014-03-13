package expressiontree;

import parserutilities.NodeVisitor;

public class PowNode implements Node {

	private Node base;
	private Node exponent;
	
	public PowNode(Node base, Node exp){
		this.base = base;
		this.exponent = exp;
	}
	public int getType() {
		return Node.POW_NODE;
	}

	public double getValue() {
		return Math.pow(base.getValue(), exponent.getValue());
	}
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	    base.accept(visitor);
	    exponent.accept(visitor);
	}

}
