/**
 * 
 */
package com.amazonplp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This class is used to fetch product names and price from search results in sort order on amazon.in 
 * and where ever price is not present, it will be considered as zero.
 * @author Anshul
 *
 */
public class AmazonScript {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
        driver.get("https://www.amazon.in");
       
		
		WebElement SearchBox = driver.findElement(By.id("twotabsearchtextbox"));
		SearchBox.sendKeys("Lg Soundbar");
		SearchBox.sendKeys(Keys.ENTER);

        // Find all product elements on the search results page
        List<WebElement> productContainers = driver.findElements(By.xpath("//div[@data-component-type='s-search-result']"));

        // Map to store product names and their prices
        Map<String, Integer> productPriceMap = new HashMap<>();

		for (WebElement productContainer : productContainers) {
			try {
				// Find the product name
				WebElement productNameElement = productContainer
						.findElement(By.xpath(".//span[@class='a-size-medium a-color-base a-text-normal']"));
				String productName = productNameElement.getText();

				// Find the price container and determine the price
				List<WebElement> priceContainerList = productContainer
						.findElements(By.xpath(".//span[contains(@class, 'a-price')]"));
				if (productName.toLowerCase().contains("lg")) {
					int price = 0; // Default price
					if (priceContainerList.size() > 0) {
						WebElement wholePriceElement = priceContainerList.get(0)
								.findElement(By.xpath(".//span[@class='a-price-whole']"));
						if (wholePriceElement != null && !wholePriceElement.getText().isEmpty()) {
							price = Integer.parseInt(wholePriceElement.getText().replace(",", ""));
						}
					} else {
						price = 0;
					}

					// Add the product to the map
					productPriceMap.put(productName, price);
				}
			} catch (Exception e) {
				// Handle any exceptions that occur while processing the product
				System.out.println("Error processing product: " + e.getMessage());
			}
		}

        // Print the product names and their prices
        for (Entry<String, Integer> entry : productPriceMap.entrySet()) {
            System.out.println("Price:" + entry.getValue() + ", Product: " + entry.getKey());
        }

        // Close the browser
        driver.quit();
    

	}

}
