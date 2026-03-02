import { useState, useEffect } from "react";
import axiosInstance from "../api/axiosInstance";

// тип сообщения
type Message = {
  id: number;
  text: string;
  role: "user" | "assistant";
};

// пропсы
type Props = {
  sessionId: number; // выбранная сессия
};

export default function ChatWindow({ sessionId }: Props) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState<string>("");

  // подгружаем историю при смене сессии
  useEffect(() => {
    const stored = localStorage.getItem(`chat_${sessionId}`);
    if (stored) {
      setMessages(JSON.parse(stored));
    } else {
      setMessages([]);
    }
  }, [sessionId]);

  // сохранение истории
  useEffect(() => {
    localStorage.setItem(`chat_${sessionId}`, JSON.stringify(messages));
  }, [messages, sessionId]);

  // отправка сообщения
  const sendMessage = async (msg: string) => {
    if (!msg) return;

    const userMessage: Message = { id: Date.now(), text: msg, role: "user" };
    setMessages(prev => [...prev, userMessage]);
    setInput("");

    try {
      const response = await axiosInstance.post("/chat", { message: msg, sessionId });
      const aiMessage: Message = {
        id: Date.now() + 1,
        text: response.data.reply,
        role: "assistant"
      };
      setMessages(prev => [...prev, aiMessage]);
    } catch (err) {
      console.error(err);
    }
  };

  return (
      <div style={{ flex: 1, display: "flex", flexDirection: "column", padding: "1rem" }}>
        <div style={{ flex: 1, overflowY: "auto", marginBottom: "1rem" }}>
          {messages.map(msg => (
              <div
                  key={msg.id}
                  style={{
                    textAlign: msg.role === "user" ? "right" : "left",
                    marginBottom: "0.5rem"
                  }}
              >
                <b>{msg.role === "user" ? "You" : "AI"}:</b> {msg.text}
              </div>
          ))}
        </div>

        <div style={{ display: "flex" }}>
          <input
              type="text"
              value={input}
              onChange={e => setInput(e.target.value)}
              style={{ flex: 1, marginRight: "0.5rem" }}
          />
          <button onClick={() => sendMessage(input)}>Send</button>
        </div>
      </div>
  );
}