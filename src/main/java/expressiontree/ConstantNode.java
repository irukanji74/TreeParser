package expressiontree;

import parserutilities.NodeVisitor;

/**
 Class implements Node for the tree node of constant number
 */
public class ConstantNode implements Node{
	
	/** The value of the constant */
	  private double value;
	  
	  public ConstantNode(double value){
		  this.value = value;
	  }
	  
	  public ConstantNode(String strValue){
		  this.value = Double.valueOf(strValue);
	  }

	public int getType() {
		return Node.CONSTANT_NODE;
	}

	public double getValue() {
		return value;
	}

	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
		
	}

}
