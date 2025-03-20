# Documentation: `LinksController.java`

## Overview
The `LinksController` class is a REST controller implemented using the Spring Boot framework. It provides two endpoints (`/links` and `/links-v2`) that allow users to retrieve a list of links from a given URL. The class leverages the `LinkLister` utility to perform the actual link extraction.

## Class Details

### Annotations
- **`@RestController`**: Indicates that this class is a REST controller, where each method returns a domain object instead of a view.
- **`@EnableAutoConfiguration`**: Enables Spring Boot's auto-configuration mechanism.

### Endpoints
The class defines two endpoints, both of which accept a `url` parameter and return a list of links in JSON format.

| Endpoint       | HTTP Method | Parameters | Produces           | Description                                                                 |
|----------------|-------------|------------|--------------------|-----------------------------------------------------------------------------|
| `/links`       | GET         | `url` (String) | `application/json` | Extracts links from the provided URL using the `LinkLister.getLinks` method. |
| `/links-v2`    | GET         | `url` (String) | `application/json` | Extracts links from the provided URL using the `LinkLister.getLinksV2` method. |

### Methods

#### `links`
- **Signature**: `List<String> links(@RequestParam String url) throws IOException`
- **Description**: This method handles requests to the `/links` endpoint. It uses the `LinkLister.getLinks` method to extract links from the provided URL.
- **Parameters**:
  - `url`: The URL from which links are to be extracted.
- **Returns**: A list of links (`List<String>`).
- **Throws**: 
  - `IOException`: If an I/O error occurs during link extraction.

#### `linksV2`
- **Signature**: `List<String> linksV2(@RequestParam String url) throws BadRequest`
- **Description**: This method handles requests to the `/links-v2` endpoint. It uses the `LinkLister.getLinksV2` method to extract links from the provided URL.
- **Parameters**:
  - `url`: The URL from which links are to be extracted.
- **Returns**: A list of links (`List<String>`).
- **Throws**: 
  - `BadRequest`: If the request is invalid or the URL cannot be processed.

## Dependencies
The class depends on the following:
- **Spring Boot**: For REST controller functionality and auto-configuration.
- **`LinkLister`**: A utility class (not provided in the code snippet) that performs the actual link extraction.

## Insights
- **Error Handling**: The `links` method throws `IOException`, while the `linksV2` method throws a custom `BadRequest` exception. This suggests that `linksV2` may have additional validation or error handling compared to `links`.
- **Versioning**: The presence of `/links-v2` indicates an effort to version the API, which is a good practice for maintaining backward compatibility.
- **Scalability**: The use of `List<String>` as the return type implies that the number of links returned is limited by memory. For large-scale applications, consider using a paginated response or streaming approach.
- **Security**: The code does not include any explicit validation or sanitization of the `url` parameter. This could lead to potential security vulnerabilities, such as SSRF (Server-Side Request Forgery). Proper validation and sanitization should be implemented.

## File Metadata
- **File Name**: `LinksController.java`
