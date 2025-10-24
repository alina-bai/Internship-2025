import React, { useState } from "react";
import { loginUser } from '../api/api';

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    const result = await loginUser(username, password);
    if (result.token) {
      localStorage.setItem("token", result.token);
      setMessage("Login successful!");
    } else {
      setMessage(result.message || "Login failed");
    }
  };

  return (
    <div className="flex flex-col items-center mt-10">
      <h2 className="text-xl mb-4">Login</h2>
      <form onSubmit={handleLogin} className="flex flex-col gap-2 w-64">
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="border p-2"
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="border p-2"
        />
        <button type="submit" className="bg-blue-500 text-white p-2 rounded">
          Login
        </button>
      </form>
      <p className="mt-2">{message}</p>
    </div>
  );
}
