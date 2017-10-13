
# Activity

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **String** | The type of the activity [message|contactRelationUpdate|converationUpdate|typing|endOfConversation|event|invoke] |  [optional]
**id** | **String** | ID of this activity |  [optional]
**timestamp** | [**DateTime**](DateTime.md) | UTC Time when message was sent (set by service) |  [optional]
**localTimestamp** | [**DateTime**](DateTime.md) | Local time when message was sent (set by client, Ex: 2016-09-23T13:07:49.4714686-07:00) |  [optional]
**serviceUrl** | **String** | Service endpoint where operations concerning the activity may be performed |  [optional]
**channelId** | **String** | ID of the channel where the activity was sent |  [optional]
**from** | [**ChannelAccount**](ChannelAccount.md) | Sender address |  [optional]
**conversation** | [**ConversationAccount**](ConversationAccount.md) | Conversation |  [optional]
**recipient** | [**ChannelAccount**](ChannelAccount.md) | (Outbound to bot only) Bot&#39;s address that received the message |  [optional]
**textFormat** | **String** | Format of text fields [plain|markdown] Default:markdown |  [optional]
**attachmentLayout** | **String** | Hint for how to deal with multiple attachments: [list|carousel] Default:list |  [optional]
**membersAdded** | [**List&lt;ChannelAccount&gt;**](ChannelAccount.md) | Array of address added |  [optional]
**membersRemoved** | [**List&lt;ChannelAccount&gt;**](ChannelAccount.md) | Array of addresses removed |  [optional]
**topicName** | **String** | Conversations new topic name |  [optional]
**historyDisclosed** | **Boolean** | True if the previous history of the channel is disclosed |  [optional]
**locale** | **String** | The language code of the Text field |  [optional]
**text** | **String** | Content for the message |  [optional]
**speak** | **String** | SSML Speak for TTS audio response |  [optional]
**inputHint** | **String** | Indicates whether the bot is accepting, expecting, or ignoring input |  [optional]
**summary** | **String** | Text to display if the channel cannot render cards |  [optional]
**suggestedActions** | [**SuggestedActions**](SuggestedActions.md) | SuggestedActions are used to provide keyboard/quickreply like behavior in many clients |  [optional]
**attachments** | [**List&lt;Attachment&gt;**](Attachment.md) | Attachments |  [optional]
**entities** | [**List&lt;Entity&gt;**](Entity.md) | Collection of Entity objects, each of which contains metadata about this activity. Each Entity object is typed. |  [optional]
**channelData** | **Object** | Channel-specific payload |  [optional]
**action** | **String** | ContactAdded/Removed action |  [optional]
**replyToId** | **String** | The original ID this message is a response to |  [optional]
**value** | **Object** | Open-ended value |  [optional]
**name** | **String** | Name of the operation to invoke or the name of the event |  [optional]
**relatesTo** | [**ConversationReference**](ConversationReference.md) | Reference to another conversation or activity |  [optional]
**code** | **String** | Code indicating why the conversation has ended |  [optional]



