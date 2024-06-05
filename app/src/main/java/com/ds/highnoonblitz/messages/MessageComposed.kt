package com.ds.highnoonblitz.messages
import com.ds.highnoonblitz.bluetooth.communication_threads.Purposes
import org.json.JSONArray
import org.json.JSONObject

class MessageComposed private constructor(private val message: JSONObject, private val purpose: Int) {

    companion object {
        class MessageBuilder {
            private var message = JSONObject()
            private var purpose: Int = Purposes.MESSAGE_TEST

            fun addMessageParameter(key: String, value: Any): MessageBuilder {
                when (value) {
                    is String -> message.put(key, value)
                    is Boolean -> message.put(key, value)
                    is JSONObject -> {
                        if (message.has(key)) {
                            val existingObject = message.getJSONObject(key)
                            val keys = value.keys()
                            while (keys.hasNext()) {
                                val nextKey = keys.next()
                                existingObject.put(nextKey, value.get(nextKey))
                            }
                        } else {
                            message.put(key, value)
                        }
                    }
                    is List<*> -> {
                        if (value.all { it is String }) {
                            val jsonArray = JSONArray(value)
                            message.put(key, jsonArray)
                        } else {
                            throw IllegalArgumentException("List elements are not all Strings")
                        }
                    }

                }
                return this
            }

            fun addMessage(message: JSONObject): MessageBuilder {
                this.message = message
                return this
            }

            fun setPurpose(purpose: Int): MessageBuilder {
                this.purpose = purpose
                return this
            }

            fun build(): MessageComposed {
                return MessageComposed(message, purpose)
            }

            fun fromString(jsonString: String): MessageBuilder {
                val jsonObject = JSONObject(jsonString)
                this.message = jsonObject.getJSONObject("message")
                this.purpose = jsonObject.getInt("purpose")
                return this
            }
        }
    }

    fun getMessage(): JSONObject {
        return message
    }

    fun getPurpose(): Int {
        return purpose
    }

    override fun toString(): String {
        val jsonObject = JSONObject()
        jsonObject.put("message", message)
        jsonObject.put("purpose", purpose)
        return jsonObject.toString()
    }
}