package expressiontree;

import parserutilities.NodeVisitor;

public class FunctionNode implements Node {

	  public static final int SIN = 1;
	  public static final int COS = 2;
	  public static final int TAN = 3;
	  public static final int SQRT = 4;

	  private int functionId;
	  private Node argument;
	  
	  public FunctionNode(int functionId, Node argument){
		  this.functionId = functionId;
		  this.argument = argument;
	  }
	  
	  
	public int getType() {
		return Node.FUNCTION_NODE;
	}

	public double getValue() {
		
		switch(functionId){
		  case SIN:  return Math.sin(argument.getValue());
	      case COS:  return Math.cos(argument.getValue());
	      case TAN:  return Math.tan(argument.getValue());
	      case SQRT: return Math.sqrt(argument.getValue());
		}
		// to do exception throwing
		return 0;
	}
	
	public static int stringToFunction(String str) {
	    if (str.equals("sin")) return FunctionNode.SIN;
	    if (str.equals("cos")) return FunctionNode.COS;
	    if (str.equals("tan")) return FunctionNode.TAN;
	    if (str.equals("sqrt")) return FunctionNode.SQRT;

	    return 0;// todo exception throwing
	  }

	public static String getAllFunctions() {
	    return "sin|cos|tan|sqrt";
	  }


	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	    argument.accept(visitor);
		
	}
}
