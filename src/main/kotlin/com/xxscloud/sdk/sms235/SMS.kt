@file:Suppress("UNCHECKED_CAST", "unused")

package com.xxscloud.sdk.sms235

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


/**
 * 短信SDK.
 * @property token SMSToken
 * @constructor
 */
class SMS constructor(
        private val token: SMSToken
) {
    /**
     * 发送短信.
     * @param phone Array<String> 手机号.
     * @param message String 消息内容.
     * @param label String 头部标签.
     * @param sendTime Date 发送时间
     * @return Boolean 是否成功.
     */
    fun send(phone: Array<String>, message: String, label: String = "", sendTime: Date = Date()): Boolean {
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmm")
        val request = HashMap<String, Any>()
        request["msg"] = if (label.isNotEmpty()) "【${label}】${message}" else message
        request["phone"] = phone.joinToString(",")
        request["sendtime"] = simpleDateFormat.format(sendTime)
        val result = HttpClient.post(token, "http://smssh1.253.com/msg/send/json", request)
        return if ((result["code"] ?: "").toString() == "0") {
            true
        } else {
            throw IOException((result["errorMsg"] ?: "").toString())
        }
    }

    /**
     * 发送短信.
     * @param phone String 手机号.
     * @param message String 消息内容.
     * @param label String 头部标签.
     * @return Boolean 是否成功.
     */
    fun send(phone: String, message: String, label: String = ""): Boolean {
        return send(arrayOf(phone), message, label)
    }

    /**
     * 发送短信.
     * @param phone String 手机号.
     * @param template String 模板内容.
     * @param label String 头部标签.
     * @param args HashMap<String, String> 模板参数.
     * @return Boolean 是否成功.
     */
    fun send(phone: String, template: String, label: String = "", args: HashMap<String, String>): Boolean {
        var message = template
        args.forEach { (k, v) ->
            message = message.replace("#{${k}}", v)
        }
        return send(arrayOf(phone), message, label)
    }
}