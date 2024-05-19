package emitter;
import java.io.*;
import java.util.List;

import ast.ProcedureDeclaration;

/**
 * Emitter to write to a file.
 * @author Ms. Datar
 * @author Ishan Mysore
 * @version 12/14/23
 */
public class Emitter
{
	private PrintWriter out;
	private int ifLabelNumber;
	private int whileLabelNumber;
	private ProcedureDeclaration current;
	private int excessStackHeight;

	//creates an emitter for writing to a new file with given name
	public Emitter(String outputFileName)
	{
		ifLabelNumber = 0;
		whileLabelNumber = 0;
		excessStackHeight = 0;
		current = null;
		try
		{
			out = new PrintWriter(new FileWriter(outputFileName), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	//prints one line of code to file (with non-labels indented)
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	/**
	 * Pushes the given register onto the stack by subtracting 4 from the stack pointer 
	 * and storing the register in memory.
	 * @param reg the name of the register
	 */
	public void emitPush(String reg)
	{
		excessStackHeight += 4;
		emit("subu $sp $sp 4");
		emit("sw " + reg + " ($sp) \t # push $v0");
	}

	/**
	 * Pops $v0 off the stack and stores that value into the given register. Then, it adds 4
	 * to the stack pointer.
	 * @param reg the name of the register
	 */
	public void emitPop(String reg)
	{
		emit("lw " + reg + " ($sp) \t # pop $v0");
		emit("addu $sp $sp 4");
		excessStackHeight -= 4;
	}

	/**
	 * Generates the next label number for If statements; increases by 1 every time this method
	 * is called.
	 * @return the next label id for If statements
	 */
	public int nextIfLabelID()
	{
		int copy = ifLabelNumber;
		ifLabelNumber++;
		return copy;
	}

	/**
	 * Generates the next label number for While statements; increases by 1 every time this method
	 * is called.
	 * @return the next label id for While statements
	 */
	public int nextWhileLabelID()
	{
		int copy = whileLabelNumber;
		whileLabelNumber++;
		return copy;
	}

	/**
	 * Returns the name of the procedure context, or null if there is no procedure context.
	 * @return the name of the procedure, or null if there is no procedure
	 */
	public String getProcedureName()
	{
		if (current != null)
		{
			return current.getName();
		}
		return null;
	}

	/**
	 * Sets the procedure context to the procedure passed in to a parameter.
	 * @param proc the procedure context to set
	 * @postcondition excessStackHeight is set to 0
	 */
	public void setProcedureContext(ProcedureDeclaration proc)
	{
		excessStackHeight = 0;
		current = proc;
	}

	/**
	 * Clears the procedure context by setting the current procedure to null.
	 */
	public void clearProcedureContext()
	{
		current = null;
	}

	/**
	 * Determines if a variable given by the String varName is a local variable.
	 * A variable is local if the variable name equals the procedure name (the return value),
	 * if the variable matches one of the local variables declared inside the method, or if the
	 * variable matches one of the parameters of the procedure. Otherwise, the variable is global.
	 * @param varName the name of the variable to check for locality
	 * @return true if the variable is local, false if not
	 */
	public boolean isLocalVariable(String varName)
	{
		if (current != null)
		{
			if (varName.equals(current.getName()))
			{
				return true;
			}
			List<String> localVars = current.getLocalVars();
			for (String var : localVars)
			{
				if (var.equals(varName))
				{
					return true;
				}
			}
			List<String> args = current.getParams();
			for (String arg : args)
			{
				if (arg.equals(varName))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Determines the offset of a local variable from the stack. The offset is just
	 * 4 * (number of positions from the top).
	 * @param localVarName a local variable we are determining the offset of
	 * @precondition localVarname is the name of a local variable for the procedure currently
	 * being compiled
	 * @return the offset of the local variable
	 */
	public int getOffset(String localVarName)
	{
		List<String> args = current.getParams();
		List<String> localVars = current.getLocalVars();
		int offset = 0;
		for (int i = localVars.size()-1; i >= 0; i--)
		{
			if (localVars.get(i).equals(localVarName))
			{
				return offset + excessStackHeight;
			}
			offset += 4;
		}
		if (localVarName.equals(current.getName()))
		{
			return offset + excessStackHeight;
		}
		offset += 4;
		for (int i = args.size()-1; i >= 0; i--)
		{
			if (args.get(i).equals(localVarName))
			{
				return offset + excessStackHeight;
			}
			offset += 4;
		}
		return offset + excessStackHeight;
	}

	//closes the file.  should be called after all calls to emit.
	public void close()
	{
		out.close();
	}
}