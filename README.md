# GitHub Repositories API

Spring Boot application that provides REST API for fetching GitHub user repositories with branch information.

## Features

- Fetches all non-fork repositories for a given GitHub user
- Returns repository name, owner login, and branch details
- Provides branch name and last commit SHA for each branch
- Handles non-existing users with proper 404 error response
- Input validation for GitHub usernames

## Technology Stack

- **Java 21**
- **Spring Boot 3.5**
- **Spring Web**
- **Lombok**
- **MockMvc** for integration testing

## API Endpoints

### Get User Repositories

```http
GET /api/github/{username}
```

**Parameters:**
- `username` (path) - GitHub username (1-39 characters, alphanumeric and hyphens only)

**Example Response (200 OK):**
```json
[
  {
    "repoName": "Restaurant_Web",
    "ownerLogin": "Gosqu248",
    "branches": [
      {
        "name": "admin",
        "lastCommitSha": "30f6c33da39334035ca233afecd3dd8012587280"
      },
      {
        "name": "login",
        "lastCommitSha": "cfd5085780f860ad9151876051b869c2f67cb81f"
      },
      {
        "name": "main",
        "lastCommitSha": "4e5c74e842150ce2c5d8707bec1e7dde9cad8186"
      }
    ]
  }
]
```

**Error Response (404 Not Found):**
```json
{
  "status": 404,
  "message": "User not found on GitHub"
}
```

## Running the Application

### Prerequisites
- Java 21 or higher
- Maven 3.6+

### Build and Run
```bash
# Clone the repository
git clone https://github.com/Gosqu248/ApiteraTask.git
cd atipera-task

# Build the project
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Example Usage
```bash
# Get repositories for user 'Gosqu248'
curl http://localhost:8080/api/github/Gosqu248

# Test with non-existing user
curl http://localhost:8080/api/github/nonexistentuser123
```

## Testing

### Run All Tests
```bash
mvn test
```

### Integration Test
The application includes a comprehensive integration test (`GithubIntegrationTest`) that verifies:
- Filtering of fork repositories
- Proper response structure
- GitHub API integration
- Error handling

