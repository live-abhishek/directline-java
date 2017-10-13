# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.ConversationsApi;

import java.io.File;
import java.util.*;

public class ConversationsApiExample {

    public static void main(String[] args) {
        
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
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://directline.botframework.com*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ConversationsApi* | [**conversationsGetActivities**](docs/ConversationsApi.md#conversationsGetActivities) | **GET** /v3/directline/conversations/{conversationId}/activities | Get activities in this conversation. This method is paged with the &#39;watermark&#39; parameter.
*ConversationsApi* | [**conversationsPostActivity**](docs/ConversationsApi.md#conversationsPostActivity) | **POST** /v3/directline/conversations/{conversationId}/activities | Send an activity
*ConversationsApi* | [**conversationsReconnectToConversation**](docs/ConversationsApi.md#conversationsReconnectToConversation) | **GET** /v3/directline/conversations/{conversationId} | Get information about an existing conversation
*ConversationsApi* | [**conversationsStartConversation**](docs/ConversationsApi.md#conversationsStartConversation) | **POST** /v3/directline/conversations | Start a new conversation
*ConversationsApi* | [**conversationsUpload**](docs/ConversationsApi.md#conversationsUpload) | **POST** /v3/directline/conversations/{conversationId}/upload | Upload file(s) and send as attachment(s)
*TokensApi* | [**tokensGenerateTokenForNewConversation**](docs/TokensApi.md#tokensGenerateTokenForNewConversation) | **POST** /v3/directline/tokens/generate | Generate a token for a new conversation
*TokensApi* | [**tokensRefreshToken**](docs/TokensApi.md#tokensRefreshToken) | **POST** /v3/directline/tokens/refresh | Refresh a token


## Documentation for Models

 - [Activity](docs/Activity.md)
 - [ActivitySet](docs/ActivitySet.md)
 - [AnimationCard](docs/AnimationCard.md)
 - [Attachment](docs/Attachment.md)
 - [AudioCard](docs/AudioCard.md)
 - [CardAction](docs/CardAction.md)
 - [CardImage](docs/CardImage.md)
 - [ChannelAccount](docs/ChannelAccount.md)
 - [Conversation](docs/Conversation.md)
 - [ConversationAccount](docs/ConversationAccount.md)
 - [ConversationReference](docs/ConversationReference.md)
 - [Entity](docs/Entity.md)
 - [Error](docs/Error.md)
 - [ErrorResponse](docs/ErrorResponse.md)
 - [Fact](docs/Fact.md)
 - [GeoCoordinates](docs/GeoCoordinates.md)
 - [HeroCard](docs/HeroCard.md)
 - [MediaUrl](docs/MediaUrl.md)
 - [Mention](docs/Mention.md)
 - [Object](docs/Object.md)
 - [Place](docs/Place.md)
 - [ReceiptCard](docs/ReceiptCard.md)
 - [ReceiptItem](docs/ReceiptItem.md)
 - [ResourceResponse](docs/ResourceResponse.md)
 - [SigninCard](docs/SigninCard.md)
 - [SuggestedActions](docs/SuggestedActions.md)
 - [Thing](docs/Thing.md)
 - [ThumbnailCard](docs/ThumbnailCard.md)
 - [ThumbnailUrl](docs/ThumbnailUrl.md)
 - [TokenParameters](docs/TokenParameters.md)
 - [VideoCard](docs/VideoCard.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

botframework@microsoft.com

