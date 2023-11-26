package assignments;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FilterAssignment {

	static WebDriver driver;

	static WebDriverWait wait;

	public static void main(String[] args) {

		driver = new ChromeDriver();
		WebDriverManager.chromedriver().setup();
		driver.get("https://www.t-mobile.com/tablets");

		driver.manage().window().maximize();

		wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		jsExecutor.executeScript("arguments[0].scrollIntoView(true);",
				driver.findElement(By.xpath("//div[@class='product-filters']//tmo-bottom-sheet-header")));

		selectFilter("Brands", "Apple", "Samsung", "TCL");
		selectFilter("Brands", "TCL");
		// selectFilter("Deals", "New","Special offer");
		// selectFilter("Operating System", "Android", "iPadOS");
		selectFilter("Brands", "All");

		// driver.quit();
	}

	private static void selectFilter(String filterCategoryName, String... items) {
		if (filterCategoryName != null && !filterCategoryName.isEmpty()) {
			if (items != null && items.length > 0) {
				By catergoryItemListView = By.xpath("//legend[normalize-space()='" + filterCategoryName
						+ "']/ancestor::mat-expansion-panel-header/following-sibling::div[@role='region']");

				if (!driver.findElement(catergoryItemListView).getAttribute("style").equals("visibility: visible;")) {
					driver.findElement(By.xpath("//legend[normalize-space()='" + filterCategoryName + "']")).click();
					wait.until(
							ExpectedConditions.attributeToBe(catergoryItemListView, "style", "visibility: visible;"));
				}

				for (String item : items) {
					if (item != null & !item.isEmpty()) {
						if (item.equalsIgnoreCase("All")) {
							List<WebElement> webElementList = driver
									.findElements(By.xpath("//legend[normalize-space()='" + filterCategoryName
											+ "']/ancestor::mat-expansion-panel-header/following-sibling::div//label/span[2]"));

							for (WebElement element : webElementList) {
								System.out.println("element text" + element.getText().trim());

								if (!driver.findElement(By.xpath(
										"//legend[normalize-space()='Brands']/ancestor::mat-expansion-panel-header/following-sibling::div//span[normalize-space()='"
												+ element.getText().trim() + "']/ancestor::mat-checkbox"))
										.getAttribute("class").contains("mat-checkbox-checked")) {
									element.click();
									wait.until(ExpectedConditions
											.jsReturnsValue("return document.readyState === 'complete';"));
								}
							}

						} else {
							if (!driver.findElement(By.xpath(
									"//legend[normalize-space()='Brands']/ancestor::mat-expansion-panel-header/following-sibling::div//span[normalize-space()='"
											+ item + "']/ancestor::mat-checkbox"))
									.getAttribute("class").contains("mat-checkbox-checked")) {
								driver.findElement(By.xpath("//legend[normalize-space()='" + filterCategoryName
										+ "']/ancestor::mat-expansion-panel-header/following-sibling::div//span[normalize-space()='"
										+ item + "']")).click();
								wait.until(ExpectedConditions
										.jsReturnsValue("return document.readyState === 'complete';"));
							}
						}
					} else {
						System.out.println("Please provide item name");
					}
				}
			} else {
				System.out.println("Please provide item name");
			}
		} else {
			System.out.println("Please provide category name");
		}
	}

}
