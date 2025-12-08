import React, { useEffect, useState } from "react";
import type { ChatSessionSummary } from "../types/chat";

const API_BASE_URL = "http://localhost:8080/api";

interface ChatSidebarProps {
  selectedSessionId: number | null;
  onSelectSession: (sessionId: number) => void;
}

const ChatSidebar: React.FC<ChatSidebarProps> = ({
  selectedSessionId,
  onSelectSession,
}) => {
  const [sessions, setSessions] = useState<ChatSessionSummary[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchSessions = async () => {
      try {
        setLoading(true);
        setError(null);

        const token = localStorage.getItem("jwtToken");
        if (!token) {
          setError("Нет токена. Войдите заново.");
          return;
        }

        const response = await fetch(`${API_BASE_URL}/chat/sessions`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          }

        });

        if (!response.ok) {
          throw new Error(`Failed to load chat sessions (Status: ${response.status})`);
        }

        const data: ChatSessionSummary[] = await response.json();

        // ✅ ЛОГ: Подтверждение успешной загрузки списка
        console.log("✅ Chat Sidebar: Sessions loaded successfully", data);

        setSessions(data);
      } catch (err: any) {
        console.error("❌ Chat Sidebar Error:", err.message);
        setError(err.message || "Unknown error");
      } finally {
        setLoading(false);
      }
    };

    fetchSessions();
  }, []);

  return (
    <div
      style={{
        width: "260px",
        borderRight: "1px solid #111827",
        padding: "12px",
        boxSizing: "border-box",
        display: "flex",
        flexDirection: "column",
        gap: "8px",
        backgroundColor: "#111827",
        color: "#F9FAFB",
      }}
    >
      <h2
        style={{
          margin: 0,
          marginBottom: "8px",
          fontSize: "18px",
          fontWeight: 600,
        }}
      >
        Chat history
      </h2>

      {loading && <div style={{ color: "#9CA3AF" }}>Loading sessions...</div>}

      {error && (
        <div style={{ color: "#FCA5A5", fontSize: "14px" }}>Error: {error}</div>
      )}

      {!loading && !error && sessions.length === 0 && (
        <div style={{ fontSize: "14px", color: "#9CA3AF" }}>
          You don't have any chats yet.
        </div>
      )}

      <div style={{ display: "flex", flexDirection: "column", gap: "4px" }}>
        {sessions.map((session) => {
          const isActive = session.id === selectedSessionId;

          return (
            <button
              key={session.id}
              onClick={() => {
                  // ⭐ ЛОГ: Убедитесь, что клик дошел до этой точки
                  console.log("➡️ Клик по кнопке в ChatSidebar для ID:", session.id);
                  onSelectSession(session.id);
              }}
              style={{
                textAlign: "left",
                padding: "8px",
                borderRadius: "6px",
                border: "none",
                cursor: "pointer",
                backgroundColor: isActive ? "#3B82F6" : "transparent",
                color: isActive ? "#F9FAFB" : "#E5E7EB",
                fontWeight: isActive ? 600 : 400,
                fontSize: "14px",
                transition: 'background-color 0.15s',
              }}
              onMouseEnter={(e) => {
                  if (!isActive) e.currentTarget.style.backgroundColor = '#1F2937';
              }}
              onMouseLeave={(e) => {
                  if (!isActive) e.currentTarget.style.backgroundColor = 'transparent';
              }}
            >
              {session.title || `Chat #${session.id}`}
            </button>
          );
        })}
      </div>
    </div>
  );
};

export default ChatSidebar;