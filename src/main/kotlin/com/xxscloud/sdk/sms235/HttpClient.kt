@file:Suppress("UNCHECKED_CAST")

package com.xxscloud.sdk.sms235

import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.collections.HashMap


object HttpClient {
    private val CLIENT = OkHttpClient.Builder().build()
    private val JSON_MAPPER: ObjectMapper = ObjectMapper()
    private val log = LoggerFactory.getLogger(HttpClient::class.java)
    private val mediaType = "application/json".toMediaType()


    fun post(token: SMSToken, url: String, request: HashMap<String, Any>): HashMap<String, Any> {
        request["account"] = token.account
        request["password"] = token.password
        log.debug("[创蓝] 地址: $url 请求参数: ${JSON_MAPPER.writeValueAsString(request)}")
        val response = CLIENT.newCall(Request.Builder().url(url).post(JSON_MAPPER.writeValueAsString(request).toRequestBody(mediaType)).build()).execute()
        val responseJson = response.body?.string() ?: throw IOException("response is null")
        log.debug("[创蓝] 响应参数: $responseJson")
        return JSON_MAPPER.readValue(responseJson, HashMap::class.java) as HashMap<String, Any>
    }
}