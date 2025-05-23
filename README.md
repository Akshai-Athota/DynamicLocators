
## Summary of Classes for Self-Healing Automation Framework

### 1. **App**

* Main test runner launching Chrome browser.
* Opens login page, interacts with elements using self-healing locators.
* Records the test session.
* Closes the browser after test execution.

### 2. **ElementFinder**

* Finds web elements **in parallel** using multiple locator strategies.
* Waits dynamically with retries for elements to appear.
* Parses locator strings and supports xpath, css, id, name, etc.
* Improves element-finding reliability and performance.

### 3. **LocatorGenarator**

* Integrates with OpenAI GPT API.
* Sends current page HTML to GPT to generate optimal locators.
* Returns locators in JSON format for clickable and interactable elements.
* Enables dynamic locator generation when locators break.

### 4. **LocatorUtils**

* Reads locator JSON files from local storage.
* Provides lists of locator strings for a given element key and page.
* Supports locator reuse for stable test runs.

### 5. **LocatorWriter**

* Saves and formats locator JSON data to files.
* Updates locator files with AI-generated locators.
* Ensures persistent storage for locator healing.

### 6. **SelfHealingLocatorService**

* Singleton managing locator finding and healing lifecycle.
* Uses `ElementFinder` to find elements from saved locators.
* If not found, triggers AI locator generation and updates files.
* Retries element finding with fresh locators.
* Prevents repeated healing attempts per page load.

