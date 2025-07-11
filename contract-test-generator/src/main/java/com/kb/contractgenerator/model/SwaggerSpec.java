package com.kb.contractgenerator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class SwaggerSpec {
    @NotBlank
    private String openapi;
    
    @NotNull
    private Info info;
    
    @NotNull
    private Map<String, PathItem> paths;
    
    private Components components;
    
    private List<Server> servers;

    public static class Info {
        private String title;
        private String version;
        private String description;
        
        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class PathItem {
        private Operation get;
        private Operation post;
        private Operation put;
        private Operation delete;
        private Operation patch;
        
        // Getters and setters
        public Operation getGet() { return get; }
        public void setGet(Operation get) { this.get = get; }
        public Operation getPost() { return post; }
        public void setPost(Operation post) { this.post = post; }
        public Operation getPut() { return put; }
        public void setPut(Operation put) { this.put = put; }
        public Operation getDelete() { return delete; }
        public void setDelete(Operation delete) { this.delete = delete; }
        public Operation getPatch() { return patch; }
        public void setPatch(Operation patch) { this.patch = patch; }
    }

    public static class Operation {
        private String operationId;
        private String summary;
        private String description;
        private List<Parameter> parameters;
        private RequestBody requestBody;
        private Map<String, Response> responses;
        private List<String> tags;
        
        // Getters and setters
        public String getOperationId() { return operationId; }
        public void setOperationId(String operationId) { this.operationId = operationId; }
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<Parameter> getParameters() { return parameters; }
        public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
        public RequestBody getRequestBody() { return requestBody; }
        public void setRequestBody(RequestBody requestBody) { this.requestBody = requestBody; }
        public Map<String, Response> getResponses() { return responses; }
        public void setResponses(Map<String, Response> responses) { this.responses = responses; }
        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }
    }

    public static class Parameter {
        private String name;
        private String in;
        private String description;
        private boolean required;
        private Schema schema;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getIn() { return in; }
        public void setIn(String in) { this.in = in; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public Schema getSchema() { return schema; }
        public void setSchema(Schema schema) { this.schema = schema; }
    }

    public static class RequestBody {
        private String description;
        private boolean required;
        private Map<String, MediaType> content;
        
        // Getters and setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public Map<String, MediaType> getContent() { return content; }
        public void setContent(Map<String, MediaType> content) { this.content = content; }
    }

    public static class Response {
        private String description;
        private Map<String, MediaType> content;
        private Map<String, Header> headers;
        
        // Getters and setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, MediaType> getContent() { return content; }
        public void setContent(Map<String, MediaType> content) { this.content = content; }
        public Map<String, Header> getHeaders() { return headers; }
        public void setHeaders(Map<String, Header> headers) { this.headers = headers; }
    }

    public static class MediaType {
        private Schema schema;
        private Map<String, Example> examples;
        
        // Getters and setters
        public Schema getSchema() { return schema; }
        public void setSchema(Schema schema) { this.schema = schema; }
        public Map<String, Example> getExamples() { return examples; }
        public void setExamples(Map<String, Example> examples) { this.examples = examples; }
    }

    public static class Schema {
        private String type;
        private String format;
        private String description;
        private Map<String, Schema> properties;
        private List<String> required;
        private Schema items;
        private String ref;
        
        @JsonProperty("$ref")
        public String getRef() { return ref; }
        public void setRef(String ref) { this.ref = ref; }
        
        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getFormat() { return format; }
        public void setFormat(String format) { this.format = format; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Map<String, Schema> getProperties() { return properties; }
        public void setProperties(Map<String, Schema> properties) { this.properties = properties; }
        public List<String> getRequired() { return required; }
        public void setRequired(List<String> required) { this.required = required; }
        public Schema getItems() { return items; }
        public void setItems(Schema items) { this.items = items; }
    }

    public static class Example {
        private String summary;
        private String description;
        private Object value;
        
        // Getters and setters
        public String getSummary() { return summary; }
        public void setSummary(String summary) { this.summary = summary; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Object getValue() { return value; }
        public void setValue(Object value) { this.value = value; }
    }

    public static class Header {
        private String description;
        private Schema schema;
        
        // Getters and setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Schema getSchema() { return schema; }
        public void setSchema(Schema schema) { this.schema = schema; }
    }

    public static class Components {
        private Map<String, Schema> schemas;
        private Map<String, Response> responses;
        private Map<String, Parameter> parameters;
        
        // Getters and setters
        public Map<String, Schema> getSchemas() { return schemas; }
        public void setSchemas(Map<String, Schema> schemas) { this.schemas = schemas; }
        public Map<String, Response> getResponses() { return responses; }
        public void setResponses(Map<String, Response> responses) { this.responses = responses; }
        public Map<String, Parameter> getParameters() { return parameters; }
        public void setParameters(Map<String, Parameter> parameters) { this.parameters = parameters; }
    }

    public static class Server {
        private String url;
        private String description;
        
        // Getters and setters
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    // Main class getters and setters
    public String getOpenapi() { return openapi; }
    public void setOpenapi(String openapi) { this.openapi = openapi; }
    public Info getInfo() { return info; }
    public void setInfo(Info info) { this.info = info; }
    public Map<String, PathItem> getPaths() { return paths; }
    public void setPaths(Map<String, PathItem> paths) { this.paths = paths; }
    public Components getComponents() { return components; }
    public void setComponents(Components components) { this.components = components; }
    public List<Server> getServers() { return servers; }
    public void setServers(List<Server> servers) { this.servers = servers; }
} 