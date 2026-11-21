package org.example;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class ShopTest {
    static final String URL = "https://www.saucedemo.com";
    static Playwright playwright;
    static Browser browser;
    Page page;

    @BeforeAll
    static void setUpAll() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false));
    }

    @AfterAll
    static void tearDownAll() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void setUp() { page = browser.newPage(); }

    @AfterEach
    void tearDown() { page.close(); }

    void login(String pwd) {
        page.navigate(URL);
        page.fill("#user-name", "standard_user");
        page.fill("#password", pwd);
        page.click("#login-button");
    }

    @Test
    void validLogin() {
        login("secret_sauce");
        assertThat(page).hasURL(URL + "/inventory.html");
    }

    @Test
    void invalidLogin() {
        login("wrong");
        assertThat(page.locator("[data-test='error']"))
                .containsText("do not match");
    }

    @Test
    void addToCart() {
        login("secret_sauce");
        page.click("#add-to-cart-sauce-labs-backpack");
        assertThat(page.locator(".shopping_cart_badge")).hasText("1");
    }
}