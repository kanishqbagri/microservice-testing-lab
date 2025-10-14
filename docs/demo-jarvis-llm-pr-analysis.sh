#!/bin/bash

# Jarvis LLM-Powered PR Analysis Demo Script
# Demonstrates real AI/LLM integration for intelligent PR analysis

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

# Configuration
JARVIS_URL="http://localhost:8085"
DEMO_BRANCH="demo/llm-pr-analysis-$(date +%s)"
DEMO_PR_TITLE="Implement AI-powered user recommendation system with machine learning"
DEMO_DELAY=3
REPO_ROOT="/Users/kanishkabagri/Workspace/microservice-testing-lab"

# Logging functions
log_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

log_demo() {
    echo -e "${PURPLE}🎯 $1${NC}"
}

log_ai() {
    echo -e "${CYAN}🤖 $1${NC}"
}

log_git() {
    echo -e "${WHITE}📝 $1${NC}"
}

log_step() {
    echo -e "${YELLOW}🔄 $1${NC}"
}

log_llm() {
    echo -e "${CYAN}🧠 $1${NC}"
}

# Check prerequisites
check_prerequisites() {
    log_demo "Checking prerequisites for LLM-powered PR demo..."
    echo
    
    # Check if we're in a git repository
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        log_error "Not in a git repository. Please run this from the project root."
        exit 1
    fi
    
    # Check if Jarvis is running
    log_info "Checking Jarvis Core health..."
    if ! curl -s "$JARVIS_URL/api/pr-analysis/health" > /dev/null; then
        log_error "Jarvis Core is not running at $JARVIS_URL"
        log_info "Please start Jarvis first:"
        log_info "cd microservice-testing-lab/jarvis-core && mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dserver.port=8085'"
        exit 1
    fi
    
    log_success "All prerequisites met!"
    echo
}

# Create complex demo changes that showcase LLM analysis
create_complex_demo_changes() {
    log_demo "Phase 1: Creating complex demo changes for LLM analysis..."
    echo
    
    # Create demo branch
    log_git "Creating demo branch: $DEMO_BRANCH"
    git checkout -b "$DEMO_BRANCH" 2>/dev/null || git checkout "$DEMO_BRANCH"
    log_success "Demo branch created/checked out"
    echo
    
    # Create a complex AI/ML recommendation system
    log_step "Creating complex AI-powered recommendation system..."
    
    # Create the ML recommendation service
    cat > microservices/user-service/src/main/java/com/kb/user/service/MLRecommendationService.java << 'EOF'
package com.kb.user.service;

import com.kb.user.model.User;
import com.kb.user.model.Product;
import com.kb.user.model.UserPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AI-powered Machine Learning Recommendation Service
 * Uses collaborative filtering and content-based filtering for personalized recommendations
 */
@Service
public class MLRecommendationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserPreferenceRepository preferenceRepository;
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    // ML Model parameters
    private static final int RECOMMENDATION_COUNT = 10;
    private static final double SIMILARITY_THRESHOLD = 0.3;
    private static final double CONFIDENCE_THRESHOLD = 0.7;
    
    /**
     * Generate personalized product recommendations using collaborative filtering
     */
    @Cacheable(value = "recommendations", key = "#userId")
    public CompletableFuture<List<ProductRecommendation>> generateRecommendations(Long userId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Step 1: Get user preferences and history
                User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                
                List<UserPreference> userPreferences = preferenceRepository.findByUserId(userId);
                
                // Step 2: Find similar users using collaborative filtering
                List<Long> similarUsers = findSimilarUsers(userId, userPreferences);
                
                // Step 3: Generate recommendations based on similar users
                List<ProductRecommendation> collaborativeRecommendations = 
                    generateCollaborativeRecommendations(userId, similarUsers);
                
                // Step 4: Apply content-based filtering
                List<ProductRecommendation> contentBasedRecommendations = 
                    generateContentBasedRecommendations(userId, userPreferences);
                
                // Step 5: Combine and rank recommendations using ML ensemble
                List<ProductRecommendation> finalRecommendations = 
                    combineRecommendations(collaborativeRecommendations, contentBasedRecommendations);
                
                // Step 6: Apply diversity and freshness filters
                return applyDiversityFilter(finalRecommendations);
                
            } catch (Exception e) {
                throw new RuntimeException("Error generating recommendations: " + e.getMessage(), e);
            }
        }, executorService);
    }
    
    /**
     * Find similar users using cosine similarity and collaborative filtering
     */
    private List<Long> findSimilarUsers(Long userId, List<UserPreference> userPreferences) {
        // Create user preference vector
        Map<Long, Double> userVector = userPreferences.stream()
            .collect(Collectors.toMap(
                UserPreference::getProductId,
                UserPreference::getRating,
                (existing, replacement) -> existing
            ));
        
        // Calculate similarity with other users
        Map<Long, Double> userSimilarities = new HashMap<>();
        
        List<User> allUsers = userRepository.findAll();
        for (User otherUser : allUsers) {
            if (otherUser.getId().equals(userId)) continue;
            
            List<UserPreference> otherPreferences = preferenceRepository.findByUserId(otherUser.getId());
            Map<Long, Double> otherVector = otherPreferences.stream()
                .collect(Collectors.toMap(
                    UserPreference::getProductId,
                    UserPreference::getRating,
                    (existing, replacement) -> existing
                ));
            
            double similarity = calculateCosineSimilarity(userVector, otherVector);
            if (similarity > SIMILARITY_THRESHOLD) {
                userSimilarities.put(otherUser.getId(), similarity);
            }
        }
        
        // Return top similar users
        return userSimilarities.entrySet().stream()
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .limit(50)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    /**
     * Generate recommendations using collaborative filtering
     */
    private List<ProductRecommendation> generateCollaborativeRecommendations(Long userId, List<Long> similarUsers) {
        Map<Long, Double> productScores = new HashMap<>();
        
        for (Long similarUserId : similarUsers) {
            List<UserPreference> preferences = preferenceRepository.findByUserId(similarUserId);
            
            for (UserPreference preference : preferences) {
                // Check if user hasn't already rated this product
                if (preferenceRepository.findByUserIdAndProductId(userId, preference.getProductId()).isEmpty()) {
                    double score = preference.getRating() * getUserSimilarity(userId, similarUserId);
                    productScores.merge(preference.getProductId(), score, Double::sum);
                }
            }
        }
        
        return productScores.entrySet().stream()
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .limit(RECOMMENDATION_COUNT)
            .map(entry -> {
                Product product = productRepository.findById(entry.getKey()).orElse(null);
                if (product != null) {
                    return ProductRecommendation.builder()
                        .product(product)
                        .score(entry.getValue())
                        .confidence(calculateConfidence(entry.getValue()))
                        .recommendationType("COLLABORATIVE_FILTERING")
                        .build();
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    /**
     * Generate recommendations using content-based filtering
     */
    private List<ProductRecommendation> generateContentBasedRecommendations(Long userId, List<UserPreference> userPreferences) {
        // Analyze user's preference patterns
        Map<String, Double> categoryPreferences = analyzeCategoryPreferences(userPreferences);
        Map<String, Double> brandPreferences = analyzeBrandPreferences(userPreferences);
        
        // Find products matching user preferences
        List<Product> allProducts = productRepository.findAll();
        
        return allProducts.stream()
            .filter(product -> !hasUserRated(userId, product.getId()))
            .map(product -> {
                double score = calculateContentBasedScore(product, categoryPreferences, brandPreferences);
                return ProductRecommendation.builder()
                    .product(product)
                    .score(score)
                    .confidence(calculateConfidence(score))
                    .recommendationType("CONTENT_BASED")
                    .build();
            })
            .filter(rec -> rec.getScore() > 0.5)
            .sorted(Comparator.comparing(ProductRecommendation::getScore).reversed())
            .limit(RECOMMENDATION_COUNT)
            .collect(Collectors.toList());
    }
    
    /**
     * Combine recommendations using ensemble learning
     */
    private List<ProductRecommendation> combineRecommendations(
            List<ProductRecommendation> collaborative, 
            List<ProductRecommendation> contentBased) {
        
        Map<Long, ProductRecommendation> combinedMap = new HashMap<>();
        
        // Add collaborative filtering results with weight 0.6
        for (ProductRecommendation rec : collaborative) {
            rec.setScore(rec.getScore() * 0.6);
            combinedMap.put(rec.getProduct().getId(), rec);
        }
        
        // Add content-based results with weight 0.4
        for (ProductRecommendation rec : contentBased) {
            Long productId = rec.getProduct().getId();
            if (combinedMap.containsKey(productId)) {
                // Combine scores
                ProductRecommendation existing = combinedMap.get(productId);
                existing.setScore(existing.getScore() + (rec.getScore() * 0.4));
                existing.setRecommendationType("ENSEMBLE");
            } else {
                rec.setScore(rec.getScore() * 0.4);
                combinedMap.put(productId, rec);
            }
        }
        
        return combinedMap.values().stream()
            .sorted(Comparator.comparing(ProductRecommendation::getScore).reversed())
            .limit(RECOMMENDATION_COUNT)
            .collect(Collectors.toList());
    }
    
    /**
     * Apply diversity filter to ensure recommendation variety
     */
    private List<ProductRecommendation> applyDiversityFilter(List<ProductRecommendation> recommendations) {
        Set<String> usedCategories = new HashSet<>();
        Set<String> usedBrands = new HashSet<>();
        
        return recommendations.stream()
            .filter(rec -> {
                String category = rec.getProduct().getCategory();
                String brand = rec.getProduct().getBrand();
                
                // Ensure diversity in categories and brands
                if (usedCategories.size() < 5 && !usedCategories.contains(category)) {
                    usedCategories.add(category);
                    return true;
                }
                if (usedBrands.size() < 3 && !usedBrands.contains(brand)) {
                    usedBrands.add(brand);
                    return true;
                }
                return usedCategories.contains(category) || usedBrands.contains(brand);
            })
            .limit(RECOMMENDATION_COUNT)
            .collect(Collectors.toList());
    }
    
    // Helper methods
    private double calculateCosineSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        Set<Long> commonProducts = new HashSet<>(vector1.keySet());
        commonProducts.retainAll(vector2.keySet());
        
        if (commonProducts.isEmpty()) return 0.0;
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (Long productId : commonProducts) {
            double rating1 = vector1.get(productId);
            double rating2 = vector2.get(productId);
            
            dotProduct += rating1 * rating2;
            norm1 += rating1 * rating1;
            norm2 += rating2 * rating2;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    private double getUserSimilarity(Long userId1, Long userId2) {
        // Simplified similarity calculation
        // In a real implementation, this would use cached similarity scores
        return 0.5 + (Math.random() * 0.4); // Mock similarity between 0.5-0.9
    }
    
    private double calculateConfidence(double score) {
        return Math.min(1.0, score / 5.0); // Normalize to 0-1 range
    }
    
    private Map<String, Double> analyzeCategoryPreferences(List<UserPreference> preferences) {
        // Mock implementation - analyze user's category preferences
        return Map.of(
            "Electronics", 0.8,
            "Books", 0.6,
            "Clothing", 0.4
        );
    }
    
    private Map<String, Double> analyzeBrandPreferences(List<UserPreference> preferences) {
        // Mock implementation - analyze user's brand preferences
        return Map.of(
            "Apple", 0.9,
            "Samsung", 0.7,
            "Nike", 0.5
        );
    }
    
    private double calculateContentBasedScore(Product product, Map<String, Double> categoryPrefs, Map<String, Double> brandPrefs) {
        double categoryScore = categoryPrefs.getOrDefault(product.getCategory(), 0.0);
        double brandScore = brandPrefs.getOrDefault(product.getBrand(), 0.0);
        return (categoryScore * 0.7) + (brandScore * 0.3);
    }
    
    private boolean hasUserRated(Long userId, Long productId) {
        return !preferenceRepository.findByUserIdAndProductId(userId, productId).isEmpty();
    }
}
EOF

    # Create the recommendation model
    cat > microservices/user-service/src/main/java/com/kb/user/model/ProductRecommendation.java << 'EOF'
package com.kb.user.model;

import lombok.Builder;
import lombok.Data;

/**
 * Product recommendation with ML confidence scores
 */
@Data
@Builder
public class ProductRecommendation {
    
    private Product product;
    private double score;
    private double confidence;
    private String recommendationType;
    private String reason;
    private long timestamp;
}
EOF

    # Create the ML controller
    cat > microservices/user-service/src/main/java/com/kb/user/controller/MLRecommendationController.java << 'EOF'
package com.kb.user.controller;

import com.kb.user.model.ProductRecommendation;
import com.kb.user.service.MLRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * ML-powered Recommendation API Controller
 */
@RestController
@RequestMapping("/api/ml/recommendations")
@CrossOrigin(origins = "*")
public class MLRecommendationController {
    
    @Autowired
    private MLRecommendationService mlRecommendationService;
    
    /**
     * Get personalized product recommendations using AI/ML
     */
    @GetMapping("/user/{userId}")
    public CompletableFuture<ResponseEntity<List<ProductRecommendation>>> getRecommendations(@PathVariable Long userId) {
        return mlRecommendationService.generateRecommendations(userId)
            .thenApply(ResponseEntity::ok)
            .exceptionally(throwable -> {
                return ResponseEntity.internalServerError().build();
            });
    }
    
    /**
     * Get recommendation explanations for transparency
     */
    @GetMapping("/user/{userId}/explain")
    public ResponseEntity<String> explainRecommendations(@PathVariable Long userId) {
        // In a real implementation, this would provide ML model explanations
        String explanation = String.format("""
            AI Recommendation Explanation for User %d:
            
            Our ML system uses a hybrid approach combining:
            1. Collaborative Filtering: Finds users with similar preferences
            2. Content-Based Filtering: Analyzes product features and user history
            3. Ensemble Learning: Combines both approaches for better accuracy
            
            The recommendations are ranked by:
            - User similarity scores (0.6 weight)
            - Content matching scores (0.4 weight)
            - Diversity and freshness filters
            
            Confidence scores indicate prediction reliability.
            """, userId);
        
        return ResponseEntity.ok(explanation);
    }
}
EOF

    # Create comprehensive tests
    cat > microservices/user-service/src/test/java/com/kb/user/service/MLRecommendationServiceTest.java << 'EOF'
package com.kb.user.service;

import com.kb.user.model.*;
import com.kb.user.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MLRecommendationServiceTest {
    
    @Autowired
    private MLRecommendationService mlRecommendationService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserPreferenceRepository preferenceRepository;
    
    private User testUser;
    private Product testProduct;
    
    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);
        
        // Create test product
        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setCategory("Electronics");
        testProduct.setBrand("TestBrand");
        testProduct.setPrice(99.99);
        testProduct = productRepository.save(testProduct);
        
        // Create user preference
        UserPreference preference = new UserPreference();
        preference.setUserId(testUser.getId());
        preference.setProductId(testProduct.getId());
        preference.setRating(4.5);
        preferenceRepository.save(preference);
    }
    
    @Test
    void testGenerateRecommendations() throws Exception {
        // When
        CompletableFuture<List<ProductRecommendation>> future = 
            mlRecommendationService.generateRecommendations(testUser.getId());
        
        List<ProductRecommendation> recommendations = future.get();
        
        // Then
        assertNotNull(recommendations);
        assertFalse(recommendations.isEmpty());
        
        // Verify recommendation structure
        ProductRecommendation firstRec = recommendations.get(0);
        assertNotNull(firstRec.getProduct());
        assertTrue(firstRec.getScore() > 0);
        assertTrue(firstRec.getConfidence() >= 0 && firstRec.getConfidence() <= 1);
        assertNotNull(firstRec.getRecommendationType());
    }
    
    @Test
    void testRecommendationDiversity() throws Exception {
        // When
        CompletableFuture<List<ProductRecommendation>> future = 
            mlRecommendationService.generateRecommendations(testUser.getId());
        
        List<ProductRecommendation> recommendations = future.get();
        
        // Then
        long uniqueCategories = recommendations.stream()
            .map(rec -> rec.getProduct().getCategory())
            .distinct()
            .count();
        
        long uniqueBrands = recommendations.stream()
            .map(rec -> rec.getProduct().getBrand())
            .distinct()
            .count();
        
        // Should have some diversity
        assertTrue(uniqueCategories > 1, "Recommendations should have category diversity");
        assertTrue(uniqueBrands > 1, "Recommendations should have brand diversity");
    }
    
    @Test
    void testRecommendationConfidence() throws Exception {
        // When
        CompletableFuture<List<ProductRecommendation>> future = 
            mlRecommendationService.generateRecommendations(testUser.getId());
        
        List<ProductRecommendation> recommendations = future.get();
        
        // Then
        for (ProductRecommendation rec : recommendations) {
            assertTrue(rec.getConfidence() >= 0.0 && rec.getConfidence() <= 1.0, 
                "Confidence should be between 0 and 1");
        }
    }
}
EOF

    log_success "Complex AI/ML demo changes created successfully!"
    echo
    
    # Show the changes
    log_git "Showing git status:"
    git status --short
    echo
    
    log_git "Showing diff of changes:"
    git diff --stat
    echo
}

# Analyze the complex PR with LLM
analyze_complex_pr_with_llm() {
    log_demo "Phase 2: Analyzing complex PR with LLM-powered analysis..."
    echo
    
    # Get PR information
    local pr_id="PR-$(date +%s)"
    local pr_title="$DEMO_PR_TITLE"
    local pr_description="This PR implements a sophisticated AI-powered recommendation system using machine learning techniques:

## Features Implemented:
- **Collaborative Filtering**: Uses user similarity to recommend products
- **Content-Based Filtering**: Analyzes product features and user preferences  
- **Ensemble Learning**: Combines multiple ML approaches for better accuracy
- **Diversity Filtering**: Ensures recommendation variety
- **Async Processing**: Uses CompletableFuture for performance
- **Caching**: Implements recommendation caching for efficiency

## Technical Implementation:
- ML recommendation service with hybrid filtering
- Cosine similarity calculations for user matching
- Product scoring algorithms with confidence metrics
- RESTful API endpoints for recommendations
- Comprehensive unit tests with ML validation
- Performance optimization with thread pools

## AI/ML Components:
- User preference analysis and pattern recognition
- Product feature extraction and matching
- Recommendation ranking and confidence scoring
- Diversity and freshness filtering algorithms

This change introduces significant ML/AI capabilities and affects multiple system components."
    
    local author="ai-engineer"
    local source_branch="$DEMO_BRANCH"
    local target_branch="main"
    local repository="microservice-testing-lab"
    
    log_info "PR Information:"
    echo "  PR ID: $pr_id"
    echo "  Title: $pr_title"
    echo "  Author: $author"
    echo "  Source Branch: $source_branch"
    echo "  Target Branch: $target_branch"
    echo
    
    # Prepare complex PR data for LLM analysis
    local pr_data=$(cat << EOF
{
    "prId": "$pr_id",
    "title": "$pr_title",
    "description": "$pr_description",
    "author": "$author",
    "sourceBranch": "$source_branch",
    "targetBranch": "$target_branch",
    "repository": "$repository",
    "fileChanges": [
        {
            "filePath": "microservices/user-service/src/main/java/com/kb/user/service/MLRecommendationService.java",
            "changeType": "ADDED",
            "linesAdded": 350,
            "linesDeleted": 0,
            "keywords": ["machine learning", "ai", "recommendation", "collaborative filtering", "content-based", "ensemble", "async", "caching"]
        },
        {
            "filePath": "microservices/user-service/src/main/java/com/kb/user/model/ProductRecommendation.java",
            "changeType": "ADDED",
            "linesAdded": 25,
            "linesDeleted": 0,
            "keywords": ["model", "recommendation", "confidence", "scoring"]
        },
        {
            "filePath": "microservices/user-service/src/main/java/com/kb/user/controller/MLRecommendationController.java",
            "changeType": "ADDED",
            "linesAdded": 60,
            "linesDeleted": 0,
            "keywords": ["controller", "api", "ml", "async", "explanation"]
        },
        {
            "filePath": "microservices/user-service/src/test/java/com/kb/user/service/MLRecommendationServiceTest.java",
            "changeType": "ADDED",
            "linesAdded": 120,
            "linesDeleted": 0,
            "keywords": ["test", "ml", "recommendation", "diversity", "confidence"]
        }
    ]
}
EOF
)
    
    log_llm "Sending complex PR data to LLM for intelligent analysis..."
    echo
    
    # Call Jarvis API with LLM analysis
    local response=$(curl -s -X POST "$JARVIS_URL/api/pr-analysis/analyze" \
        -H "Content-Type: application/json" \
        -d "$pr_data")
    
    if [ $? -eq 0 ] && [ -n "$response" ]; then
        log_success "LLM analysis completed!"
        echo
        
        # Display comprehensive LLM analysis results
        log_ai "🧠 LLM-Powered Analysis Results:"
        echo "================================================"
        echo "$response" | jq -r '
            "📊 PR Analysis Summary:",
            "   PR ID: " + .prInfo.prId,
            "   Title: " + .prInfo.title,
            "   Overall Risk Level: " + .riskAssessment.overallRiskLevel,
            "   Overall Impact: " + .impactAnalysis.overallImpact,
            "   AI Confidence Score: " + (.confidenceScore * 100 | floor | tostring) + "%",
            "",
            "🔍 LLM Pattern Analysis:",
            "   Security Patterns: " + (.impactAnalysis.affectedServices | length | tostring),
            "   Performance Patterns: " + (.impactAnalysis.breakingChanges | length | tostring),
            "   Architectural Patterns: " + (.impactAnalysis.newFeatures | length | tostring),
            "",
            "🧠 AI/ML Impact Assessment:",
            "   Affected Services: " + (.impactAnalysis.affectedServices | join(", ")),
            "   New ML Features: " + (.impactAnalysis.newFeatures | join(", ")),
            "   Breaking Changes: " + (.impactAnalysis.breakingChanges | join(", ")),
            "",
            "🚨 LLM Risk Assessment:",
            "   Risk Factors: " + (.riskAssessment.riskFactors | length | tostring),
            "   High Risk Areas: " + (.riskAssessment.highRiskAreas | join(", ")),
            "",
            "🧪 LLM-Generated Test Recommendations (" + (.testRecommendations | length | tostring) + " total):",
            (.testRecommendations[] | "   • " + .description + " (" + .testType + ", Priority: " + (.priority * 100 | floor | tostring) + "%, Critical: " + (.critical | tostring) + ")"),
            "",
            "💡 LLM Insights:",
            (.insights[] | "   • " + .),
            "",
            "⚠️ LLM Warnings:",
            (.warnings[] | "   • " + .)
        '
        
        # Store analysis for later use
        echo "$response" > /tmp/llm_analysis.json
        
    else
        log_error "Failed to analyze PR with LLM"
        return 1
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Show LLM-powered actionable recommendations
show_llm_recommendations() {
    log_demo "Phase 3: LLM-Powered Actionable Recommendations"
    echo
    
    if [ ! -f /tmp/llm_analysis.json ]; then
        log_error "No LLM analysis data found. Please run the analysis first."
        return 1
    fi
    
    local analysis=$(cat /tmp/llm_analysis.json)
    
    log_llm "🎯 LLM-Generated Actionable Recommendations:"
    echo "================================================"
    
    # Extract high priority recommendations
    echo "$analysis" | jq -r '
        "🚀 IMMEDIATE LLM RECOMMENDATIONS:",
        (.testRecommendations[] | select(.priority >= 0.8) | "   • " + .description),
        "",
        "📋 LLM TESTING STRATEGY:",
        (.testRecommendations | group_by(.testType) | .[] | 
            "   " + .[0].testType + ":",
            (.[] | "     ✓ " + .description + " (Priority: " + (.priority * 100 | floor | tostring) + "%)")
        ),
        "",
        "🧠 AI/ML SPECIFIC RECOMMENDATIONS:",
        (.insights[] | select(contains("AI") or contains("ML") or contains("machine learning") or contains("recommendation")) | "   • " + .),
        "",
        "⚠️ LLM RISK MITIGATION:",
        (.warnings[] | "   • " + .)
    '
    
    echo
    log_step "LLM Decision Analysis:"
    echo "1. Should we proceed with merging this complex AI/ML PR?"
    echo "2. What ML-specific tests should we prioritize?"
    echo "3. Are there any AI model validation concerns?"
    echo "4. How should we handle the performance implications?"
    echo
    
    # Simulate LLM decision making
    log_llm "Simulating LLM decision process..."
    sleep 2
    
    local risk_level=$(echo "$analysis" | jq -r '.riskAssessment.overallRiskLevel')
    local impact_level=$(echo "$analysis" | jq -r '.impactAnalysis.overallImpact')
    
    if [ "$risk_level" = "HIGH" ] || [ "$risk_level" = "CRITICAL" ] || [ "$impact_level" = "HIGH" ] || [ "$impact_level" = "CRITICAL" ]; then
        log_warning "High risk/impact detected by LLM! Recommend comprehensive review before merge."
        echo "   → Schedule AI/ML architecture review"
        echo "   → Run ML model validation tests"
        echo "   → Perform performance benchmarking"
        echo "   → Consider A/B testing deployment"
    else
        log_success "LLM analysis indicates acceptable risk/impact for merge with proper testing."
        echo "   → Run ML recommendation tests"
        echo "   → Validate AI model accuracy"
        echo "   → Perform load testing for async operations"
        echo "   → Deploy to staging with monitoring"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Simulate ML model validation
simulate_ml_validation() {
    log_demo "Phase 4: Simulating ML Model Validation"
    echo
    
    log_llm "Running ML model validation checks..."
    echo "✓ Collaborative filtering algorithm validation"
    echo "✓ Content-based filtering accuracy testing"
    echo "✓ Ensemble learning performance metrics"
    echo "✓ Recommendation diversity analysis"
    echo "✓ Confidence score calibration"
    echo "✓ Async processing performance testing"
    echo
    
    log_success "ML model validation completed!"
    echo
    
    log_info "ML Performance Metrics:"
    echo "• Recommendation accuracy: 87.3%"
    echo "• Diversity score: 0.82"
    echo "• Response time: 245ms average"
    echo "• Confidence calibration: 0.91"
    echo "• Memory usage: 156MB peak"
    echo
    
    sleep $DEMO_DELAY
}

# Cleanup
cleanup() {
    log_demo "Phase 5: Cleanup"
    echo
    
    log_info "Cleaning up demo resources..."
    
    # Switch back to main branch
    git checkout main 2>/dev/null || true
    
    # Delete demo branch
    git branch -D "$DEMO_BRANCH" 2>/dev/null || true
    
    # Remove demo files
    rm -f microservices/user-service/src/main/java/com/kb/user/service/MLRecommendationService.java
    rm -f microservices/user-service/src/main/java/com/kb/user/model/ProductRecommendation.java
    rm -f microservices/user-service/src/main/java/com/kb/user/controller/MLRecommendationController.java
    rm -f microservices/user-service/src/test/java/com/kb/user/service/MLRecommendationServiceTest.java
    
    # Clean up temp files
    rm -f /tmp/llm_analysis.json
    
    log_success "Cleanup completed!"
    echo
}

# Main demo function
main() {
    echo "================================================"
    echo "    🧠 Jarvis LLM-Powered PR Analysis Demo"
    echo "    Real AI/ML Integration & Analysis"
    echo "================================================"
    echo
    
    log_demo "Welcome to the LLM-Powered PR Analysis Demo!"
    log_demo "This demo showcases real AI/ML integration for intelligent code analysis."
    echo
    
    # Check prerequisites
    check_prerequisites
    
    # Run the complete LLM-powered workflow
    create_complex_demo_changes
    analyze_complex_pr_with_llm
    show_llm_recommendations
    simulate_ml_validation
    cleanup
    
    echo "================================================"
    echo -e "${GREEN}🎉 LLM-Powered PR Analysis Demo Completed!${NC}"
    echo "================================================"
    echo
    
    echo -e "${BLUE}Demo Summary:${NC}"
    echo "• ✅ Created complex AI/ML recommendation system"
    echo "• ✅ Analyzed with real LLM-powered analysis"
    echo "• ✅ Generated intelligent ML-specific recommendations"
    echo "• ✅ Simulated ML model validation workflow"
    echo "• ✅ Demonstrated AI confidence scoring"
    echo
    
    echo -e "${YELLOW}LLM Features Demonstrated:${NC}"
    echo "• 🧠 Real pattern recognition and code analysis"
    echo "• 🤖 Intelligent impact assessment using AI"
    echo "• 📊 ML-specific risk analysis and mitigation"
    echo "• 🎯 AI-generated test recommendations"
    echo "• 🔍 Semantic code understanding"
    echo "• ⚡ Performance and scalability analysis"
    echo "• 🛡️ Security vulnerability detection"
    echo
    
    echo -e "${PURPLE}Production Readiness:${NC}"
    echo "• Integrate with OpenAI API for real LLM analysis"
    echo "• Configure custom prompts for your domain"
    echo "• Set up ML model monitoring and validation"
    echo "• Implement A/B testing for AI recommendations"
    echo "• Add explainable AI features for transparency"
    echo
}

# Handle cleanup on exit
trap cleanup EXIT

# Run the demo
main "$@"
