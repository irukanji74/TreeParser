package expressiontree;

import java.util.EnumSet;

import parserutilities.AbstractNodeVisitor;

public class FunctionNode implements Node {

	public enum Function{
		SIN(1,"sin"), COS(2,"cos"), TAN(3,"tan"), SQRT(4,"sqrt");
		
		int value;
		String name;
		
		private Function(int value, String name){
			this.value = value;
			this.name = name;
		}
		
		public int getValue(){
			return this.value;
		}
		
		public String getName(){
			return this.name;
		}
	}
	
	
	 /* public static final int SIN = 1;
	  public static final int COS = 2;
	  public static final int TAN = 3;
	  public static final int SQRT = 4;
     */
	
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
		Function[] fArray = Function.values();
		  for(Function fval: fArray){
			  if(fval.getValue() == functionId){
				  return funcResult(fval.getName());
			  }
		  }
		  /*switch(functionId){
		  case SIN:  return Math.sin(argument.getValue());
	      case COS:  return Math.cos(argument.getValue());
	      case TAN:  return Math.tan(argument.getValue());
	      case SQRT: return Math.sqrt(argument.getValue());
		  }*/
		throw new ParserException("Invalid function id ");
     }
	
	
	private double funcResult(String name) {

		if(name.equals("sin")) return Math.sin(argument.getValue());
		if(name.equals("cos")) return Math.cos(argument.getValue());
		if(name.equals("tan")) return Math.tan(argument.getValue());
		if(name.equals("sqrt")) return Math.sqrt(argument.getValue());
		
		   throw new ParserException("Invalid function name ");
	}
	
	
	public static int stringToFunction(String str) {
		EnumSet<Function> enumSet = EnumSet.allOf(Function.class);
		for(Function fenum : enumSet){
			if(str.equals(fenum.getName())){
				return fenum.getValue();
			}
		}
	   /* if (str.equals("sin")) return Function.SIN.getValue();
	    if (str.equals("cos")) return Function.COS.getValue();
	    if (str.equals("tan")) return Function.TAN.getValue();
	    if (str.equals("sqrt"))return  Function.SQRT.getValue();
	    */
            throw new ParserException("Unexpected Function " + str + " found");
	  }

	public static String getAllFunctions() {
	    return "sin|cos|tan|sqrt";
	  }


	public void accept(AbstractNodeVisitor  visitor) {
		visitor.visit(this);
	    argument.accept(visitor);
		
	}
}
