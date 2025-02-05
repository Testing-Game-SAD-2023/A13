package mypackage;
import mypackage.ParameterParser;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
RegressionTest_it0_livello1_it2_livello2_it4_livello3_it5_livello4.class,
RegressionTest_it2_livello2_it4_livello3_it5_livello4.class,
RegressionTest_it4_livello3_it5_livello4.class,
RegressionTest_it5_livello4.class
})
public class RegressionTest{ }
