# Documentation: `LinkLister.java`

## Overview
The `LinkLister` class provides functionality to extract hyperlinks from a given URL. It uses the `Jsoup` library for HTML parsing and includes methods to retrieve links with additional validation for private IP addresses.

---

## Class: `LinkLister`

### Purpose
The class is designed to fetch and process hyperlinks (`<a>` tags) from a web page. It includes two methods:
1. `getLinks`: Extracts all hyperlinks from a given URL.
2. `getLinksV2`: Adds validation to ensure the URL does not point to a private IP address before extracting links.

---

## Methods

### `getLinks(String url)`
#### Description
Fetches all hyperlinks (`<a>` tags) from the HTML content of the provided URL.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the web page to parse. |

#### Returns
| Type          | Description                          |
|---------------|--------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the web page. |

#### Exceptions
| Type          | Description                          |
|---------------|--------------------------------------|
| `IOException` | Thrown if there is an issue connecting to the URL or fetching its content. |

#### Logic
1. Connects to the provided URL using `Jsoup.connect(url).get()`.
2. Selects all `<a>` elements using `doc.select("a")`.
3. Extracts the absolute URL of each hyperlink using `link.absUrl("href")`.
4. Returns the list of extracted URLs.

---

### `getLinksV2(String url)`
#### Description
Fetches hyperlinks from the provided URL after validating that the URL does not point to a private IP address.

#### Parameters
| Name | Type   | Description                     |
|------|--------|---------------------------------|
| `url` | `String` | The URL of the web page to parse. |

#### Returns
| Type          | Description                          |
|---------------|--------------------------------------|
| `List<String>` | A list of absolute URLs extracted from the web page. |

#### Exceptions
| Type          | Description                          |
|---------------|--------------------------------------|
| `BadRequest`  | Thrown if the URL points to a private IP address or if any other error occurs during processing. |

#### Logic
1. Parses the URL using `new URL(url)` to extract the host.
2. Checks if the host starts with private IP address prefixes (`172.`, `192.168`, or `10.`).
   - If true, throws a `BadRequest` exception with the message "Use of Private IP".
3. If the host is valid, calls the `getLinks` method to fetch hyperlinks.
4. Catches any exceptions and wraps them in a `BadRequest` exception.

---

## Insights

### Key Features
- **HTML Parsing**: Utilizes the `Jsoup` library to parse HTML and extract `<a>` tags.
- **Private IP Validation**: Ensures URLs pointing to private IP addresses are rejected for security reasons.
- **Error Handling**: Implements robust exception handling to manage invalid URLs or connection issues.

### Dependencies
- **Jsoup Library**: Required for HTML parsing and manipulation.
- **Java Networking**: Uses `java.net.URL` for URL validation and host extraction.

### Security Considerations
- The `getLinksV2` method prevents access to private IP addresses, mitigating potential security risks such as SSRF (Server-Side Request Forgery).
- The `BadRequest` exception is used to handle invalid inputs gracefully.

### Potential Enhancements
- **Customizable IP Validation**: Allow users to specify additional IP ranges or domains to block.
- **Timeout Handling**: Add timeout settings for `Jsoup.connect()` to prevent long delays during network issues.
- **Logging**: Implement detailed logging for debugging and monitoring purposes.

---

## File Metadata
| Key         | Value              |
|-------------|--------------------|
| **File Name** | `LinkLister.java` |
