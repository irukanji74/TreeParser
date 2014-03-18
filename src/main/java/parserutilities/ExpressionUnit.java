package parserutilities;

public class ExpressionUnit {

	  
	public enum Terminals{
		FINISH(0),  PLUSMINUS(1),  MULDIV(2),  RAISED(3),  FUNCTION(4),
		OPEN_BRACKET(5),  CLOSE_BRACKET(6),  NUMBER(7), VARIABLE(8);
		
		int termValue;
		
		private Terminals(int termValue){
			this.termValue = termValue;
		}
	}
	
	  public static final int FINISH = 0;
	  public static final int PLUSMINUS = 1;
	  public static final int MULDIV = 2;
	  public static final int RAISED = 3;
	  public static final int FUNCTION = 4;
	  public static final int OPEN_BRACKET = 5;
	  public static final int CLOSE_BRACKET = 6;
	  public static final int NUMBER = 7;
	  public static final int VARIABLE = 8;
	  
	  public final int unit;
	  public final String sequence;//saves String representation of a parsed unit
	  public final int position;// saves position of a unit in the parsed expression
	  
	  public ExpressionUnit(int unit, String seq, int pos){
		  this.unit = unit;
		  this.sequence = seq;
		  this.position = pos;
		  
	  }
}
