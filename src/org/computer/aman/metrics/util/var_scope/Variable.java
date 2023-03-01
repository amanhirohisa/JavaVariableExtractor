package org.computer.aman.metrics.util.var_scope;

/**
 * A class storing a variable's data including the variable's
 * <ul>
 *  <li>name,</li> 
 *  <li>type,</li> 
 *  <li>kind (local variable, method parameter, field),</li> 
 *  <li>line number at which the variable is declared,</li>
 *  <li>line number corresponding to the beginning of the scope,</li>
 *  <li>line number corresponding to the ending of the scope.</li>
 * </ul>
 * 
 * @author Hirohisa AMAN &lt;aman@computer.org&gt;
 */
public class Variable 
{
	public static final int LOCAL_VAR = 0;
	public static final int METHOD_PARAM = 1;
	public static final int FIELD = 2;
	
	/**
	 * Create a Variable object.
	 * 
	 * @param aName variable's name
	 * @param aType variable's type name
	 * @param aKind variable's kind
	 * @param aLine line at which the variable is declared
	 * @param aBegin beginning line of scope
	 * @param aEnd ending line of scope
	 */
	public Variable(final String aName, final String aType, final int aKind, final int aLine, final int aBegin, final int aEnd)
	{
		name = aName;
		type = aType;
		kind = aKind;
		line = aLine;
		begin = aBegin;
		end = aEnd;
	}
	
	/**
	 * Returns the variable's kind as a String.
	 * 
	 * @return variable's kind: "LocalVar", "Parameter", or "Field"
	 */
	public String getKindAsString()
	{
		if ( kind == LOCAL_VAR ) {
			return "LocalVar";
		}
		if ( kind == METHOD_PARAM ) {
			return "Parameter";
		}
		return "Field";
	}
	
	/**
	 * Returns the variable's data as a String.
	 * 
	 * @return A string representation of the variable.
	 */
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
		builder.append("("); builder.append(line); builder.append(") "); 
		builder.append(name); builder.append(" : "); builder.append(type); 
		builder.append(" | "); builder.append(getKindAsString()); builder.append(" | ");
		builder.append(begin); builder.append(" -- "); builder.append(end);
		return builder.toString();
	}

	/**
	 * Returns the variable's data in the TSV (Tab-Separated Values) format.
	 * 
	 * @return TSV record presenting the variable's data as "path line kind name type begin end"
	 */
	public String toTSV()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(line); builder.append("\t");
		if ( kind == LOCAL_VAR ) {
			builder.append("L\t");
		}
		else if ( kind == METHOD_PARAM ) {
			builder.append("M\t");
		}
		else {
			builder.append("F\t");
		}
		builder.append(name); builder.append("\t");
		builder.append(type); builder.append("\t");
		builder.append(begin); builder.append("\t");
		builder.append(end);
		return builder.toString();
	}
	
	private int begin;
	private int end;
	private int kind;
	private int line;
	private String name;
	private String type;
}
