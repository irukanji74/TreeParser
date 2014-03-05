package utilities;

import java.util.HashMap;

public class MatchParser {
	private HashMap<String, Double> variables;

	public MatchParser() {
		variables = new HashMap<String, Double>();
	}

	public void setVariable(String variableName, Double variableValue) {
		variables.put(variableName, variableValue);
	}

	public Double getVariable(String variableName) {
		if (!variables.containsKey(variableName)) {
			System.err.println("Error: variable not exist " + variableName + ".");
			return 0.0;
		}
		return variables.get(variableName);
	}

	public double parse(String s) throws Exception {
		Result result = plusMinus(s);
		if (!result.rest.isEmpty()) {
			System.err.println("Error: can't full parse");
			System.err.println("rest: " + result.rest);
		}
		return result.acc;
	}

	private Result plusMinus(String s) throws Exception {
		Result current = mulDiv(s);
		double acc = current.acc;

		while (current.rest.length() > 0) {
			if (!(current.rest.charAt(0) == '+' || current.rest.charAt(0) == '-'))
				break;

			char sign = current.rest.charAt(0);
			String next = current.rest.substring(1);

			current = mulDiv(next);
			if (sign == '+') {
				acc += current.acc;
			} else {
				acc -= current.acc;
			}
		}
		return new Result(acc, current.rest);
	}
	
	private Result mulDiv(String s) throws Exception {
		Result current = parenthesis(s);
		
		double acc = current.acc;
		while (true) {
			if (current.rest.length() == 0) {
				return current;
			}
			char sign = current.rest.charAt(0);
			if ((sign != '*' && sign != '/'))
				return current;
			
			String next = current.rest.substring(1);
			Result right = parenthesis(next);
			
			if (sign == '*') {
				acc *= right.acc;
			} else {
				acc /= right.acc;
			}
			
			current = new Result(acc, right.rest);
		}
	}

	private Result parenthesis(String s) throws Exception {
		char zeroChar = s.charAt(0);
		if (zeroChar == '(') {
			Result r = plusMinus(s.substring(1));
			if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
				r.rest = r.rest.substring(1);
			} else {
				System.err.println("Error: not closed bracket");
			}
			return r;
		}
		return functionVariable(s);
	}

	private Result functionVariable(String s) throws Exception {
		String f = "";
		int i = 0;
		// ищем название функции или переменной
		// имя обязательно должно начинаться с буквы
		while (i < s.length()
				&& (Character.isLetter(s.charAt(i)) 
			    || (Character.isDigit(s.charAt(i)) && i > 0))) {
			f += s.charAt(i);
			i++;
		}
		if (!f.isEmpty()) { // если что-нибудь нашли
			if (i < s.length() && s.charAt(i) == '(') { // и следующий символ
														// скобка значит - это
														// функция
				Result r = parenthesis(s.substring(f.length()));
				return processFunction(f, r);
				// иначе - это переменная
			} //else if(){ }
			else{
				return new Result(getVariable(f), s.substring(f.length()));
			}
		}
		return num(s);
	}

	private Result num(String s) throws Exception {
		int i = 0;
		int dot_cnt = 0;
		boolean negative = false;
		// число также может начинаться с минуса
		if (s.charAt(0) == '-') {
			negative = true;
			s = s.substring(1);
		}
		// разрешаем только цифры и точку
		while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
			// но также проверям, что в числе может быть только одна точка!
			if (s.charAt(i) == '.' && ++dot_cnt > 1) {
				throw new Exception("not valid number '" + s.substring(0, i + 1) + "'");
			}
			i++;
		}
		if (i == 0) { // что-либо похожее на число мы не нашли
			throw new Exception("can't get valid number in '" + s + "'");
		}

		double dPart = Double.parseDouble(s.substring(0, i));
		if (negative)
			dPart = -dPart;
		String restPart = s.substring(i);

		return new Result(dPart, restPart);
	}

	// Тут определяем все нашие функции, которыми мы можем пользоватся в
	// формулах
	private Result processFunction(String func, Result r) {
		if (func.equals("sin")) {
			return new Result(Math.sin(Math.toRadians(r.acc)), r.rest);
		} else if (func.equals("cos")) {
			return new Result(Math.cos(Math.toRadians(r.acc)), r.rest);
		} else if (func.equals("tan")) {
			return new Result(Math.tan(Math.toRadians(r.acc)), r.rest);
		} else if (func.equals("sqrt")) {
			return new Result(Math.sqrt(r.acc), r.rest);
		}else {
			System.err.println("function '" + func + "' is not defined");
		}
		return r;
	}
}

class Result {

	public double acc;
	public String rest;

	public Result(double v, String r) {
		this.acc = v;
		this.rest = r;
	}
}

class CheckParse{
	public static void main(String[] args) {
		MatchParser mp = new MatchParser();
		mp.setVariable("x", 4.5);
		try {
			System.out.println(mp.parse("x+23*(sin(x*3)-x*0.21)"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}