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

  // ⭐ ID текущей сессии чата (null = новый чат)
  const [chatSessionId, setChatSessionId] = useState<number | null>(null);

  const chatEndRef = useRef<HTMLDivElement | null>(null);

  // Автоскролл вниз при новом сообщении
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [chat]);

  // ===============================
  // 1) ОТПРАВКА СООБЩЕНИЯ
  // ===============================
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
        setChat((prev) => [
          ...prev,
          {
            role: "ai",
            text: "⚠️ Ошибка аутентификации. Пожалуйста, войдите снова.",
          },
        ]);
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
            'Cache-Control': 'no-cache', // Для POST-запросов обычно не нужно, но для надежности
          },
        }
      );

      const replyText: string =
        response.data?.response ?? "⚠️ Неизвестный ответ от AI";
      const newSessionId: number | null =
        response.data?.chatSessionId ?? chatSessionId;

      if (newSessionId != null) {
        setChatSessionId(newSessionId);
      }

      const aiMessage: ChatMessage = { role: "ai", text: replyText };
      setChat((prev) => [...prev, aiMessage]);
    } catch (error: any) {
      console.error("❌ Chat error:", error);
      setChat((prev) => [
        ...prev,
        {
          role: "ai",
          text: "⚠️ AI: Что-то пошло не так. Проверьте логи сервера.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  // ===============================
  // 2) ПЕРЕКЛЮЧЕНИЕ СЕССИЙ
  // ===============================
  const handleSelectSession = async (sessionId: number) => {
    // ⭐ ЛОГ 1: Главная проверка, дошла ли функция до родителя
    console.log("✅ КЛИК: Запуск handleSelectSession в Chat.tsx для ID:", sessionId);

    // ⭐ УЛУЧШЕНИЕ: Очищаем старую историю сразу и сбрасываем ID
    setChat([]);
    setLoading(true);
    setChatSessionId(null);

    try {
      const token = localStorage.getItem("jwtToken");

      if (!token) {
        setChat([{ role: "ai", text: "⚠️ Ошибка аутентификации. Токен отсутствует." }]);
        onLogout();
        return;
      }

      const response = await axios.get<ChatMessageDto[]>(
        `${API_BASE_URL}/chat/sessions/${sessionId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            // ⭐ ИСПРАВЛЕНИЕ 304: Запрещаем кеширование, чтобы всегда получать 200 OK
            'Cache-Control': 'no-cache',
          },
        }
      );

      const messagesFromServer = response.data;

      // ⭐ ЛО