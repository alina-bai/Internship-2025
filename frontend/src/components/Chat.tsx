// frontend/src/components/Chat.tsx

import React, { useState, useEffect, useRef } from "react";
import axios from "axios";

import ChatSidebar from "./ChatSidebar";
import type { ChatMessageDto } from "../types/chat";

const API_BASE_URL = "http://localhost:8080/api";

type Role = "user" | "ai";

interface ChatMessage {
  role: Role;
  text: string;
}

interface ChatProps {
  userEmail: string;
  onLogout: () => void;
}

const Chat: React.FC<ChatProps> = ({ userEmail, onLogout }) => {
  const [message, setMessage] = useState("");
  const [chat, setChat] = useState<ChatMessage[]>([]);
  const [loading, setLoading] = useState(false);

  // id текущей сессии
  const [chatSessionId, setChatSessionId] = useState<number | null>(null);

  const chatEndRef = useRef<HTMLDivElement | null>(null);

  // автоскролл
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [chat]);

  // =====================================================
  // 1. ОТПРАВКА СООБЩЕНИЯ
  // =====================================================
  const handleSend = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!message.trim()) return;

    const userMessage: ChatMessage = { role: "user", text: message };
    setChat((prev) => [...prev, userMessage]);
    setMessage("");
    setLoading(true);

    try {
      const token = localStorage.getItem("jwtToken");
      if (!token) {
        setChat((prev) => [...prev, { role: "ai", text: "Auth error. Login again." }]);
        onLogout();
        return;
      }

      const response = await axios.post(
        `${API_BASE_URL}/chat`,
        {
          prompt: userMessage.text,
          chatSessionId: chatSessionId,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
            "Cache-Control": "no-cache",
          },
        }
      );

      const reply = response.data?.response ?? "AI returned empty response";
      const newId = response.data?.chatSessionId ?? chatSessionId;

      if (newId != null) setChatSessionId(newId);

      setChat((prev) => [...prev, { role: "ai", text: reply }]);
    } catch (err) {
      console.error("Chat error:", err);
      setChat((prev) => [...prev, { role: "ai", text: "Network error." }]);
    } finally {
      setLoading(false);
    }
  };

  // =====================================================
  // 2. ЗАГРУЗКА СТАРОЙ СЕССИИ
  // =====================================================
  const handleSelectSession = async (sessionId: number) => {
    console.log("➡️ SELECT CHAT SESSION:", sessionId);

    setChat([]);
    setLoading(true);
    setChatSessionId(sessionId);

    try {
      const token = localStorage.getItem("jwtToken");
      if (!token) {
        onLogout();
        return;
      }

      const response = await axios.get<ChatMessageDto[]>(
        `${API_BASE_URL}/chat/sessions/${sessionId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Cache-Control": "no-cache", // предотвращает 304
          },
        }
      );

      console.log("📥 LOADED MESSAGES FOR SESSION:", response.data);

      const mapped: ChatMessage[] = response.data.map((msg) => ({
        role: msg.role as "user" | "ai",
        text: msg.content,
      }));

      setChat(mapped);
    } catch (err) {
      console.error("Load session error:", err);
      setChat([{ role: "ai", text: "Failed to load this chat." }]);
    } finally {
      setLoading(false);
    }
  };

  // =====================================================
  // 3. UI РЕНДЕР
  // =====================================================

  return (
    <div className="flex min-h-screen bg-gray-100">
      <ChatSidebar
        selectedSessionId={chatSessionId}
        onSelectSession={handleSelectSession}
      />

      {/* Чат */}
      <div className="flex flex-col flex-1">
        <header className="bg-blue-600 text-white p-4 flex justify-between items-center">
          <h1 className="text-xl font-bold">
            AI Chat — {userEmail}
            {chatSessionId && <span className="ml-2 text-sm">Chat #{chatSessionId}</span>}
          </h1>

          <button
            onClick={onLogout}
            className="bg-red-500 px-4 py-2 rounded-lg hover:bg-red-600"
          >
            Logout
          </button>
        </header>

        <main className="flex-1 p-4 overflow-y-auto">
          {chat.map((msg, index) => (
            <p
              key={index}
              className={`p-2 rounded mb-2 ${
                msg.role === "user"
                  ? "bg-blue-100 text-right"
                  : "bg-green-100 text-left"
              }`}
            >
              {msg.text}
            </p>
          ))}

          {loading && (
            <p className="text-center text-gray-500 mt-2">AI is typing…</p>
          )}

          <div ref={chatEndRef} />
        </main>

        <form
          onSubmit={handleSend}
          className="flex p-4 bg-white border-t border-gray-200"
        >
          <input
            type="text"
            placeholder="Enter message…"
            value={message}
            onChange={(e) => setMessage(e.target.value)}
            className="flex-1 border p-2 rounded mr-2"
            disabled={loading}
          />

          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:bg-blue-300"
            disabled={loading || !message.trim()}
          >
            Send
          </button>
        </form>
      </div>
    </div>
  );
};

export default Chat;
