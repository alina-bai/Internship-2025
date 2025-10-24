import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Chat: React.FC = () => {
  const [message, setMessage] = useState('');
  const [chat, setChat] = useState<string[]>([]);
  const navigate = useNavigate();

  const handleSend = (e: React.FormEvent) => {
    e.preventDefault();
    if (!message.trim()) return;
    setChat((prev) => [...prev, message]);
    setMessage('');
  };

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    localStorage.removeItem('username');
    navigate('/login');
  };

  return (
    <div className="flex flex-col min-h-screen bg-gray-100">
      <header className="bg-blue-600 text-white p-4 flex justify-between items-center">
        <h1 className="text-xl font-bold">Chat Room</h1>
        <button
          onClick={handleLogout}
          className="bg-red-500 px-3 py-1 rounded hover:bg-red-600"
        >
          Logout
        </button>
      </header>

      <main className="flex-1 p-4 overflow-y-auto">
        {chat.length === 0 ? (
          <p className="text-gray-600 text-center">No messages yet</p>
        ) : (
          chat.map((msg, i) => (
            <p key={i} className="bg-white p-2 rounded shadow mb-2">
              {msg}
            </p>
          ))
        )}
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
        >
          Send
        </button>
      </form>
    </div>
  );
};

export default Chat;
