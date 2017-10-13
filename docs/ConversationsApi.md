# ConversationsApi

All URIs are relative to *https://directline.botframework.com*

Method | HTTP request | Description
------------- | ------------- | -------------
[**conversationsGetActivities**](ConversationsApi.md#conversationsGetActivities) | **GET** /v3/directline/conversations/{conversationId}/activities | Get activities in this conversation. This method is paged with the &#39;watermark&#39; parameter.
[**conversationsPostActivity**](ConversationsApi.md#conversationsPostActivity) | **POST** /v3/directline/conversations/{conversationId}/activities | Send an activity
[**conversationsReconnectToConversation**](ConversationsApi.md#conversationsReconnectToConversation) | **GET** /v3/directline/conversations/{conversationId} | Get information about an existing conversation
[**conversationsStartConversation**](ConversationsApi.md#conversationsStartConversation) | **POST** /v3/directline/conversations | Start a new conversation
[**conversationsUpload**](ConversationsApi.md#conversationsUpload) | **POST** /v3/directline/conversations/{conversationId}/upload | Upload file(s) and send as attachment(s)


<a name="conversationsGetActivities"></a>
# **conversationsGetActivities**
> ActivitySet conversationsGetActivities(conversationId, watermark)

Get activities in this conversation. This method is paged with the &#39;watermark&#39; parameter.

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConversationsApi;


ConversationsApi apiInstance = new ConversationsApi();
String conversationId = "conversationId_example"; // String | Conversation ID
String watermark = "watermark_example"; // String | (Optional) only returns activities newer than this watermark
try {
    ActivitySet result = apiInstance.conversationsGetActivities(conversationId, watermark);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConversationsApi#conversationsGetActivities");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conversationId** | **String**| Conversation ID |
 **watermark** | **String**| (Optional) only returns activities newer than this watermark | [optional]

### Return type

[**ActivitySet**](ActivitySet.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/json, text/html, application/xml, text/xml

<a name="conversationsPostActivity"></a>
# **conversationsPostActivity**
> ResourceResponse conversationsPostActivity(conversationId, activity)

Send an activity

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConversationsApi;


ConversationsApi apiInstance = new ConversationsApi();
String conversationId = "conversationId_example"; // String | Conversation ID
Activity activity = new Activity(); // Activity | Activity to send
try {
    ResourceResponse result = apiInstance.conversationsPostActivity(conversationId, activity);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConversationsApi#conversationsPostActivity");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conversationId** | **String**| Conversation ID |
 **activity** | [**Activity**](Activity.md)| Activity to send |

### Return type

[**ResourceResponse**](ResourceResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json, text/json, text/html, application/xml, text/xml, application/x-www-form-urlencoded
 - **Accept**: application/json, text/json, text/html

<a name="conversationsReconnectToConversation"></a>
# **conversationsReconnectToConversation**
> Conversation conversationsReconnectToConversation(conversationId, watermark)

Get information about an existing conversation

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConversationsApi;


ConversationsApi apiInstance = new ConversationsApi();
String conversationId = "conversationId_example"; // String | 
String watermark = "watermark_example"; // String | 
try {
    Conversation result = apiInstance.conversationsReconnectToConversation(conversationId, watermark);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConversationsApi#conversationsReconnectToConversation");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conversationId** | **String**|  |
 **watermark** | **String**|  | [optional]

### Return type

[**Conversation**](Conversation.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/json, text/html, application/xml, text/xml

<a name="conversationsStartConversation"></a>
# **conversationsStartConversation**
> Conversation conversationsStartConversation()

Start a new conversation

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConversationsApi;


ConversationsApi apiInstance = new ConversationsApi();
try {
    Conversation result = apiInstance.conversationsStartConversation();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConversationsApi#conversationsStartConversation");
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

<a name="conversationsUpload"></a>
# **conversationsUpload**
> ResourceResponse conversationsUpload(conversationId, file, userId)

Upload file(s) and send as attachment(s)

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConversationsApi;


ConversationsApi apiInstance = new ConversationsApi();
String conversationId = "conversationId_example"; // String | 
File file = new File("/path/to/file.txt"); // File | 
String userId = "userId_example"; // String | 
try {
    ResourceResponse result = apiInstance.conversationsUpload(conversationId, file, userId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConversationsApi#conversationsUpload");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **conversationId** | **String**|  |
 **file** | **File**|  |
 **userId** | **String**|  | [optional]

### Return type

[**ResourceResponse**](ResourceResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json, text/json, text/html

