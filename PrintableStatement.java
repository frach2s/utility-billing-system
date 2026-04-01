public interface PrintableStatement {
	 String printStatementTitle();
	 String getStatementBody();
	 String getStatementFooter();
	
	void printStatement();
	void printStatement(boolean shortMode); // choice to ng custoer if gusto nla yung short version ng statement nla

}