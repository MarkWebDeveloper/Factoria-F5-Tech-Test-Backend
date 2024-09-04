package dev.mark.factoria_tech_test.messages;

import java.util.HashMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    private HashMap<String, String> message;
  
    public HashMap<String, String> getMessage() {
      return message;
    }
  
    public HashMap<String, String> createMessage(String msg) {
      HashMap<String, String> newMessage = new HashMap<String, String>();
      newMessage.put("message", msg);
      return newMessage;
    }
  
  }