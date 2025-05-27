# 🤖 Smart Self-Healing UI Locator Framework

## 📖 Overview

This project provides an end-to-end **self-healing locator management system** for Selenium WebDriver automation. It intelligently generates, stores, retrieves, and dynamically finds web element locators with resilience against UI changes.

Core innovation lies in integrating **OpenAI GPT-4o** to generate locator JSON files from page HTML, combined with a **self-healing** mechanism that attempts to recover missing or broken locators automatically.

---

## 🔥 Features

- **Automatic Locator Generation**  
  Parses page HTML and uses GPT-4o to generate XPath, CSS, and other locators in a structured JSON format.

- **Locator Backup & Versioning**  
  Backs up existing locator JSON files before writing updates to enable auditing and rollback.

- **Parallel Locator Search**  
  Tries multiple locator strategies in parallel to quickly find a working element.

- **Self-Healing**  
  On failure to find an element, automatically regenerates locators by fetching fresh HTML and re-querying GPT, reducing test flakiness.

- **Detailed Logging & Timing**  
  Maintains logs of locator changes and operation timings for traceability and performance monitoring.

- **Singleton Service Pattern**  
  Provides a single instance of the SelfHealingLocatorService to manage locators efficiently across tests.

---

## 📁 Project Structure

### Locator Utilities (`org.example.locatorUtills`)

- `LocatorGenarator` — Calls OpenAI GPT to generate locators from HTML.
- `LocatorWriter` — Writes locators prettily into JSON files.
- `GetLocators` — Reads locators from JSON by key and page.
- `BackUpLocators` — Backs up existing JSON locator files.

### Logging (`org.example.log`)

- `LocatorLogger` — Logs locator update events with old/new snapshots.
- `TimingLogger` — Logs durations for operations like generation and lookup.

### Services (`org.example.services`)

- `ElementFinderService` — Finds elements by trying locators in parallel with timeout.
- `SelfHealingLocatorService` — Orchestrates fetching locators, backing up, regenerating via GPT, and searching elements with self-healing logic.

### Main Application (`org.example.App`)

- Entry point demonstrating usage by navigating a login page, finding username, password, and submit button elements via the self-healing service.

---

## ⚙️ How to Use

1. **Set WebDriver path** in `App.java` to your local ChromeDriver location.

2. **Run `App.main()`**:
    - Opens a browser to the sample URL.
    - Attempts to find locators from JSON files.
    - If locators missing or stale, regenerates from fresh HTML via GPT.
    - Inputs credentials and submits login.

3. **Check logs** in `src/main/resources` for timing and locator change details.

---

## 🛠️ Prerequisites

- Java 17+ (or compatible)
- Maven or Gradle (to manage dependencies)
- Selenium WebDriver (ChromeDriver for Chrome)
- OpenAI API key configured for GPT-4o usage
- Internet access for OpenAI API calls

---

## ⚡ Benefits

- Reduces maintenance overhead for locator updates.
- Increases robustness against UI changes.
- Enables faster locator retrieval through parallel searches.
- Transparent logging aids debugging and audit trails.
- Easily extensible for new locator types or AI improvements.

---

## 📚 Future Enhancements

- Integration with CI/CD pipelines.
- GUI for visual locator editing.
- Support for more browsers and mobile devices.
- Cache management and incremental updates.
- Advanced error handling and retry policies.

---

## 👨‍💻 Author

Akshai Athota

---



Feel free to ask if you want to contribute!
