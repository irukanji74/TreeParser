package expressiontree;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private PartExpression pExpr;

	public ParserException(String message) {
		super(message);
	}

	public ParserException(String message, PartExpression expr) {
		super(message);
		this.pExpr = expr;
	}
	
	public PartExpression getExp(){
		return pExpr;
	}
}
