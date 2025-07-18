package com.example.chess;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/com/example/chess",
        plugin = {"pretty", "json:target/cucumber-reports/cucumber.json"})
public class RunCucumberTest {
}
