// Описание одной сессии (чата), как приходит с бэкенда
export interface ChatSessionSummary {
  id: number;
  title: string;
  updatedAt: string; // например: "2025-11-18T10:20:00"
}

// Описание одного сообщения, как его отдаёт бэкенд (ChatMessageDto)
export interface ChatMessageDto {
  id: number;
  role: "user" | "ai";
  content: string;
  createdAt: string; // ISO-строка даты
}

