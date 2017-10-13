# TokensApi

All URIs are relative to *https://directline.botframework.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**tokensGenerateTokenForNewConversation**](TokensApi.md#tokensGenerateTokenForNewConversation) | **POST** /v3/directline/tokens/generate | Generate a token for a new conversation
[**tokensRefreshToken**](TokensApi.md#tokensRefreshToken) | **POST** /v3/directline/tokens/refresh | Refresh a token


<a name="tokensGenerateTokenForNewConversation"></a>
# **tokensGenerateTokenForNewConversation**
> Conversation tokensGenerateTokenForNewConversation(tokenParameters)

Generate a token for a new conversation

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TokensApi;


TokensApi apiInstance = new TokensApi();
TokenParameters tokenParameters = new TokenParameters(); // TokenParameters | 
try {
    Conversation result = apiInstance.tokensGenerateTokenForNewConversation(tokenParameters);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#tokensGenerateTokenForNewConversation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tokenParameters** | [**TokenParameters**](TokenParameters.md)|  | [optional]

### Return type

[**Conversation**](Conversation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, text/json, text/html, application/xml, text/xml, application/x-www-form-urlencoded
 - **Accept**: application/json, text/json, text/html, application/xml, text/xml

<a name="tokensRefreshToken"></a>
# **tokensRefreshToken**
> Conversation tokensRefreshToken()

Refresh a token

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TokensApi;


TokensApi apiInstance = new TokensApi();
try {
    Conversation result = apiInstance.tokensRefreshToken();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#tokensRefreshToken");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Conversation**](Conversation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/json, text/html, application/xml, text/xml

