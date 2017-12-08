namespace java com.hfy.thrift

struct Message {
   1: string clientName,
   2: string message
}

service MessageService {
    oneway void sendMessage(Message msg)
}