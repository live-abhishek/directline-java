/*
 * Bot Connector - Direct Line API - v3.0
 * Direct Line 3.0  ===============      The Direct Line API is a simple REST API for connecting directly to a single bot. This API is intended for developers  writing their own client applications, web chat controls, mobile apps, or service-to-service applications that will  talk to their bot.    Within the Direct Line API, you will find:    * An **authentication mechanism** using standard secret/token patterns  * The ability to **send** messages from your client to your bot via an HTTP POST message  * The ability to **receive** messages by **WebSocket** stream, if you choose  * The ability to **receive** messages by **polling HTTP GET**, if you choose  * A stable **schema**, even if your bot changes its protocol version    Direct Line 1.1 and 3.0 are both available and supported. This document describes Direct Line 3.0. For information  on Direct Line 1.1, visit the [Direct Line 1.1 reference documentation](/en-us/restapi/directline/).    # Authentication: Secrets and Tokens    Direct Line allows you to authenticate all calls with either a secret (retrieved from the Direct Line channel  configuration page) or a token (which you may get at runtime by converting your secret).    A Direct Line **secret** is a master key that can access any conversation, and create tokens. Secrets do not expire.    A Direct Line **token** is a key for a single conversation. It expires but can be refreshed.    If you're writing a service-to-service application, using the secret may be simplest. If you're writing an application  where the client runs in a web browser or mobile app, you may want to exchange your secret for a token, which only  works for a single conversation and will expire unless refreshed. You choose which security model works best for you.    Your secret or token is communicated in the ```Authorization``` header of every call, with the Bearer scheme.  Example below.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other HTTP headers, omitted]  ```    You may notice that your Direct Line client credentials are different from your bot's credentials. This is  intentional, and it allows you to revise your keys independently and lets you share client tokens without  disclosing your bot's password.     ## Exchanging a secret for a token    This operation is optional. Use this step if you want to prevent clients from accessing conversations they aren't  participating in.    To exchange a secret for a token, POST to /v3/directline/tokens/generate with your secret in the auth header  and no HTTP body.    ```  -- connect to directline.botframework.com --  POST /v3/directline/tokens/generate HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn\",    \"expires_in\": 1800  }  ```    If successful, the response is a token suitable for one conversation. The token expires in the seconds  indicated in the ```expires_in``` field (30 minutes in the example above) and must be refreshed before then to  remain useful.    This call is similar to ```/v3/directline/conversations```. The difference is that the call to  ```/v3/directline/tokens/generate``` does not start the conversation, does not contact the bot, and does not  create a streaming WebSocket URL.  * Call ```/v3/directline/conversations``` if you will distribute the token to client(s) and want them to     initiate the conversation.  * Call ```/v3/directline/conversations``` if you intend to start the conversation immediately.      ## Refreshing a token    A token may be refreshed an unlimited number of times unless it is expired.    To refresh a token, POST to /v3/directline/tokens/refresh. This method is valid only for unexpired tokens.    ```  -- connect to directline.botframework.com --  POST /v3/directline/tokens/refresh HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xniaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0\",    \"expires_in\": 1800  }  ```       # REST calls for a Direct Line conversation    Direct Line conversations are explicitly opened by clients and may run as long as the bot and client participate  (and have valid credentials). While the conversation is open, the bot and client may both send messages. More than  one client may connect to a given conversation and each client may participate on behalf of multiple users.    ## Starting a conversation    Clients begin by explicitly starting a conversation. If successful, the Direct Line service replies with a  JSON object containing a conversation ID, a token, and a WebSocket URL that may be used later.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 201 Created  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn\",    \"expires_in\": 1800,    \"streamUrl\": \"https://directline.botframework.com/v3/directline/conversations/abc123/stream?t=RCurR_XV9ZA.cwA...\"  }  ```    If the conversation was started, an HTTP 201 status code is returned. HTTP 201 is the code that clients  will receive under most circumstances, as the typical use case is for a client to start a new conversation.  Under certain conditions -- specifically, when the client has a token scoped to a single conversation AND  when that conversation was started with a prior call to this URL -- this method will return HTTP 200 to signify  the request was acceptable but that no conversation was created (as it already existed).    You have 60 seconds to connect to the WebSocket URL. If the connection cannot be established during this time,  use the reconnect method below to generate a new stream URL.    This call is similar to ```/v3/directline/tokens/generate```. The difference is that the call to  ```/v3/directline/conversations``` starts the conversation, contacts the bot, and creates a streaming WebSocket  URL, none of which occur when generating a token.  * Call ```/v3/directline/conversations``` if you will distribute the token to client(s) and want them to    initiate the conversation.  * Call ```/v3/directline/conversations``` if you intend to start the conversation immediately.    ## Reconnecting to a conversation    If a client is using the WebSocket interface to receive messages but loses its connection, it may need to reconnect.  Reconnecting requires generating a new WebSocket stream URL, and this can be accomplished by sending a GET request  to the ```/v3/directline/conversations/{id}``` endpoint.    The ```watermark``` parameter is optional. If supplied, the conversation replays from the watermark,  guaranteeing no messages are lost. If ```watermark``` is omitted, only messages received after the reconnection  call (```GET /v3/directline/conversations/abc123```) are replayed.    ```  -- connect to directline.botframework.com --  GET /v3/directline/conversations/abc123?watermark=0000a-42 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"conversationId\": \"abc123\",    \"token\": \"RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0y8qbOF5xPGfiCpg4Fv0y8qqbOF5x8qbOF5xn\",    \"streamUrl\": \"https://directline.botframework.com/v3/directline/conversations/abc123/stream?watermark=000a-4&t=RCurR_XV9ZA.cwA...\"  }  ```    You have 60 seconds to connect to the WebSocket stream URL. If the connection cannot be established during this  time, issue another reconnect request to get an updated stream URL.    ## Sending an Activity to the bot    Using the Direct Line 3.0 protocol, clients and bots may exchange many different Bot Framework v3 Activites,  including Message Activities, Typing Activities, and custom activities that the bot supports.    To send any one of these activities to the bot,    1. the client formulates the Activity according to the Activity schema (see below)  2. the client issues a POST message to ```/v3/directline/conversations/{id}/activities```  3. the service returns when the activity was delivered to the bot, with an HTTP status code reflecting the     bot's status code. If the POST was successful, the service returns a JSON payload containing the ID of the     Activity that was sent.    Example follows.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    {    \"type\": \"message\",    \"from\": {      \"id\": \"user1\"    },    \"text\": \"hello\"  }    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0001\"  }  ```    The client's Activity is available in the message retrieval path (either polling GET or WebSocket) and is not  returned inline.    The total time to POST a message to a Direct Line conversation is:    * Transit time to the Direct Line service,  * Internal processing time within Direct Line (typically less than 120ms)  * Transit time to the bot  * Processing time within the bot  * Transit time for HTTP responses to travel back to the client.    If the bot generates an error, that error will trigger an HTTP 502 error (\"Bad Gateway\") in  the ```POST /v3/directline/conversations/{id}/activities``` call.    ### Sending one or more attachments by URL    Clients may optionally send attachments, such as images or documents. If the client already has a URL for the  attachment, the simplest way to send it is to include the URL in the ```contentUrl``` field of an Activity  attachment object. This applies to HTTP, HTTPS, and ```data:``` URIs.    ### Sending a single attachment by upload    Often, clients have an image or document on a device but no URL that can be included in the activity.    To upload an attachment, POST a single attachment to  the ```/v3/directline/conversations/{conversationId}/upload``` endpoint. The ```Content-Type```  and ```Content-Disposition``` headers control the attachment's type and filename, respectively.    A user ID is required. Supply the ID of the user sending the attachment as a ```userId``` parameter in the URL.    If uploading a single attachment, a message activity is sent to the bot when the upload completes.    On completion, the service returns the ID of the activity that was sent.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/upload?userId=user1 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  Content-Type: image/jpeg  Content-Disposition: name=\"file\"; filename=\"badjokeeel.jpg\"  [other headers]    [JPEG content]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0003\"  }  ```    ### Sending multiple attachments by upload    If uploading multiple attachments, use ```multipart/form-data``` as the content type and include each  attachment as a separate part. Each attachment's type and filename may be included in the ```Content-Type```  and ```Content-Disposition``` headers in each part.    An activity may be included by adding a part with content type of ```application/vnd.microsoft.activity```.  Other parts in the payload are attached to this activity before it is sent. If an Activity is not included,  an empty Activity is created as a wrapper for the attachments.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/upload?userId=user1 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  Content-Type: multipart/form-data; boundary=----DD4E5147-E865-4652-B662-F223701A8A89  [other headers]    ----DD4E5147-E865-4652-B662-F223701A8A89  Content-Type: image/jpeg  Content-Disposition: form-data; name=\"file\"; filename=\"badjokeeel.jpg\"  [other headers]    [JPEG content]    ----DD4E5147-E865-4652-B662-F223701A8A89  Content-Type: application/vnd.microsoft.activity  [other headers]    {    \"type\": \"message\",    \"from\": {      \"id\": \"user1\"    },    \"text\": \"Hey I just IM'd you\\n\\nand this is crazy\\n\\nbut here's my webhook\\n\\nso POST me maybe\"  }    ----DD4E5147-E865-4652-B662-F223701A8A89            -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0004\"  }  ```    ## Receiving Activities from the bot    Direct Line 3.0 clients may choose from two different mechanisms for retrieving messages:    1. A **streaming WebSocket**, which pushes messages efficiently to clients.  2. A **polling GET** interface, which is available for clients unable to use WebSockets or for clients     retrieving the conversation history.    **Not all activities are available via the polling GET interface.** A table of activity availability follows.    |Activity type|Availability|  |-------------|--------|  |Message|Polling GET and WebSocket|  |Typing|WebSocket only|  |ConversationUpdate|Not sent/received via client|  |ContactRelationUpdate|Not supported in Direct Line|  |EndOfConversation|Polling GET and WebSocket|  |All other activity types|Polling GET and WebSocket|    ### Receiving Activities by WebSocket    To connect via WebSocket, a client uses the StreamUrl when starting a conversation. The stream URL is  preauthorized and does NOT require an Authorization header containing the client's secret or token.    ```  -- connect to wss://directline.botframework.com --  GET /v3/directline/conversations/abc123/stream?t=RCurR_XV9ZA.cwA...\" HTTP/1.1  Upgrade: websocket  Connection: upgrade  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 101 Switching Protocols  [other headers]  ```    The Direct Line service sends the following messages:    * An **ActivitySet**, which contains one or more activities and a watermark (described below)  * An empty message, which the Direct Line service uses to ensure the connection is still valid  * Additional types, to be defined later. These types are identified by the properties in the JSON root.    ActivitySets contain messages sent by the bot and by all users. Example ActivitySet:    ```  {    \"activities\": [{      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0000\",      \"from\": {        \"id\": \"user1\"      },      \"text\": \"hello\"    }],    \"watermark\": \"0000a-42\"  }  ```    Clients should keep track of the \"watermark\" value from each ActivitySet so they can use it on reconnect.  **Note** that a ```null``` or missing watermark should be ignored and should not overwrite a prior watermark  in the client.    Clients should ignore empty messages.    Clients may send their own empty messages to verify connectivity. The Direct Line service will ignore these.    The service may forcibly close the connection under certain conditions. If the client has not received an  EndOfConversation activity, it may reconnect by issuing a GET request to the conversation endpoint to get a  new stream URL (see above).    The WebSocket stream contains live updates and very recent messages (since the call to get the WebSocket call  was issued) but it does not include messages sent prior to the most recent POST  to ```/v3/directline/conversations/{id}```. To retrieve messages sent earlier in the conversation, use the  GET mechanism below.    ### Receiving Activities by GET    The GET mechanism is useful for clients who are unable to use the WebSocket, or for clients wishing to retrieve  the conversation history.    To retrieve messages, issue a GET call to the conversation endpoint. Optionally supply a watermark, indicating  the most recent message seen. The watermark field accompanies all GET/WebSocket messages as a property in the  ActivitySet.    ```  -- connect to directline.botframework.com --  GET /v3/directline/conversations/abc123/activities?watermark=0001a-94 HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"activities\": [{      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0000\",      \"from\": {        \"id\": \"user1\"      },      \"text\": \"hello\"    }, {      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0001\",      \"from\": {        \"id\": \"bot1\"      },      \"text\": \"Nice to see you, user1!\"    }],    \"watermark\": \"0001a-95\"  }  ```    Clients should page through the available activities by advancing the ```watermark``` value until no activities  are returned.      ### Timing considerations     Most clients wish to retain a complete message history. Even though Direct Line is a multi-part protocol with  potential timing gaps, the protocol and service is designed to make it easy to build a reliable client.    1. The ```watermark``` field sent in the WebSocket stream and GET response is reliable. You will not miss     messages as long as you replay the watermark verbatim.  2. When starting a conversation and connecting to the WebSocket stream, any Activities sent after the POST but     before the socket is opened are replayed before new messages.  3. When refreshing history by GET call while connected to the WebSocket, Activities may be duplicated across both     channels. Keeping a list of all known Activity IDs will allow you to reject duplicate messages should they occur.    Clients using the polling GET interface should choose a polling interval that matches their intended use.    * Service-to-service applications often use a polling interval of 5s or 10s.  * Client-facing applications often use a polling interval of 1s, and fire an additional request ~300ms after    every message the client sends to rapidly pick up a bot's response. This 300ms delay should be adjusted    based on the bot's speed and transit time.    ## Ending a conversation    Either a client or a bot may signal the end of a DirectLine conversation. This operation halts communication  and prevents the bot and the client from sending messages. Messages may still be retrieved via the GET mechanism.  Sending this messages is as simple as POSTing an EndOfConversation activity.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  Authorization: Bearer RCurR_XV9ZA.cwA.BKA.iaJrC8xpy8qbOF5xnR2vtCX7CZj0LdjAPGfiCpg4Fv0  [other headers]    {    \"type\": \"endOfConversation\",    \"from\": {      \"id\": \"user1\"    }  }    -- response from directline.botframework.com --  HTTP/1.1 200 OK  [other headers]    {    \"id\": \"0004\"  }  ```    ## REST API errors    HTTP calls to the Direct Line service follow standard HTTP error conventions:    * 2xx status codes indicate success. (Direct Line 3.0 uses 200 and 201.)  * 4xx status codes indicate an error in your request.    * 401 indicates a missing or malformed Authorization header (or URL token, in calls where a token parameter      is allowed).    * 403 indicates an unauthorized client.      * If calling with a valid but expired token, the ```code``` field is set to ```TokenExpired```.    * 404 indicates a missing path, site, conversation, etc.  * 5xx status codes indicate a service-side error.    * 500 indicates an error inside the Direct Line service.    * 502 indicates an error was returned by the bot. **This is a common error code.**  * 101 is used in the WebSocket connection path, although this is likely handled by your WebSocket client.    When an error message is returned, error detail may be present in a JSON response. Look for an ```error```  property with ```code``` and ```message``` fields.    ```  -- connect to directline.botframework.com --  POST /v3/directline/conversations/abc123/activities HTTP/1.1  [detail omitted]    -- response from directline.botframework.com --  HTTP/1.1 502 Bad Gateway  [other headers]    {    \"error\": {      \"code\": \"BotRejectedActivity\",      \"message\": \"Failed to send activity: bot returned an error\"    }  }  ```    The contents of the ```message``` field may change. The HTTP status code and values in the ```code```  property are stable.    # Schema    The Direct Line 3.0 schema is identical to the Bot Framework v3 schema.    When a bot sends an Activity to a client through Direct Line:    * attachment cards are preserved,  * URLs for uploaded attachments are hidden with a private link, and  * the ```channelData``` property is preserved without modification.    When a client sends an Activity to a bot through Direct Line:    * the ```type``` property contains the kind of activity you are sending (typically ```message```),  * the ```from``` property must be populated with a user ID, chosen by your client,  * attachments may contain URLs to existing resources or URLs uploaded through the Direct Line attachment    endpoint, and  * the ```channelData``` property is preserved without modification.    Clients and bots may send Activities of any type, including Message Activities, Typing Activities, and  custom Activity types.    Clients may send a single Activity at a time.    ```  {    \"type\": \"message\",    \"channelId\": \"directline\",    \"from\": {      \"id\": \"user1\"    },    \"text\": \"hello\"  }  ```    Clients receive multiple Activities as part of an ActivitySet. The ActivitySet has an array of activities  and a watermark field.    ```  {    \"activities\": [{      \"type\": \"message\",      \"channelId\": \"directline\",      \"conversation\": {        \"id\": \"abc123\"      },      \"id\": \"abc123|0000\",      \"from\": {        \"id\": \"user1\"      },      \"text\": \"hello\"    }],    \"watermark\": \"0000a-42\"  }  ```    # Libraries for the Direct Line API    The Direct Line API is designed to be coded directly, but the Bot Framework includes libraries and controls that  help you to embed Direct-Line-powered bots into your application.    * The [Bot Framework Web Chat control](https://github.com/Microsoft/BotFramework-WebChat) is an easy way to embed    the Direct Line protocol into a webpage.  * [Direct Line Nuget package](https://www.nuget.org/packages/Microsoft.Bot.Connector.DirectLine) with libraries for    .Net 4.5, UWP, and .Net Standard.  * [DirectLineJs](https://github.com/Microsoft/BotFramework-DirectLineJs), also available on    [NPM](https://www.npmjs.com/package/botframework-directlinejs)  * You may generate your own from the [Direct Line Swagger file](swagger.json)    Our [BotBuilder-Samples GitHub repo](https://github.com/Microsoft/BotBuilder-Samples) also contains samples for    [C#](https://github.com/Microsoft/BotBuilder-Samples/tree/master/CSharp/core-DirectLine) and    [JavaScript](https://github.com/Microsoft/BotBuilder-Samples/tree/master/Node/core-DirectLine).
 *
 * OpenAPI spec version: v3
 * Contact: botframework@microsoft.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.model;

import java.util.Objects;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.Attachment;
import io.swagger.client.model.ChannelAccount;
import io.swagger.client.model.ConversationAccount;
import io.swagger.client.model.ConversationReference;
import io.swagger.client.model.Entity;
import io.swagger.client.model.SuggestedActions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * An Activity is the basic communication type for the Bot Framework 3.0 protocol
 */
@ApiModel(description = "An Activity is the basic communication type for the Bot Framework 3.0 protocol")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-09-27T09:29:02.538Z")
public class Activity {
  @SerializedName("type")
  private String type = null;

  @SerializedName("id")
  private String id = null;

  @SerializedName("timestamp")
  private DateTime timestamp = null;

  @SerializedName("localTimestamp")
  private DateTime localTimestamp = null;

  @SerializedName("serviceUrl")
  private String serviceUrl = null;

  @SerializedName("channelId")
  private String channelId = null;

  @SerializedName("from")
  private ChannelAccount from = null;

  @SerializedName("conversation")
  private ConversationAccount conversation = null;

  @SerializedName("recipient")
  private ChannelAccount recipient = null;

  @SerializedName("textFormat")
  private String textFormat = null;

  @SerializedName("attachmentLayout")
  private String attachmentLayout = null;

  @SerializedName("membersAdded")
  private List<ChannelAccount> membersAdded = null;

  @SerializedName("membersRemoved")
  private List<ChannelAccount> membersRemoved = null;

  @SerializedName("topicName")
  private String topicName = null;

  @SerializedName("historyDisclosed")
  private Boolean historyDisclosed = null;

  @SerializedName("locale")
  private String locale = null;

  @SerializedName("text")
  private String text = null;

  @SerializedName("speak")
  private String speak = null;

  @SerializedName("inputHint")
  private String inputHint = null;

  @SerializedName("summary")
  private String summary = null;

  @SerializedName("suggestedActions")
  private SuggestedActions suggestedActions = null;

  @SerializedName("attachments")
  private List<Attachment> attachments = null;

  @SerializedName("entities")
  private List<Entity> entities = null;

  @SerializedName("channelData")
  private Object channelData = null;

  @SerializedName("action")
  private String action = null;

  @SerializedName("replyToId")
  private String replyToId = null;

  @SerializedName("value")
  private Object value = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("relatesTo")
  private ConversationReference relatesTo = null;

  @SerializedName("code")
  private String code = null;

  public Activity type(String type) {
    this.type = type;
    return this;
  }

   /**
   * The type of the activity [message|contactRelationUpdate|converationUpdate|typing|endOfConversation|event|invoke]
   * @return type
  **/
  @ApiModelProperty(value = "The type of the activity [message|contactRelationUpdate|converationUpdate|typing|endOfConversation|event|invoke]")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Activity id(String id) {
    this.id = id;
    return this;
  }

   /**
   * ID of this activity
   * @return id
  **/
  @ApiModelProperty(value = "ID of this activity")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Activity timestamp(DateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

   /**
   * UTC Time when message was sent (set by service)
   * @return timestamp
  **/
  @ApiModelProperty(value = "UTC Time when message was sent (set by service)")
  public DateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(DateTime timestamp) {
    this.timestamp = timestamp;
  }

  public Activity localTimestamp(DateTime localTimestamp) {
    this.localTimestamp = localTimestamp;
    return this;
  }

   /**
   * Local time when message was sent (set by client, Ex: 2016-09-23T13:07:49.4714686-07:00)
   * @return localTimestamp
  **/
  @ApiModelProperty(value = "Local time when message was sent (set by client, Ex: 2016-09-23T13:07:49.4714686-07:00)")
  public DateTime getLocalTimestamp() {
    return localTimestamp;
  }

  public void setLocalTimestamp(DateTime localTimestamp) {
    this.localTimestamp = localTimestamp;
  }

  public Activity serviceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
    return this;
  }

   /**
   * Service endpoint where operations concerning the activity may be performed
   * @return serviceUrl
  **/
  @ApiModelProperty(value = "Service endpoint where operations concerning the activity may be performed")
  public String getServiceUrl() {
    return serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  public Activity channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

   /**
   * ID of the channel where the activity was sent
   * @return channelId
  **/
  @ApiModelProperty(value = "ID of the channel where the activity was sent")
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public Activity from(ChannelAccount from) {
    this.from = from;
    return this;
  }

   /**
   * Sender address
   * @return from
  **/
  @ApiModelProperty(value = "Sender address")
  public ChannelAccount getFrom() {
    return from;
  }

  public void setFrom(ChannelAccount from) {
    this.from = from;
  }

  public Activity conversation(ConversationAccount conversation) {
    this.conversation = conversation;
    return this;
  }

   /**
   * Conversation
   * @return conversation
  **/
  @ApiModelProperty(value = "Conversation")
  public ConversationAccount getConversation() {
    return conversation;
  }

  public void setConversation(ConversationAccount conversation) {
    this.conversation = conversation;
  }

  public Activity recipient(ChannelAccount recipient) {
    this.recipient = recipient;
    return this;
  }

   /**
   * (Outbound to bot only) Bot&#39;s address that received the message
   * @return recipient
  **/
  @ApiModelProperty(value = "(Outbound to bot only) Bot's address that received the message")
  public ChannelAccount getRecipient() {
    return recipient;
  }

  public void setRecipient(ChannelAccount recipient) {
    this.recipient = recipient;
  }

  public Activity textFormat(String textFormat) {
    this.textFormat = textFormat;
    return this;
  }

   /**
   * Format of text fields [plain|markdown] Default:markdown
   * @return textFormat
  **/
  @ApiModelProperty(value = "Format of text fields [plain|markdown] Default:markdown")
  public String getTextFormat() {
    return textFormat;
  }

  public void setTextFormat(String textFormat) {
    this.textFormat = textFormat;
  }

  public Activity attachmentLayout(String attachmentLayout) {
    this.attachmentLayout = attachmentLayout;
    return this;
  }

   /**
   * Hint for how to deal with multiple attachments: [list|carousel] Default:list
   * @return attachmentLayout
  **/
  @ApiModelProperty(value = "Hint for how to deal with multiple attachments: [list|carousel] Default:list")
  public String getAttachmentLayout() {
    return attachmentLayout;
  }

  public void setAttachmentLayout(String attachmentLayout) {
    this.attachmentLayout = attachmentLayout;
  }

  public Activity membersAdded(List<ChannelAccount> membersAdded) {
    this.membersAdded = membersAdded;
    return this;
  }

  public Activity addMembersAddedItem(ChannelAccount membersAddedItem) {
    if (this.membersAdded == null) {
      this.membersAdded = new ArrayList<ChannelAccount>();
    }
    this.membersAdded.add(membersAddedItem);
    return this;
  }

   /**
   * Array of address added
   * @return membersAdded
  **/
  @ApiModelProperty(value = "Array of address added")
  public List<ChannelAccount> getMembersAdded() {
    return membersAdded;
  }

  public void setMembersAdded(List<ChannelAccount> membersAdded) {
    this.membersAdded = membersAdded;
  }

  public Activity membersRemoved(List<ChannelAccount> membersRemoved) {
    this.membersRemoved = membersRemoved;
    return this;
  }

  public Activity addMembersRemovedItem(ChannelAccount membersRemovedItem) {
    if (this.membersRemoved == null) {
      this.membersRemoved = new ArrayList<ChannelAccount>();
    }
    this.membersRemoved.add(membersRemovedItem);
    return this;
  }

   /**
   * Array of addresses removed
   * @return membersRemoved
  **/
  @ApiModelProperty(value = "Array of addresses removed")
  public List<ChannelAccount> getMembersRemoved() {
    return membersRemoved;
  }

  public void setMembersRemoved(List<ChannelAccount> membersRemoved) {
    this.membersRemoved = membersRemoved;
  }

  public Activity topicName(String topicName) {
    this.topicName = topicName;
    return this;
  }

   /**
   * Conversations new topic name
   * @return topicName
  **/
  @ApiModelProperty(value = "Conversations new topic name")
  public String getTopicName() {
    return topicName;
  }

  public void setTopicName(String topicName) {
    this.topicName = topicName;
  }

  public Activity historyDisclosed(Boolean historyDisclosed) {
    this.historyDisclosed = historyDisclosed;
    return this;
  }

   /**
   * True if the previous history of the channel is disclosed
   * @return historyDisclosed
  **/
  @ApiModelProperty(value = "True if the previous history of the channel is disclosed")
  public Boolean getHistoryDisclosed() {
    return historyDisclosed;
  }

  public void setHistoryDisclosed(Boolean historyDisclosed) {
    this.historyDisclosed = historyDisclosed;
  }

  public Activity locale(String locale) {
    this.locale = locale;
    return this;
  }

   /**
   * The language code of the Text field
   * @return locale
  **/
  @ApiModelProperty(value = "The language code of the Text field")
  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public Activity text(String text) {
    this.text = text;
    return this;
  }

   /**
   * Content for the message
   * @return text
  **/
  @ApiModelProperty(value = "Content for the message")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Activity speak(String speak) {
    this.speak = speak;
    return this;
  }

   /**
   * SSML Speak for TTS audio response
   * @return speak
  **/
  @ApiModelProperty(value = "SSML Speak for TTS audio response")
  public String getSpeak() {
    return speak;
  }

  public void setSpeak(String speak) {
    this.speak = speak;
  }

  public Activity inputHint(String inputHint) {
    this.inputHint = inputHint;
    return this;
  }

   /**
   * Indicates whether the bot is accepting, expecting, or ignoring input
   * @return inputHint
  **/
  @ApiModelProperty(value = "Indicates whether the bot is accepting, expecting, or ignoring input")
  public String getInputHint() {
    return inputHint;
  }

  public void setInputHint(String inputHint) {
    this.inputHint = inputHint;
  }

  public Activity summary(String summary) {
    this.summary = summary;
    return this;
  }

   /**
   * Text to display if the channel cannot render cards
   * @return summary
  **/
  @ApiModelProperty(value = "Text to display if the channel cannot render cards")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public Activity suggestedActions(SuggestedActions suggestedActions) {
    this.suggestedActions = suggestedActions;
    return this;
  }

   /**
   * SuggestedActions are used to provide keyboard/quickreply like behavior in many clients
   * @return suggestedActions
  **/
  @ApiModelProperty(value = "SuggestedActions are used to provide keyboard/quickreply like behavior in many clients")
  public SuggestedActions getSuggestedActions() {
    return suggestedActions;
  }

  public void setSuggestedActions(SuggestedActions suggestedActions) {
    this.suggestedActions = suggestedActions;
  }

  public Activity attachments(List<Attachment> attachments) {
    this.attachments = attachments;
    return this;
  }

  public Activity addAttachmentsItem(Attachment attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<Attachment>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

   /**
   * Attachments
   * @return attachments
  **/
  @ApiModelProperty(value = "Attachments")
  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }

  public Activity entities(List<Entity> entities) {
    this.entities = entities;
    return this;
  }

  public Activity addEntitiesItem(Entity entitiesItem) {
    if (this.entities == null) {
      this.entities = new ArrayList<Entity>();
    }
    this.entities.add(entitiesItem);
    return this;
  }

   /**
   * Collection of Entity objects, each of which contains metadata about this activity. Each Entity object is typed.
   * @return entities
  **/
  @ApiModelProperty(value = "Collection of Entity objects, each of which contains metadata about this activity. Each Entity object is typed.")
  public List<Entity> getEntities() {
    return entities;
  }

  public void setEntities(List<Entity> entities) {
    this.entities = entities;
  }

  public Activity channelData(Object channelData) {
    this.channelData = channelData;
    return this;
  }

   /**
   * Channel-specific payload
   * @return channelData
  **/
  @ApiModelProperty(value = "Channel-specific payload")
  public Object getChannelData() {
    return channelData;
  }

  public void setChannelData(Object channelData) {
    this.channelData = channelData;
  }

  public Activity action(String action) {
    this.action = action;
    return this;
  }

   /**
   * ContactAdded/Removed action
   * @return action
  **/
  @ApiModelProperty(value = "ContactAdded/Removed action")
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Activity replyToId(String replyToId) {
    this.replyToId = replyToId;
    return this;
  }

   /**
   * The original ID this message is a response to
   * @return replyToId
  **/
  @ApiModelProperty(value = "The original ID this message is a response to")
  public String getReplyToId() {
    return replyToId;
  }

  public void setReplyToId(String replyToId) {
    this.replyToId = replyToId;
  }

  public Activity value(Object value) {
    this.value = value;
    return this;
  }

   /**
   * Open-ended value
   * @return value
  **/
  @ApiModelProperty(value = "Open-ended value")
  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Activity name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Name of the operation to invoke or the name of the event
   * @return name
  **/
  @ApiModelProperty(value = "Name of the operation to invoke or the name of the event")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Activity relatesTo(ConversationReference relatesTo) {
    this.relatesTo = relatesTo;
    return this;
  }

   /**
   * Reference to another conversation or activity
   * @return relatesTo
  **/
  @ApiModelProperty(value = "Reference to another conversation or activity")
  public ConversationReference getRelatesTo() {
    return relatesTo;
  }

  public void setRelatesTo(ConversationReference relatesTo) {
    this.relatesTo = relatesTo;
  }

  public Activity code(String code) {
    this.code = code;
    return this;
  }

   /**
   * Code indicating why the conversation has ended
   * @return code
  **/
  @ApiModelProperty(value = "Code indicating why the conversation has ended")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Activity activity = (Activity) o;
    return Objects.equals(this.type, activity.type) &&
        Objects.equals(this.id, activity.id) &&
        Objects.equals(this.timestamp, activity.timestamp) &&
        Objects.equals(this.localTimestamp, activity.localTimestamp) &&
        Objects.equals(this.serviceUrl, activity.serviceUrl) &&
        Objects.equals(this.channelId, activity.channelId) &&
        Objects.equals(this.from, activity.from) &&
        Objects.equals(this.conversation, activity.conversation) &&
        Objects.equals(this.recipient, activity.recipient) &&
        Objects.equals(this.textFormat, activity.textFormat) &&
        Objects.equals(this.attachmentLayout, activity.attachmentLayout) &&
        Objects.equals(this.membersAdded, activity.membersAdded) &&
        Objects.equals(this.membersRemoved, activity.membersRemoved) &&
        Objects.equals(this.topicName, activity.topicName) &&
        Objects.equals(this.historyDisclosed, activity.historyDisclosed) &&
        Objects.equals(this.locale, activity.locale) &&
        Objects.equals(this.text, activity.text) &&
        Objects.equals(this.speak, activity.speak) &&
        Objects.equals(this.inputHint, activity.inputHint) &&
        Objects.equals(this.summary, activity.summary) &&
        Objects.equals(this.suggestedActions, activity.suggestedActions) &&
        Objects.equals(this.attachments, activity.attachments) &&
        Objects.equals(this.entities, activity.entities) &&
        Objects.equals(this.channelData, activity.channelData) &&
        Objects.equals(this.action, activity.action) &&
        Objects.equals(this.replyToId, activity.replyToId) &&
        Objects.equals(this.value, activity.value) &&
        Objects.equals(this.name, activity.name) &&
        Objects.equals(this.relatesTo, activity.relatesTo) &&
        Objects.equals(this.code, activity.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, id, timestamp, localTimestamp, serviceUrl, channelId, from, conversation, recipient, textFormat, attachmentLayout, membersAdded, membersRemoved, topicName, historyDisclosed, locale, text, speak, inputHint, summary, suggestedActions, attachments, entities, channelData, action, replyToId, value, name, relatesTo, code);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Activity {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    localTimestamp: ").append(toIndentedString(localTimestamp)).append("\n");
    sb.append("    serviceUrl: ").append(toIndentedString(serviceUrl)).append("\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    conversation: ").append(toIndentedString(conversation)).append("\n");
    sb.append("    recipient: ").append(toIndentedString(recipient)).append("\n");
    sb.append("    textFormat: ").append(toIndentedString(textFormat)).append("\n");
    sb.append("    attachmentLayout: ").append(toIndentedString(attachmentLayout)).append("\n");
    sb.append("    membersAdded: ").append(toIndentedString(membersAdded)).append("\n");
    sb.append("    membersRemoved: ").append(toIndentedString(membersRemoved)).append("\n");
    sb.append("    topicName: ").append(toIndentedString(topicName)).append("\n");
    sb.append("    historyDisclosed: ").append(toIndentedString(historyDisclosed)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    speak: ").append(toIndentedString(speak)).append("\n");
    sb.append("    inputHint: ").append(toIndentedString(inputHint)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    suggestedActions: ").append(toIndentedString(suggestedActions)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
    sb.append("    channelData: ").append(toIndentedString(channelData)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    replyToId: ").append(toIndentedString(replyToId)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    relatesTo: ").append(toIndentedString(relatesTo)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

