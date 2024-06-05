package com.ds.highnoonblitz.messages

import android.util.Log
import com.ds.highnoonblitz.leader_election_algorithm.BullyElectionManager
import com.ds.highnoonblitz.bluetooth.communication_threads.Purposes
import org.json.JSONObject

class MessageFactory private constructor() {

    companion object {

        fun createStandardMessage(message: JSONObject): MessageComposed {
            return MessageComposed.Companion.MessageBuilder()
                                            .addMessage(message)
                                            .build()
        }
        fun createUpdateListMessage(deviceMac: String): MessageComposed {
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("deviceMac", deviceMac)
                .setPurpose(Purposes.UPDATE_CONNECTED_LIST)
                .build()
        }

        fun createTestMessage(): MessageComposed {
            Log.i("com.ds.highnoonblitz.messages.MessageFactory", "Creating test message")
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("test", "MESSAGGIO DI TEST")
                .setPurpose(Purposes.MESSAGE_TEST)
                .build()
        }

        fun createReadyMessage(deviceName: String, isReady: Boolean): MessageComposed{
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("ready", isReady)
                .addMessageParameter("deviceName", deviceName)
                .setPurpose(Purposes.READY)
                .build()
        }

        fun createConfigurationMessage(devicesMac: List<String>): MessageComposed{
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("devices", devicesMac)
                .setPurpose(Purposes.CONFIGURATION)
                .build()
        }

        fun createElectionMessage(): MessageComposed {
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("ID", BullyElectionManager.sessionID)
                .setPurpose(Purposes.ELECTION_REQUEST)
                .build()
        }

        fun createInfoSharingMessage(): MessageComposed {
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("UUID", BullyElectionManager.sessionID)
                .setPurpose(Purposes.INFO_SHARING)
                .build()
        }

        fun createAcknowledgeMessage(): MessageComposed {
            return MessageComposed.Companion.MessageBuilder()
                .addMessageParameter("ACK", "ACK")
                .setPurpose(Purposes.ACK)
                .build()
        }


    }
}