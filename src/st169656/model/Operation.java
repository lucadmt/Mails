package st169656.model;

import java.io.Serializable;

public enum Operation implements Serializable
  {
    SEND,
    FORWARD,
    REPLY,
    REPLY_ALL,
    DELETE,
    LIST,
    GET_MAILBOX,
    NEW_CLIENT_PORT,
    SUCCESS,
    FAILED
  }
