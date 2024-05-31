package com.solvd.listeners;

import com.solvd.WebTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TakeScreenshotOnTestFailureListener implements ITestListener {
    private static final Logger LOGGER = LogManager.getLogger(TakeScreenshotOnTestFailureListener.class.getName());
    private static final String SCREENSHOT_LOCATION = "target/";

    @Override
    public void onTestFailure(ITestResult result) {
        WebTest testInstance = (WebTest) result.getInstance();
        File tmpFile = ((TakesScreenshot) testInstance.getDriver()).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(SCREENSHOT_LOCATION + tmpFile.getName());
        // TODO: find a better way of handling exception - i.e. add LOGGER and log it
        try {
            Files.copy(tmpFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.warn("Unable to save screenshot to " + destinationFile.getAbsolutePath());
        }
        if (destinationFile.exists()) {
            LOGGER.info("Screenshot saved to " + destinationFile.getAbsolutePath());
        }
    }
}
