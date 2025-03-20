# Documentation: `LinkLister.java`

## Overview
The `LinkLister` class provides functionality to extract hyperlinks from a given URL. It uses the `Jsoup` library to parse HTML content and retrieve links. Additionally, it includes a method to validate URLs and restrict access to private IP addresses.

---

## Class: `LinkLister`

### Purpose
The `LinkLister` class is designed to:
1. Extract all hyperlinks (`<a>` tags) from a webpage.
2. Validate URLs to prevent access to private IP addresses.

---

## Methods

### `getLinks(String url)`
#### Description
Extracts all hyperlinks from the HTML content of the given URL.

#### Parameters
- `url` (String): The URL of the webpage to parse.

#### Returns
- `List<String>`: A list of absolute URLs extracted from the webpage.

#### Exceptions
- `IOException`: Thrown if there is an error connecting to the URL or retrieving its content.

#### Logic
1. Connects to the given URL using `Jsoup.connect(url).get()`.
2. Selects all `<a>` elements using `doc.select("a")`.
3. Iterates through the selected elements and retrieves the absolute URL of each link using `link.absUrl("href")`.
4. Adds each URL to the result list.

---

### `getLinksV2(String url)`
#### Description
Extracts hyperlinks from the given URL while validating that the URL does not point to a private IP address.

#### Parameters
- `url` (String): The URL of the webpage to parse.

#### Returns
- `List<String>`: A list of absolute URLs extracted from the webpage.

#### Exceptions
- `BadRequest`: Thrown if the URL points to a private IP address or if any other error occurs during processing.

#### Logic
1. Parses the URL using `new URL(url)` to extract the host.
2. Checks if the host starts with private IP address prefixes (`172.`, `192.168`, or `10.`).
   - If the host is private, throws a `BadRequest` exception with the message "Use of Private IP".
3. If the host is valid, calls the `getLinks(url)` method to retrieve the links.
4. Catches any exceptions and wraps them in a `BadRequest` exception.

---

## Insights

### Key Features
- **HTML Parsing**: Utilizes the `Jsoup` library for efficient HTML parsing and link extraction.
- **Private IP Validation**: Implements a security check to prevent access to private IP addresses, ensuring safer usage in networked environments.

### Dependencies
- **Jsoup Library**: Required for HTML parsing and manipulation.
- **Java Networking Classes**: Uses `java.net.URL` for URL validation and host extraction.

### Exception Handling
- The `getLinksV2` method introduces custom exception handling with the `BadRequest` exception, providing more meaningful error messages.

### Potential Enhancements
- **Customizable IP Validation**: Allow users to specify additional IP ranges or domains to block.
- **Logging**: Add logging for better traceability of errors and actions.
- **Timeout Handling**: Implement timeout settings for `Jsoup.connect()` to handle slow or unresponsive URLs.

### Security Considerations
- The private IP validation ensures that the application does not inadvertently access internal or restricted network resources.
- Additional validation could be added to prevent access to malicious or blacklisted domains.

---

## Example Usage

### Extracting Links
```java
try {
    List<String> links = LinkLister.getLinks("https://example.com");
    for (String link : links) {
        System.out.println(link);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### Extracting Links with Validation
```java
try {
    List<String> links = LinkLister.getLinksV2("https://example.com");
    for (String link : links) {
        System.out.println(link);
    }
} catch (BadRequest e) {
    System.err.println("Error: " + e.getMessage());
}
```

---

## Dependencies Table

| Dependency       | Purpose                                   |
|-------------------|-------------------------------------------|
| `org.jsoup.Jsoup` | HTML parsing and manipulation            |
| `java.net.URL`    | URL validation and host extraction       |
| `java.io.IOException` | Handles connection-related errors    |

---

## File Metadata

| Key       | Value              |
|-----------|--------------------|
| File Name | `LinkLister.java`  |
| Package   | `com.scalesec.vulnado` |
