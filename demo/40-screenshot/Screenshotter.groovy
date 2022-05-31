@Grapes([
    @Grab(group='com.codeborne', module='selenide', version='5.24.3'),
    @Grab(group='org.junit.jupiter', module='junit-jupiter-params', version='5.4.0'),
])

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.openqa.selenium.Dimension

import static com.codeborne.selenide.Condition.exist
import static com.codeborne.selenide.Condition.visible
import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.Selectors
import com.codeborne.selenide.WebDriverRunner

import org.openqa.selenium.chrome.ChromeOptions
import static com.codeborne.selenide.Selenide.$
import static com.codeborne.selenide.Selenide.open

import groovy.io.FileType

public class GetScreens {
    @BeforeAll
    static void setup() {
        String folderPath = "screenshots"
        new File(folderPath).eachFile (FileType.FILES) { file -> file.delete() }
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--verbose",
                "--no-sandbox",
                "--incognito",
                "--disable-dev-shm-usage",
                "--remote-debugging-port=9222");
        Configuration.reportsFolder = "screenshots"
        Configuration.headless = true;
        login()
    }

    static void login() {
        open("http://localhost:8080/");
    }

    static void setBrowserSize () {
        WebDriverRunner.getWebDriver().manage().window().
                setSize(new Dimension(1024, 576));
    }

    @ParameterizedTest
    @CsvSource([
            "BillForm, /,Draft,''",
    ])
    void checkForm(String formName, String url, String text, String dimensions) {
        setBrowserSize()
        open(url)
        $(Selectors.byText(text)).shouldBe(visible)
        $(".expand-transition-enter-active").shouldNotBe(exist)
        $(".expand-transition-enter-to").shouldNotBe(exist)
        println(Selenide.screenshot(formName));
    }
}

