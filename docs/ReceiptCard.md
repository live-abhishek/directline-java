
# ReceiptCard

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**title** | **String** | Title of the card |  [optional]
**items** | [**List&lt;ReceiptItem&gt;**](ReceiptItem.md) | Array of Receipt Items |  [optional]
**facts** | [**List&lt;Fact&gt;**](Fact.md) | Array of Fact Objects   Array of key-value pairs. |  [optional]
**tap** | [**CardAction**](CardAction.md) | This action will be activated when user taps on the card |  [optional]
**total** | **String** | Total amount of money paid (or should be paid) |  [optional]
**tax** | **String** | Total amount of TAX paid(or should be paid) |  [optional]
**vat** | **String** | Total amount of VAT paid(or should be paid) |  [optional]
**buttons** | [**List&lt;CardAction&gt;**](CardAction.md) | Set of actions applicable to the current card |  [optional]



