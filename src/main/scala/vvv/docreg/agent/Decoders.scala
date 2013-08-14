/*
 * Copyright (c) 2013 Aviat Networks.
 * This file is part of DocReg+Web. Please refer to the NOTICE.txt file for license details.
 */

package vvv.docreg.agent

import org.jboss.netty.buffer.ChannelBuffer
import java.nio.charset.Charset
import scala.Predef._

trait ReplyDecoder {
  def decode(header: Header, buffer: ChannelBuffer): Reply

  def readString(b: ChannelBuffer, length: Int) =
  {
    val string: String = b.readBytes(length).toString(Charset.forName("UTF-8"))
    val end = string.indexOf('\u0000')
    if (end >= 0)
    {
      string.substring(0, end)
    }
    else
    {
      string
    }
  }
}

object NextChangeReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
    val changeNumber = buffer.readInt()
    val key = buffer.readInt()
    val version = buffer.readInt()
    val fileName = readString(buffer, 128)
    val projectName = readString(buffer, 64)
    val title = readString(buffer, 64)
    val description = readString(buffer, 512)
    val access = readString(buffer, 128)
    val author = readString(buffer, 64)
    val date = readString(buffer, 32)
    val server = readString(buffer, 32)
    val client = readString(buffer, 32)
    val editor = readString(buffer, 64)
    val editorStart = readString(buffer, 32)

    val keyStr = documentNumberFormat.format(key)

    NextChangeReply(
      changeNumber,
      DocumentInfo(
        keyStr,
        version,
        fileName,
        projectName,
        title,
        description,
        access,
        author,
        parseAgentDate(date),
        server,
        client,
        editor,
        parseAgentDate(editorStart))
    )
  }
}

object RegisterReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
    val response: String = readString(buffer, 128)
    val suggestedFileName: String = readString(buffer, 128)

    RegisterReply(
      response,
      suggestedFileName
    )
  }
}

object SubmitReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
    val response: String = readString(buffer, 128)
    val suggestedFileName: String = readString(buffer, 128)

    SubmitReply(
      response,
      suggestedFileName
    )
  }
}

object EditReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
//    char acAuthor[64];     // author - display if not equal to request
    val userName: String = readString(buffer, 64)

    EditReply(
      userName
    )
  }
}

object SubscribeReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
//    char acResponse[128];      // "Accepted" or "Rejected"
//    char acFileName[128];      // new name generated by server
//    char acAuthor[64];         // author name as displayed
    val response: String = readString(buffer, 128)
    val fileName: String = readString(buffer, 128)
    val userName: String = readString(buffer, 64)

    SubscribeReply(
      response,
      fileName,
      userName
    )
  }
}

object UnsubscribeReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
//    char acResponse[128];      // "Accepted" or "Rejected"
//    char acFileName[128];      // new name generated by server
//    char acAuthor[64];         // author name as displayed
    val response: String = readString(buffer, 128)
    val fileName: String = readString(buffer, 128)
    val userName: String = readString(buffer, 64)

    UnsubscribeReply(
      response,
      fileName,
      userName
    )
  }
}

object ApprovalReplyDecoder extends ReplyDecoder
{
  def decode(header: Header, buffer: ChannelBuffer) =
  {
//    char acResponse[128];      // "Accepted" or "Rejected"
    val response: String = readString(buffer, 128)

    ApprovalReply(
      response
    )
  }
}
