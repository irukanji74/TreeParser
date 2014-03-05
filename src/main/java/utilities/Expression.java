package utilities;

import java.util.Map;

import java.util.HashMap;
import java.util.Map;


/**
* Вычислимое Выражение
*/
public abstract class Expression {
  
  /** Вычислить выражение для даных значений переменных */
  public abstract Object execute(Map<String, Object> values) throws Exception;
  
  
  /** Узел дерева — «Число» */
  static class Num extends Expression {
    private final long value;
    
    public Num(long x) {
      value = x;
    }
    
    @Override
    public Object execute(Map<String, Object> values) {
      return value;
    }
  }
  
  /** Узел дерева — «Строка» */
  static class Str extends Expression {
    private final String value;
    
    public Str(String x) {
      value = x;
    }
    
    @Override
    public Object execute(Map<String, Object> values) {
      return value;
    }
  }  

  /** Узел дерева — «Переменная» */
  static class Var extends Expression {
    private final String name;
    
    public Var(String name) {
      this.name = name;
    }
    
    @Override
    public Object execute(Map<String, Object> values) {
      return values.get(name);
    }
  }
  
  
  /** Узел дерева — «Унарный оператор» */
  static class Unary extends Expression {
    private final Expression expr;
    private final boolean not;
    
    public Unary(Expression e, String oper) {
      expr = e;
      not = "!".equals(oper);
    }
    
    @Override
    public Object execute(Map<String, Object> values) throws Exception {
      Object o = expr.execute(values);
      if(not)
        return !(Boolean)o;
      else
        return -(Long)o;
    }
  }  
  
  

  /** Узел дерева — «Бинарный оператор» */
  static class Binary extends Expression {
    private final Expression x1;
    private final Expression x2;
    private final String op;
    
    public Binary(Expression x1, Expression x2, String op) {
      this.x1 = x1;
      this.x2 = x2;
      this.op = op;
    }

    @Override
    public Object execute(Map<String, Object> values) throws Exception {
      Object o1 = x1.execute(values);
      Object o2 = x2.execute(values);
      
      Class type = commonType(o1, o2);
      
      if(type == String.class)
        return execStr(o1 != null? o1.toString() : null, o2 != null ? o2.toString() : null);
      else if(type == Long.class)
        return execNum((Long)o1, (Long)o2);
      else
        return execBool((Boolean)o1, (Boolean)o2);
    }
    
    private Class commonType(Object o1, Object o2) {
      if(o1 == null || o2 == null || o1 instanceof String || o2 instanceof String)
        return String.class;
      if(o1 instanceof Long && o2 instanceof Long)
        return Long.class;
      return Boolean.class;
    }
    
    private Object execStr(String s1, String s2) throws Exception {
      if("==".equals(op))
        return (Boolean)(s1 == null ? s2 == null : s1.equals(s2));
      if("!=".equals(op))
        return (Boolean)(s1 == null ? s2 != null : !s1.equals(s2));
      if("+".equals(op))
        return (String)(s1 == null ? s2 : s1 + (s2 == null ? "" : s2));
      throw new Exception("Illegal String operator: " + op);
    }

    private Object execBool(boolean q1, boolean q2) throws Exception {    
      if("&&".equals(op))
        return q1 && q2;
      if("||".equals(op))
        return q1 || q2;
      if("==".equals(op))
        return q1 == q2;
      if("!=".equals(op))
        return q1 != q2;
      throw new Exception("Illegal Boolean operator: " + op);
    }

    private Object execNum(long n1, long n2) throws Exception {    
      if("==".equals(op))
        return (Boolean)(n1 == n2);
      if("!=".equals(op))
        return (Boolean)(n1 != n2);
      if("<".equals(op))
        return (Boolean)(n1 < n2);
      if("<=".equals(op))
        return (Boolean)(n1 <= n2);
      if(">".equals(op))
        return (Boolean)(n1 > n2);
      if(">=".equals(op))
        return (Boolean)(n1 >= n2);
      if("+".equals(op))
        return (Long)(n1 + n2);
      if("-".equals(op))
        return (Long)(n1 - n2);
      if("*".equals(op))
        return (Long)(n1 * n2);
      if("/".equals(op))
        return (Long)(n1 / n2);
      
      throw new Exception("Illegal Long operator: " + op);
    }
  }
}



/** Компилятор выражений */
 class ExpressionBuilder {
  
  private String expression; // Строка с исходным выражением
  private int p = 0; // текущая позиция
  
  public static Expression build(String expression) {
    ExpressionBuilder builder = new ExpressionBuilder(expression);
    builder.skip(" ");
    Expression expr = builder.build(0);
    return expr;
  }
  
  private ExpressionBuilder(String expression) {
    this.expression = expression;
  }
  
  
  /** Построить узел выражения */
  Expression build(int state) {
    if(lastState(state)) {
      Expression ex = null;
      boolean isMinus = startWith("-");
      if(isMinus)
        skip("-");

      if(startWith("(")) {
        skip("(");
        ex = build(0);
        skip(")");
      }
      else 
        ex = readSingle();
      if(isMinus)
        ex = new Expression.Unary(ex, "-");
      return ex;
    }
    
    boolean unarNot = state == 2 && startWith("!");
    if(unarNot)
      skip("!");
    
    /* Строим первый операнд */
    Expression a1 = build(state+1);
    if(unarNot)
      a1 = new Expression.Unary(a1, "!");
    
    // строим последущие операнды
    String op = null; 
    while((op = readStateOperator(state)) != null) {
      Expression a2 = build(state + 1);
      a1 = new Expression.Binary(a1, a2, op);
      
    }
    return a1;
  }
  
  private static String [][] states = new String[][] {
    {"||"},
    {"&&"},
    {"!"},
    { "<=", ">=", "==", "!=", "<", ">"},
    {"+", "-"},
    {"*", "/"},
    null
  };
  
  private boolean lastState(int s) {
    return s+1 >= states.length;
  }
  
  private boolean startWith(String s) {
    return expression.startsWith(s, p);
  }
  
  private void skip(String s) {
    if(startWith(s))
      p+= s.length();
    while(p < expression.length() && expression.charAt(p) == ' ')
      p++;
  }
  
  
  private String readStateOperator(int state) {
    String[] ops = states[state];
    for(String s : ops) {
      if(startWith(s)) {
        skip(s);
        return s;
      }
    }
    return null;
  }
  
  /**
   * считываем из потока "простое" значение (имя переменной, число или строку)
   * @return
   */
  private Expression readSingle() {
    int p0 = p;
    // чиатем из потока строку
    if(startWith("'") || startWith("\"")) {
      boolean q2 = startWith("\"");
      p = expression.indexOf(q2 ? '"' : '\'', p+1);
      Expression ex = new Expression.Str(expression.substring(p0+1, p));
      skip(q2 ? "\"" : "'");
      return ex;
    }
    
    // в потоке не строка => число или переменная
    while(p < expression.length()) {
      if(!(Character.isLetterOrDigit(expression.charAt(p))))
        break;
      p++;
    }
    
    Expression ex = null;
    if(p > p0) {
      String s = expression.substring(p0, p);
      skip(" ");
      try{
        // из потока прочитали число
        long x = Long.parseLong(s);
        return new Expression.Num(x);
      }
      catch(Exception e){}
      
      if("null".equals(s))
        return new Expression.Str(null);
      
      // не строка, не число и не null — значит переменная
      return new Expression.Var(s);
      
    }
    return null;
  }
}
  
  
 /* 
  
  // для юнит-тестов
  public ExpressionBuilder(){}
  
  @Test
  public void testBuilder() throws Exception {
    String str = "qwerty";
    long n1 = 10;
    long n2 = 5;
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("str", "str");
    map.put("n1", n1);
    map.put("n2", n2);
    
    Expression e = ExpressionBuilder.build("str != 'qwerty' && n1 / n2 >= 3 * (n2 + 10 / n1 * (2+3))");
    Boolean a = (Boolean) e.execute(map);
    Boolean b = !"qwerty".equals(str) && n1 / n2 >= 3 * (n2 + 10 / n1 * (2+3));
    assertTrue(a == b);
  }
}
*/
