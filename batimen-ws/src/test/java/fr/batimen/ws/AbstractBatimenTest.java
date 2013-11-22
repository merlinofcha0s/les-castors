package fr.batimen.ws;

import org.junit.Rule;

import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.junit.DatabaseRule;
import de.akquinet.jbosscc.needle.junit.NeedleRule;

public class AbstractBatimenTest {

	@Rule
	public DatabaseRule databaseRule = new DatabaseRule();

	@Rule
	public NeedleRule needleRule = new NeedleRule(databaseRule);

	protected TransactionHelper transactionHelper = databaseRule.getTransactionHelper();

}
