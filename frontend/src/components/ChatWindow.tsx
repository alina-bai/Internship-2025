import React, { useEffect, useState } from "react";
import api from "../api/axiosInstance";

interface Message {
  id: number;
  content: string;
  role: string;
}

interface Props {
  sessionId: number | null;
}

const ChatWindow: React.FC<Props> = ({ sessionId }) => {
  const [messages, setMessages] = useState<Message[]>([]);

  useEffect(() => {
    if (!sessionId) return;

    const loadMessages = async () => {
      try {
        console.log("📥 Loading messages for:", sessionId);

        const response = await api.get(`/chat/sessions/${sessionId}/messages`);
        setMessages(response.data);

      } catch (error) {
        console.error("❌ Failed to load messages", error);
      }
    };

    loadMessages();
  }, [sessionId]);

  if (!sessionId) {
    return <div style={{ padding: "20px" }}>Select a chat</div>;
  }

  return (
    <div style={{ flex: 1, padding: "20px" }}>
      <h3>Chat #{sessionId}</h3>

      {messages.map((msg) => (
        <div key={msg.id} style={{ marginBottom: "8px" }}>
          <b>{msg.role}:</b> {msg.content}
        </div>
      ))}
    </div>
  );
};

export default ChatWindow;
