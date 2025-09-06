package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasspathResource("features")
@ConfigurationParameter(key = io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME, value = "steps,hooks")
@ConfigurationParameter(key = io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME,
        value = "pretty, com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:")
public class CucumberTest {}

