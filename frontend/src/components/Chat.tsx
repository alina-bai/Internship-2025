import React, { useState } from "react";
import axios from "axios";

const Chat: React.FC = () => {
  const [message, setMessage] = useState("");
  const [chat, setChat] = useState<{ role: string; text: string }[]>([]);
  const [loading, setLoading] = useState(false);

  const handleSend = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!message.trim()) return;

    const userMessage = { role: "user", text: message };
    setChat((prev) => [...prev, userMessage]);
    setMessage("");
    setLoading(true);

    try {
      const token = localStorage.getItem("jwtToken");
      const response = await axios.post(
        "http://localhost:8080/api/chat",
        { message },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      const aiMessage = {
        role: "ai",
        text: response.data.reply || response.data || "⚠️ No response from AI",
      };
      setChat((prev) => [...prev, aiMessage]);
    } catch (error) {
      console.error("Chat error:", error);
      setChat((prev) => [
        ...prev,
        { role: "ai", text: "⚠️ AI: Something went wrong. Try again." },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      <header className="bg-blue-600 text-white p-4 flex justify-between items-center">
        <h1 className="text-xl font-bold">AI Chat</h1>
      </header>

      <main className="flex-1 p-4 overflow-y-auto">
        {chat.length === 0 ? (
          <p className="text-gray-600 text-center">Start the conversation!</p>
        ) : (
          chat.map((msg, index) => (
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
          ))
        )}
        {loading && <p className="text-center text-gray-500">Thinking...</p>}
      </main>

      <form
        onSubmit={handleSend}
        className="flex p-4 bg-white border-t border-gray-200"
      >
        <input
          type="text"
          placeholder="Type your message..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          className="flex-1 border p-2 rounded mr-2"
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
          disabled={loading}
        >
          Send
        </button>
      </form>
    </div>
  );
};

export default Chat;
