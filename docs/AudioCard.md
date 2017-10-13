
# AudioCard

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**aspect** | **String** | Aspect ratio of thumbnail/media placeholder, allowed values are \&quot;16x9\&quot; and \&quot;9x16\&quot; |  [optional]
**title** | **String** | Title of the card |  [optional]
**subtitle** | **String** | Subtitle of the card |  [optional]
**text** | **String** | Text of the card |  [optional]
**image** | [**ThumbnailUrl**](ThumbnailUrl.md) | Thumbnail placeholder |  [optional]
**media** | [**List&lt;MediaUrl&gt;**](MediaUrl.md) | Array of media Url objects |  [optional]
**buttons** | [**List&lt;CardAction&gt;**](CardAction.md) | Set of actions applicable to the current card |  [optional]
**shareable** | **Boolean** | Is it OK for this content to be shareable with others (default:true) |  [optional]
**autoloop** | **Boolean** | Should the client loop playback at end of content (default:true) |  [optional]
**autostart** | **Boolean** | Should the client automatically start playback of video in this card (default:true) |  [optional]



