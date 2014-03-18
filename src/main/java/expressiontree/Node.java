package expressiontree;

import parserutilities.AbstractNodeVisitor;


public interface Node {

	public enum NodeId{
		VAR_NODE(1), CONSTANT_NODE(2), ADDSUB_NODE(3), MULDIV_NODE(4), POW_NODE(5), FUNCTION_NODE(6);
		
		int idValue;
		
		private NodeId(int idValue){
			this.idValue = idValue;
		}
	}
	//id's for all the nodes
	  public static final int VAR_NODE = 1;
	  public static final int CONSTANT_NODE = 2;
	  public static final int ADDSUB_NODE = 3;
	  public static final int MULDIV_NODE = 4;
	  public static final int POW_NODE = 5;
	  public static final int FUNCTION_NODE = 6;
	  
	  // returns the type of the node
	  public int getType();
	  
	  // returns the value of the node - calculates the subexpression
	  // and assign the new value to the node
	  public double getValue();
	  
	  // implementing Visitor
	  public void accept(AbstractNodeVisitor visitor);
}
