package com.kb.contractgenerator.service;

import com.kb.contractgenerator.model.SwaggerSpec;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SwaggerParserService {

    public SwaggerSpec parseSwaggerSpec(String swaggerContent) {
        SwaggerParseResult result = new OpenAPIParser().readContents(swaggerContent, null, null);
        OpenAPI openAPI = result.getOpenAPI();
        
        if (openAPI == null) {
            throw new RuntimeException("Failed to parse Swagger specification: " + result.getMessages());
        }
        
        return convertToSwaggerSpec(openAPI);
    }

    public SwaggerSpec parseSwaggerFromUrl(String url) {
        SwaggerParseResult result = new OpenAPIParser().readLocation(url, null, null);
        OpenAPI openAPI = result.getOpenAPI();
        
        if (openAPI == null) {
            throw new RuntimeException("Failed to parse Swagger specification from URL: " + result.getMessages());
        }
        
        return convertToSwaggerSpec(openAPI);
    }

    private SwaggerSpec convertToSwaggerSpec(OpenAPI openAPI) {
        SwaggerSpec spec = new SwaggerSpec();
        
        // Convert info
        if (openAPI.getInfo() != null) {
            SwaggerSpec.Info info = new SwaggerSpec.Info();
            info.setTitle(openAPI.getInfo().getTitle());
            info.setVersion(openAPI.getInfo().getVersion());
            info.setDescription(openAPI.getInfo().getDescription());
            spec.setInfo(info);
        }
        
        // Convert paths
        Map<String, SwaggerSpec.PathItem> paths = new HashMap<>();
        if (openAPI.getPaths() != null) {
            for (Map.Entry<String, PathItem> entry : openAPI.getPaths().entrySet()) {
                paths.put(entry.getKey(), convertPathItem(entry.getValue()));
            }
        }
        spec.setPaths(paths);
        
        // Convert components
        if (openAPI.getComponents() != null) {
            spec.setComponents(convertComponents(openAPI.getComponents()));
        }
        
        // Convert servers
        if (openAPI.getServers() != null) {
            List<SwaggerSpec.Server> servers = openAPI.getServers().stream()
                .map(this::convertServer)
                .collect(Collectors.toList());
            spec.setServers(servers);
        }
        
        return spec;
    }

    private SwaggerSpec.PathItem convertPathItem(PathItem pathItem) {
        SwaggerSpec.PathItem item = new SwaggerSpec.PathItem();
        
        if (pathItem.getGet() != null) {
            item.setGet(convertOperation(pathItem.getGet()));
        }
        if (pathItem.getPost() != null) {
            item.setPost(convertOperation(pathItem.getPost()));
        }
        if (pathItem.getPut() != null) {
            item.setPut(convertOperation(pathItem.getPut()));
        }
        if (pathItem.getDelete() != null) {
            item.setDelete(convertOperation(pathItem.getDelete()));
        }
        if (pathItem.getPatch() != null) {
            item.setPatch(convertOperation(pathItem.getPatch()));
        }
        
        return item;
    }

    private SwaggerSpec.Operation convertOperation(Operation operation) {
        SwaggerSpec.Operation op = new SwaggerSpec.Operation();
        op.setOperationId(operation.getOperationId());
        op.setSummary(operation.getSummary());
        op.setDescription(operation.getDescription());
        op.setTags(operation.getTags());
        
        // Convert parameters
        if (operation.getParameters() != null) {
            List<SwaggerSpec.Parameter> params = operation.getParameters().stream()
                .map(this::convertParameter)
                .collect(Collectors.toList());
            op.setParameters(params);
        }
        
        // Convert request body
        if (operation.getRequestBody() != null) {
            op.setRequestBody(convertRequestBody(operation.getRequestBody()));
        }
        
        // Convert responses
        if (operation.getResponses() != null) {
            Map<String, SwaggerSpec.Response> responses = new HashMap<>();
            for (Map.Entry<String, ApiResponse> entry : operation.getResponses().entrySet()) {
                responses.put(entry.getKey(), convertResponse(entry.getValue()));
            }
            op.setResponses(responses);
        }
        
        return op;
    }

    private SwaggerSpec.Parameter convertParameter(Parameter parameter) {
        SwaggerSpec.Parameter param = new SwaggerSpec.Parameter();
        param.setName(parameter.getName());
        param.setIn(parameter.getIn());
        param.setDescription(parameter.getDescription());
        param.setRequired(parameter.getRequired() != null ? parameter.getRequired() : false);
        
        if (parameter.getSchema() != null) {
            param.setSchema(convertSchema(parameter.getSchema()));
        }
        
        return param;
    }

    private SwaggerSpec.RequestBody convertRequestBody(io.swagger.v3.oas.models.parameters.RequestBody requestBody) {
        SwaggerSpec.RequestBody body = new SwaggerSpec.RequestBody();
        body.setDescription(requestBody.getDescription());
        body.setRequired(requestBody.getRequired() != null ? requestBody.getRequired() : false);
        
        if (requestBody.getContent() != null) {
            Map<String, SwaggerSpec.MediaType> content = new HashMap<>();
            for (Map.Entry<String, MediaType> entry : requestBody.getContent().entrySet()) {
                content.put(entry.getKey(), convertMediaType(entry.getValue()));
            }
            body.setContent(content);
        }
        
        return body;
    }

    private SwaggerSpec.Response convertResponse(ApiResponse apiResponse) {
        SwaggerSpec.Response response = new SwaggerSpec.Response();
        response.setDescription(apiResponse.getDescription());
        
        if (apiResponse.getContent() != null) {
            Map<String, SwaggerSpec.MediaType> content = new HashMap<>();
            for (Map.Entry<String, MediaType> entry : apiResponse.getContent().entrySet()) {
                content.put(entry.getKey(), convertMediaType(entry.getValue()));
            }
            response.setContent(content);
        }
        
        return response;
    }

    private SwaggerSpec.MediaType convertMediaType(MediaType mediaType) {
        SwaggerSpec.MediaType type = new SwaggerSpec.MediaType();
        
        if (mediaType.getSchema() != null) {
            type.setSchema(convertSchema(mediaType.getSchema()));
        }
        
        return type;
    }

    private SwaggerSpec.Schema convertSchema(Schema schema) {
        SwaggerSpec.Schema convertedSchema = new SwaggerSpec.Schema();
        convertedSchema.setType(schema.getType());
        convertedSchema.setFormat(schema.getFormat());
        convertedSchema.setDescription(schema.getDescription());
        
        if (schema.getProperties() != null) {
            Map<String, SwaggerSpec.Schema> properties = new HashMap<>();
            for (Map.Entry<String, Schema> entry : schema.getProperties().entrySet()) {
                properties.put(entry.getKey(), convertSchema(entry.getValue()));
            }
            convertedSchema.setProperties(properties);
        }
        
        if (schema.getRequired() != null) {
            convertedSchema.setRequired(schema.getRequired());
        }
        
        if (schema.getItems() != null) {
            convertedSchema.setItems(convertSchema(schema.getItems()));
        }
        
        if (schema.get$ref() != null) {
            convertedSchema.setRef(schema.get$ref());
        }
        
        return convertedSchema;
    }

    private SwaggerSpec.Components convertComponents(io.swagger.v3.oas.models.Components components) {
        SwaggerSpec.Components convertedComponents = new SwaggerSpec.Components();
        
        if (components.getSchemas() != null) {
            Map<String, SwaggerSpec.Schema> schemas = new HashMap<>();
            for (Map.Entry<String, Schema> entry : components.getSchemas().entrySet()) {
                schemas.put(entry.getKey(), convertSchema(entry.getValue()));
            }
            convertedComponents.setSchemas(schemas);
        }
        
        return convertedComponents;
    }

    private SwaggerSpec.Server convertServer(io.swagger.v3.oas.models.servers.Server server) {
        SwaggerSpec.Server convertedServer = new SwaggerSpec.Server();
        convertedServer.setUrl(server.getUrl());
        convertedServer.setDescription(server.getDescription());
        return convertedServer;
    }

    public List<ContractEndpoint> extractContractEndpoints(SwaggerSpec spec) {
        List<ContractEndpoint> endpoints = new ArrayList<>();
        
        for (Map.Entry<String, SwaggerSpec.PathItem> entry : spec.getPaths().entrySet()) {
            String path = entry.getKey();
            SwaggerSpec.PathItem pathItem = entry.getValue();
            
            if (pathItem.getGet() != null) {
                endpoints.add(createEndpoint(path, "GET", pathItem.getGet()));
            }
            if (pathItem.getPost() != null) {
                endpoints.add(createEndpoint(path, "POST", pathItem.getPost()));
            }
            if (pathItem.getPut() != null) {
                endpoints.add(createEndpoint(path, "PUT", pathItem.getPut()));
            }
            if (pathItem.getDelete() != null) {
                endpoints.add(createEndpoint(path, "DELETE", pathItem.getDelete()));
            }
            if (pathItem.getPatch() != null) {
                endpoints.add(createEndpoint(path, "PATCH", pathItem.getPatch()));
            }
        }
        
        return endpoints;
    }

    private ContractEndpoint createEndpoint(String path, String method, SwaggerSpec.Operation operation) {
        ContractEndpoint endpoint = new ContractEndpoint();
        endpoint.setPath(path);
        endpoint.setMethod(method);
        endpoint.setOperationId(operation.getOperationId());
        endpoint.setSummary(operation.getSummary());
        endpoint.setDescription(operation.getDescription());
        endpoint.setParameters(operation.getParameters());
        endpoint.setRequestBody(operation.getRequestBody());
        endpoint.setResponses(operation.getResponses());
        endpoint.setTags(operation.getTags());
        return endpoint;
    }

    public static class ContractEndpoint {
        private String path;
        private String method;
        private String operationId;
        private String summary;
        private String description;
        private List<SwaggerSpec.Parameter> parameters;
        private SwaggerSpec.RequestBody requestBody;
        private Map<String, SwaggerSpec.Response> responses;
        private List<String> tags;
        
        // Getters and setters
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        
        public String getOperationId() { return operationId; }
        public void setOperationId(String operationId) { this.operationId = operationId; }
        
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public List<SwaggerSpec.Parameter> getParameters() { return parameters; }
        public void setParameters(List<SwaggerSpec.Parameter> parameters) { this.parameters = parameters; }
        
        public SwaggerSpec.RequestBody getRequestBody() { return requestBody; }
        public void setRequestBody(SwaggerSpec.RequestBody requestBody) { this.requestBody = requestBody; }
        
        public Map<String, SwaggerSpec.Response> getResponses() { return responses; }
        public void setResponses(Map<String, SwaggerSpec.Response> responses) { this.responses = responses; }
        
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }
} 