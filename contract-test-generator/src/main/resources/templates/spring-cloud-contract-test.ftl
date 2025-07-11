import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Contract for ${endpoint.operationId}"
    
    request {
        method '${endpoint.method}'
        url '${endpoint.path}'
        <#if endpoint.requestBody??>
        body(${sampleRequestBody!'{}'})
        </#if>
        <#if customHeaders??>
        <#list customHeaders?keys as key>
        headers {
            contentType(applicationJson())
            header('${key}', '${customHeaders[key]}')
        }
        </#list>
        </#if>
        <#if endpoint.parameters??>
        <#list endpoint.parameters as param>
        <#if param.in == "path">
        urlPath('${endpoint.path}') {
            pathParameter('${param.name}', 'sample_${param.name}')
        }
        </#if>
        <#if param.in == "query">
        urlPath('${endpoint.path}') {
            queryParameters {
                parameter('${param.name}', 'sample_${param.name}')
            }
        }
        </#if>
        </#list>
        </#if>
    }
    
    response {
        status 200
        body(${sampleResponseBody!'{}'})
        headers {
            contentType(applicationJson())
        }
    }
} 