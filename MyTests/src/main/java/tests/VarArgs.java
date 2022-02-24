package tests;

public class VarArgs {
	public static void fooBarMethod(String... variables){
		System.out.println(">>");
		for(String variable : variables){
		      System.out.println(variable);
		   }
		}
	
	public static void main(String args[]) {
		fooBarMethod("foo", "bar");
		fooBarMethod("foo", "bar", "boo");
		fooBarMethod(new String[]{"foo", "var", "boo"});	   
	}
}
