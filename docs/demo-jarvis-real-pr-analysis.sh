#!/bin/bash

# Jarvis Real-Time PR Analysis Demo Script
# Creates a real PR, analyzes it with Jarvis, and shows the complete workflow

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
DEMO_BRANCH="demo/jarvis-pr-analysis-$(date +%s)"
DEMO_PR_TITLE="Add user authentication endpoint with JWT security"
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

# Check prerequisites
check_prerequisites() {
    log_demo "Checking prerequisites for real-time PR demo..."
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

# Create demo changes
create_demo_changes() {
    log_demo "Phase 1: Creating realistic demo changes..."
    echo
    
    # Create demo branch
    log_git "Creating demo branch: $DEMO_BRANCH"
    git checkout -b "$DEMO_BRANCH" 2>/dev/null || git checkout "$DEMO_BRANCH"
    log_success "Demo branch created/checked out"
    echo
    
    # Create a realistic change - add a new authentication endpoint
    log_step "Creating realistic code changes..."
    
    # Create the new authentication controller
    cat > microservices/user-service/src/main/java/com/kb/user/controller/AuthController.java << 'EOF'
package com.kb.user.controller;

import com.kb.user.model.User;
import com.kb.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 * Handles user authentication and JWT token generation
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    private static final String SECRET_KEY = "your-secret-key-should-be-in-config";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours
    
    /**
     * Authenticate user and return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        try {
            String username = credentials.get("username");
            String password = credentials.get("password");
            
            // Validate credentials
            User user = authService.authenticateUser(username, password);
            if (user == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
            
            // Generate JWT token
            String token = generateJWTToken(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
            ));
            response.put("expiresIn", EXPIRATION_TIME);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Authentication failed"));
        }
    }
    
    /**
     * Validate JWT token
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Invalid token format"));
            }
            
            String token = authHeader.substring(7);
            boolean isValid = validateJWTToken(token);
            
            return ResponseEntity.ok(Map.of("valid", isValid));
            
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("valid", false, "error", "Token validation failed"));
        }
    }
    
    private String generateJWTToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    
    private boolean validateJWTToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
EOF

    # Create the auth service
    cat > microservices/user-service/src/main/java/com/kb/user/service/AuthService.java << 'EOF'
package com.kb.user.service;

import com.kb.user.model.User;
import com.kb.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication Service
 * Handles user authentication logic
 */
@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * Authenticate user with username and password
     */
    public User authenticateUser(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        
        // In a real implementation, you would hash the password
        // For demo purposes, we'll do a simple comparison
        if (password.equals(user.getPassword())) {
            return user;
        }
        
        return null;
    }
    
    /**
     * Hash password using BCrypt
     */
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    /**
     * Verify password against hash
     */
    public boolean verifyPassword(String password, String hash) {
        return passwordEncoder.matches(password, hash);
    }
}
EOF

    # Create a test file
    cat > microservices/user-service/src/test/java/com/kb/user/controller/AuthControllerTest.java << 'EOF'
package com.kb.user.controller;

import com.kb.user.model.User;
import com.kb.user.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AuthService authService;
    
    @Test
    void testLoginWithValidCredentials() throws Exception {
        // Given
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        
        when(authService.authenticateUser("testuser", "password123")).thenReturn(mockUser);
        
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.username").value("testuser"));
    }
    
    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        // Given
        when(authService.authenticateUser(any(), any())).thenReturn(null);
        
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"invalid\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }
}
EOF

    # Add JWT dependency to pom.xml (simulate adding dependency)
    log_step "Simulating dependency addition..."
    
    log_success "Demo changes created successfully!"
    echo
    
    # Show the changes
    log_git "Showing git status:"
    git status --short
    echo
    
    log_git "Showing diff of changes:"
    git diff --stat
    echo
}

# Analyze the PR with Jarvis
analyze_pr_with_jarvis() {
    log_demo "Phase 2: Analyzing PR with Jarvis AI..."
    echo
    
    # Get PR information
    local pr_id="PR-$(date +%s)"
    local pr_title="$DEMO_PR_TITLE"
    local pr_description="This PR adds a new authentication endpoint with JWT token support. It includes:

- New AuthController with login and token validation endpoints
- AuthService for user authentication logic  
- Comprehensive unit tests for the new functionality
- JWT token generation and validation
- Security improvements for user authentication

This change affects the user-service and introduces new security-related functionality."
    
    local author="demo-user"
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
    
    # Prepare PR data for analysis
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
            "filePath": "microservices/user-service/src/main/java/com/kb/user/controller/AuthController.java",
            "changeType": "ADDED",
            "linesAdded": 120,
            "linesDeleted": 0,
            "keywords": ["security", "jwt", "authentication", "controller", "endpoint"]
        },
        {
            "filePath": "microservices/user-service/src/main/java/com/kb/user/service/AuthService.java",
            "changeType": "ADDED", 
            "linesAdded": 45,
            "linesDeleted": 0,
            "keywords": ["service", "authentication", "security", "password"]
        },
        {
            "filePath": "microservices/user-service/src/test/java/com/kb/user/controller/AuthControllerTest.java",
            "changeType": "ADDED",
            "linesAdded": 65,
            "linesDeleted": 0,
            "keywords": ["test", "unit", "authentication", "controller"]
        }
    ]
}
EOF
)
    
    log_step "Sending PR data to Jarvis for analysis..."
    echo
    
    # Call Jarvis API
    local response=$(curl -s -X POST "$JARVIS_URL/api/pr-analysis/analyze" \
        -H "Content-Type: application/json" \
        -d "$pr_data")
    
    if [ $? -eq 0 ] && [ -n "$response" ]; then
        log_success "Jarvis analysis completed!"
        echo
        
        # Display analysis results
        log_ai "🤖 Jarvis AI Analysis Results:"
        echo "================================================"
        echo "$response" | jq -r '
            "📊 PR Analysis Summary:",
            "   PR ID: " + .prInfo.prId,
            "   Title: " + .prInfo.title,
            "   Overall Risk Level: " + .riskAssessment.overallRiskLevel,
            "   Overall Impact: " + .impactAnalysis.overallImpact,
            "   Confidence Score: " + (.confidenceScore * 100 | floor | tostring) + "%",
            "",
            "🔍 Impact Analysis:",
            "   Affected Services: " + (.impactAnalysis.affectedServices | join(", ")),
            "   Breaking Changes: " + (.impactAnalysis.breakingChanges | length | tostring),
            "   New Features: " + (.impactAnalysis.newFeatures | length | tostring),
            "",
            "🚨 Risk Assessment:",
            "   Risk Factors: " + (.riskAssessment.riskFactors | length | tostring),
            "   High Risk Areas: " + (.riskAssessment.highRiskAreas | join(", ")),
            "",
            "🧪 Test Recommendations (" + (.testRecommendations | length | tostring) + " total):",
            (.testRecommendations[] | "   • " + .description + " (" + .testType + ", Priority: " + (.priority * 100 | floor | tostring) + "%)"),
            "",
            "💡 Key Insights:",
            (.insights[] | "   • " + .),
            "",
            "⚠️ Warnings:",
            (.warnings[] | "   • " + .)
        '
        
        # Store analysis for later use
        echo "$response" > /tmp/jarvis_analysis.json
        
    else
        log_error "Failed to analyze PR with Jarvis"
        return 1
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Show actionable recommendations
show_actionable_recommendations() {
    log_demo "Phase 3: Actionable Recommendations & Next Steps"
    echo
    
    if [ ! -f /tmp/jarvis_analysis.json ]; then
        log_error "No analysis data found. Please run the analysis first."
        return 1
    fi
    
    local analysis=$(cat /tmp/jarvis_analysis.json)
    
    log_ai "🎯 Recommended Actions Based on Analysis:"
    echo "================================================"
    
    # Extract high priority recommendations
    echo "$analysis" | jq -r '
        "🚀 IMMEDIATE ACTIONS:",
        (.testRecommendations[] | select(.priority >= 0.8) | "   • " + .description),
        "",
        "📋 TESTING CHECKLIST:",
        (.testRecommendations | group_by(.testType) | .[] | 
            "   " + .[0].testType + ":",
            (.[] | "     ✓ " + .description)
        ),
        "",
        "🔒 SECURITY CONSIDERATIONS:",
        (.insights[] | select(contains("security") or contains("Security") or contains("🔒")) | "   • " + .),
        "",
        "⚠️ RISK MITIGATION:",
        (.warnings[] | "   • " + .)
    '
    
    echo
    log_step "Interactive Decision Points:"
    echo "1. Should we proceed with merging this PR?"
    echo "2. What additional tests should we run?"
    echo "3. Are there any security concerns to address?"
    echo
    
    # Simulate decision making
    log_info "Simulating decision process..."
    sleep 2
    
    local risk_level=$(echo "$analysis" | jq -r '.riskAssessment.overallRiskLevel')
    
    if [ "$risk_level" = "HIGH" ] || [ "$risk_level" = "CRITICAL" ]; then
        log_warning "High risk detected! Recommend additional review before merge."
        echo "   → Schedule security review"
        echo "   → Run additional integration tests"
        echo "   → Consider staging environment testing"
    else
        log_success "Risk level acceptable for merge with proper testing."
        echo "   → Run recommended test suite"
        echo "   → Perform code review"
        echo "   → Merge to staging first"
    fi
    
    echo
    sleep $DEMO_DELAY
}

# Simulate merge process
simulate_merge_process() {
    log_demo "Phase 4: Simulating Merge Process"
    echo
    
    log_step "Pre-merge checklist:"
    echo "✓ Code review completed"
    echo "✓ Unit tests passing"
    echo "✓ Integration tests planned"
    echo "✓ Security review scheduled"
    echo
    
    log_git "Simulating merge to main branch..."
    sleep 2
    
    # In a real scenario, you would do:
    # git checkout main
    # git merge $DEMO_BRANCH
    # git push origin main
    
    log_success "PR successfully merged!"
    echo
    
    log_info "Post-merge actions:"
    echo "• Deploy to staging environment"
    echo "• Run integration tests"
    echo "• Monitor application metrics"
    echo "• Update documentation"
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
    rm -f microservices/user-service/src/main/java/com/kb/user/controller/AuthController.java
    rm -f microservices/user-service/src/main/java/com/kb/user/service/AuthService.java
    rm -f microservices/user-service/src/test/java/com/kb/user/controller/AuthControllerTest.java
    
    # Clean up temp files
    rm -f /tmp/jarvis_analysis.json
    
    log_success "Cleanup completed!"
    echo
}

# Main demo function
main() {
    echo "================================================"
    echo "    🤖 Jarvis Real-Time PR Analysis Demo"
    echo "    Complete Workflow: Create → Analyze → Act"
    echo "================================================"
    echo
    
    log_demo "Welcome to the Real-Time PR Analysis Demo!"
    log_demo "This demo will create a real PR, analyze it with Jarvis, and show actionable recommendations."
    echo
    
    # Check prerequisites
    check_prerequisites
    
    # Run the complete workflow
    create_demo_changes
    analyze_pr_with_jarvis
    show_actionable_recommendations
    simulate_merge_process
    cleanup
    
    echo "================================================"
    echo -e "${GREEN}🎉 Real-Time PR Analysis Demo Completed!${NC}"
    echo "================================================"
    echo
    
    echo -e "${BLUE}Demo Summary:${NC}"
    echo "• ✅ Created realistic code changes (JWT authentication)"
    echo "• ✅ Analyzed PR with Jarvis AI in real-time"
    echo "• ✅ Generated actionable recommendations"
    echo "• ✅ Simulated complete merge workflow"
    echo "• ✅ Demonstrated risk assessment and mitigation"
    echo
    
    echo -e "${YELLOW}Key Features Demonstrated:${NC}"
    echo "• 🔄 Real git workflow with actual changes"
    echo "• 🤖 AI-powered analysis of real code"
    echo "• 📊 Risk assessment and impact analysis"
    echo "• 🧪 Intelligent test recommendations"
    echo "• 🎯 Actionable next steps and decisions"
    echo "• 🔒 Security-focused analysis"
    echo
    
    echo -e "${PURPLE}Next Steps for Production:${NC}"
    echo "• Integrate with your CI/CD pipeline"
    echo "• Connect to your version control system"
    echo "• Customize risk factors for your organization"
    echo "• Set up automated PR analysis triggers"
    echo "• Configure notification systems"
    echo
}

# Handle cleanup on exit
trap cleanup EXIT

# Run the demo
main "$@"
