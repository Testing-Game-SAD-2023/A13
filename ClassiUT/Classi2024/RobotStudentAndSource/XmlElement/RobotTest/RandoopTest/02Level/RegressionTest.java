package mypackage;
import mypackage.XmlElement;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
RegressionTest_it0_livello1_it1_livello2_it2_livello3.class,
RegressionTest_it1_livello2_it2_livello3.class,
RegressionTest_it2_livello3.class
})
public class RegressionTest{ }
