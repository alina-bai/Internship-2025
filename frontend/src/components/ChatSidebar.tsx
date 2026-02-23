import React, { useEffect, useState } from "react";
import type { ChatSessionSummary } from "../types/chat";
import api from "../api/axiosInstance";

interface ChatSidebarProps {
  selectedSessionId: number | null;
  onSelectSession: (sessionId: number) => void;
}

const ChatSidebar: React.FC<ChatSidebarProps> = ({
  selectedSessionId,
  onSelectSession,
}) => {
  const [sessions, setSessions] = useState<ChatSessionSummary[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchSessions = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await api.get("/chat/sessions");

        console.log("✅ Sessions:", response.data);
        setSessions(response.data);

      } catch (err: any) {
        console.error("❌ Chat Sidebar Error:", err.response?.status);
        setError("Не удалось загрузить чаты");
      } finally {
        setLoading(false);
      }
    };

    fetchSessions();
  }, []);

  return (
    <div style={{ width: "260px", padding: "12px" }}>
      <h2>Chat history</h2>

      {loading && <div>Loading...</div>}
      {error && <div>{error}</div>}

      {sessions.map((session) => {
        const isActive = session.id === selectedSessionId;

        return (
          <button
            key={session.id}
            onClick={() => onSelectSession(session.id)}
            style={{
              display: "block",
              width: "100%",
              marginBottom: "6px",
              background: isActive ? "#3B82F6" : "transparent",
              color: isActive ? "white" : "black",
            }}
          >
            {session.title || `Chat #${session.id}`}
          </button>
        );
      })}
    </div>
  );
};

export default ChatSidebar;
