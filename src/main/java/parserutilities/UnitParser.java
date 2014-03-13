package parserutilities;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import expressiontree.FunctionNode;

public class UnitParser {

	private class UnitInfo {
		public final Pattern regex;
		public final int token;

		public UnitInfo(Pattern regex, int token) {
			this.regex = regex;
			this.token = token;
		}
	}

	private LinkedList<UnitInfo> unitInfos;
	private LinkedList<ExpressionUnit> units;
    private static UnitParser unitParser;
	
	public UnitParser() {
		unitInfos = new LinkedList<UnitInfo>();
		units = new LinkedList<ExpressionUnit>();
	}

	public static UnitParser getUnitParser(){
		if(unitParser == null)
			unitParser = createUnitParser();
		return unitParser;
	}
	
	private static UnitParser createUnitParser() {
		UnitParser unitParser = new UnitParser();

		unitParser.add("[+-]", ExpressionUnit.PLUSMINUS);
		unitParser.add("[*/]", ExpressionUnit.MULDIV);
		unitParser.add("\\^", ExpressionUnit.RAISED);
		String funcs = FunctionNode.getAllFunctions();
		unitParser.add("(" + funcs + ")(?!\\w)", ExpressionUnit.FUNCTION);
		unitParser.add("\\(", ExpressionUnit.OPEN_BRACKET);
		unitParser.add("\\)", ExpressionUnit.CLOSE_BRACKET);
		unitParser.add("(?:\\d+\\.?|\\.\\d)\\d*(?:[Ee][-+]?\\d+)?", ExpressionUnit.NUMBER);
		unitParser.add("[a-zA-Z]\\w*", ExpressionUnit.VARIABLE);

		return unitParser;

	}

	public void separate(String str) {
		String s = str.trim();
		int totalLength = s.length();
		units.clear();
		while (!s.equals("")) {
			int remaining = s.length();
			boolean match = false;
			for (UnitInfo info : unitInfos) {
				Matcher m = info.regex.matcher(s);
				if (m.find()) {
					match = true;
					String tok = m.group().trim();
					s = m.replaceFirst("").trim();
					units.add(new ExpressionUnit(info.token, tok, totalLength - remaining));
					break;
				}
			}
			if (!match)
			System.out.println("Unexpected character in input: " + s);;
		}
	}

	public void add(String regex, int token) {
		unitInfos.add(new UnitInfo(Pattern.compile("^(" + regex + ")"), token));
	}

	public LinkedList<ExpressionUnit> getUnits() {
		return units;
	}

}
