# NLP Engine Documentation

## Overview

The NLP (Natural Language Processing) Engine is a sophisticated component of Jarvis Core that enables natural language understanding for test orchestration commands. It provides intent recognition, entity extraction, sentiment analysis, and command validation capabilities.

## Features

### 1. Intent Recognition
- **Pattern-based Classification**: Uses regex patterns to classify user intents
- **Confidence Scoring**: Provides confidence scores for intent classification
- **Multiple Intent Types**: Supports various test-related intents

### 2. Entity Extraction
- **Service Names**: Extracts microservice names (user-service, product-service, etc.)
- **Test Types**: Identifies test types (unit, integration, api, performance, etc.)
- **Parameters**: Extracts execution parameters (timeout, retries, parallel)
- **Time Constraints**: Recognizes urgency levels (now, later, scheduled)
- **Priority Levels**: Identifies priority (high, medium, low, normal)

### 3. Sentiment Analysis
- **Positive/Negative Detection**: Analyzes user sentiment
- **Confidence Scoring**: Provides sentiment confidence levels
- **Pattern Matching**: Uses predefined patterns for sentiment detection

### 4. Complexity Analysis
- **Complexity Scoring**: Calculates command complexity
- **Indicator Detection**: Identifies complexity indicators
- **Level Classification**: Categorizes as LOW, MEDIUM, or HIGH

### 5. Command Validation
- **Input Validation**: Validates command structure and content
- **Suggestion Generation**: Provides improvement suggestions
- **Warning Detection**: Identifies potential issues

## Supported Intent Types

| Intent Type | Description | Example Commands |
|-------------|-------------|------------------|
| `RUN_TESTS` | Execute test suites | "run tests for user-service" |
| `ANALYZE_FAILURES` | Analyze test failures | "analyze failures in product-service" |
| `GENERATE_TESTS` | Generate new test cases | "generate new tests for order-service" |
| `OPTIMIZE_TESTS` | Optimize test suites | "optimize test suite for notification-service" |
| `HEALTH_CHECK` | Check system health | "check health status of gateway-service" |
| `UNKNOWN` | Unrecognized intent | "random text" |

## API Endpoints

### 1. Parse Intent
```http
POST /api/nlp/parse-intent
Content-Type: application/json

{
  "input": "run unit tests for user-service with 30 seconds timeout"
}
```

**Response:**
```json
{
  "intent": {
    "rawInput": "run unit tests for user-service with 30 seconds timeout",
    "type": "RUN_TESTS",
    "description": "run tests for [user-service] of type [UNIT_TEST]",
    "parameters": {
      "services": ["user-service"],
      "testTypes": ["UNIT_TEST"],
      "parameters": {
        "timeout": "30 seconds"
      }
    },
    "confidence": 1.0,
    "timestamp": "2024-01-15T14:47:54"
  },
  "success": true
}
```

### 2. Analyze Sentiment
```http
POST /api/nlp/analyze-sentiment
Content-Type: application/json

{
  "input": "urgently run critical tests for user-service"
}
```

**Response:**
```json
{
  "sentiment": "NEGATIVE",
  "confidence": 0.75,
  "positiveScore": 0,
  "negativeScore": 3,
  "success": true
}
```

### 3. Analyze Complexity
```http
POST /api/nlp/analyze-complexity
Content-Type: application/json

{
  "input": "run complex integration tests for multiple services with various parameters"
}
```

**Response:**
```json
{
  "complexityLevel": "HIGH",
  "complexityScore": 10,
  "indicators": ["complex", "multiple", "various"],
  "success": true
}
```

### 4. Validate Command
```http
POST /api/nlp/validate-command
Content-Type: application/json

{
  "input": "test service"
}
```

**Response:**
```json
{
  "isValid": false,
  "suggestions": [
    "Consider adding action words like 'run', 'test', 'check', 'analyze'",
    "Specify which service: user-service, product-service, order-service, etc."
  ],
  "warnings": [
    "Command may be missing action context",
    "Service reference is ambiguous"
  ],
  "success": true
}
```

### 5. Extract Key Phrases
```http
POST /api/nlp/extract-phrases
Content-Type: application/json

{
  "input": "run unit tests and integration tests for user-service and product-service"
}
```

**Response:**
```json
{
  "keyPhrases": [
    "user-service",
    "product-service",
    "unit test",
    "integration test",
    "run"
  ],
  "hasMultipleCommands": true,
  "suggestedAlternatives": [
    "run unit tests for user-service",
    "run integration tests for product-service"
  ],
  "success": true
}
```

### 6. Comprehensive Analysis
```http
POST /api/nlp/comprehensive-analysis
Content-Type: application/json

{
  "input": "urgently run critical performance tests for user-service with 60 seconds timeout"
}
```

**Response:**
```json
{
  "intent": { /* intent object */ },
  "sentiment": {
    "type": "NEGATIVE",
    "confidence": 0.8,
    "positiveScore": 0,
    "negativeScore": 4
  },
  "complexity": {
    "level": "MEDIUM",
    "score": 6,
    "indicators": ["critical"]
  },
  "validation": {
    "isValid": true,
    "suggestions": [],
    "warnings": []
  },
  "keyPhrases": ["user-service", "performance test", "run"],
  "hasMultipleCommands": false,
  "suggestedAlternatives": [],
  "success": true
}
```

### 7. Health Check
```http
GET /api/nlp/health
```

**Response:**
```json
{
  "status": "HEALTHY",
  "nlpEngine": "OPERATIONAL",
  "nlpService": "OPERATIONAL",
  "testIntent": "RUN_TESTS",
  "timestamp": 1705330074000
}
```

## Configuration

### Application Properties
```yaml
jarvis:
  nlp:
    confidence-threshold: 0.7
    stanford-corenlp:
      enabled: true
      annotators: tokenize,ssplit,pos,lemma,ner,parse,sentiment
```

### Confidence Threshold
- **Default**: 0.7
- **Purpose**: Minimum confidence level for intent classification
- **Range**: 0.0 to 1.0

## Usage Examples

### Basic Intent Parsing
```java
@Autowired
private NLPEngine nlpEngine;

public void parseUserCommand(String userInput) {
    UserIntent intent = nlpEngine.parseIntent(userInput);
    
    if (intent.getConfidence() > 0.7) {
        switch (intent.getType()) {
            case RUN_TESTS:
                executeTests(intent.getParameters());
                break;
            case ANALYZE_FAILURES:
                analyzeFailures(intent.getParameters());
                break;
            // ... other cases
        }
    } else {
        // Handle low confidence
        requestClarification(userInput);
    }
}
```

### Sentiment Analysis
```java
@Autowired
private NLPService nlpService;

public void handleUserRequest(String userInput) {
    NLPService.SentimentAnalysis sentiment = nlpService.analyzeSentiment(userInput);
    
    if (sentiment.getType() == NLPService.SentimentType.NEGATIVE) {
        // Handle urgent/negative requests with higher priority
        prioritizeRequest(userInput);
    }
}
```

### Command Validation
```java
public void validateAndExecute(String userInput) {
    NLPService.CommandValidation validation = nlpService.validateCommand(userInput);
    
    if (!validation.isValid()) {
        // Show warnings and suggestions to user
        displayValidationResults(validation);
        return;
    }
    
    // Execute the command
    executeCommand(userInput);
}
```

## Pattern Recognition

### Intent Patterns
The NLP engine uses regex patterns to recognize intents:

```java
// RUN_TESTS patterns
Pattern.compile("\\b(run|execute|start|launch)\\b.*\\btests?\\b", Pattern.CASE_INSENSITIVE)
Pattern.compile("\\btests?\\b.*\\b(run|execute|start|launch)\\b", Pattern.CASE_INSENSITIVE)

// ANALYZE_FAILURES patterns
Pattern.compile("\\b(analyze|investigate|examine)\\b.*\\b(failures?|errors?|issues?)\\b", Pattern.CASE_INSENSITIVE)
```

### Entity Patterns
```java
// Service names
"user-service", "product-service", "order-service", "notification-service", "gateway-service"

// Test types
"unit test", "integration test", "api test", "performance test", "security test"

// Parameters
Pattern.compile("(\\d+)\\s*(seconds?|minutes?|hours?)") // Timeout
Pattern.compile("(\\d+)\\s*retries?") // Retry count
```

## Error Handling

### Null/Empty Input
- Returns `IntentType.UNKNOWN`
- Confidence score: 0.0
- Description: "Unable to parse intent from input"

### Low Confidence
- Threshold configurable via `jarvis.nlp.confidence-threshold`
- Default threshold: 0.7
- Can trigger clarification requests

### Exception Handling
- All API endpoints include try-catch blocks
- Returns appropriate HTTP status codes
- Logs errors for debugging

## Performance Considerations

### Pattern Matching
- Uses compiled regex patterns for efficiency
- Static initialization of patterns
- Case-insensitive matching

### Caching
- Consider implementing caching for frequently used patterns
- Cache sentiment analysis results for similar inputs

### Scalability
- Stateless design for horizontal scaling
- No external dependencies for core functionality
- Lightweight processing suitable for real-time use

## Testing

### Unit Tests
Comprehensive test coverage including:
- Intent recognition accuracy
- Entity extraction validation
- Edge cases (null, empty, malformed input)
- Confidence calculation verification

### Integration Tests
- API endpoint testing
- End-to-end workflow validation
- Performance testing

## Future Enhancements

### Planned Features
1. **Machine Learning Integration**: Replace pattern matching with ML models
2. **Context Awareness**: Remember conversation context
3. **Multi-language Support**: Support for multiple languages
4. **Custom Pattern Training**: Allow users to train custom patterns
5. **Advanced Entity Recognition**: Named Entity Recognition (NER)
6. **Intent Confidence Learning**: Improve confidence scoring over time

### Integration Opportunities
- **OpenAI GPT Integration**: For advanced language understanding
- **Stanford CoreNLP**: For more sophisticated NLP capabilities
- **Elasticsearch**: For semantic search and similarity matching
- **Redis**: For caching and session management
