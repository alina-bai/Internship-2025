import React, { useState } from "react";
import { registerUser } from "../api/api";

export default function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    const result = await registerUser(username, password);
    setMessage(result.message || "Registration done");
  };

  return (
    <div className="flex flex-col items-center mt-10">
      <h2 className="text-xl mb-4">Register</h2>
      <form onSubmit={handleRegister} className="flex flex-col gap-2 w-64">
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
        <button type="submit" className="bg-green-500 text-white p-2 rounded">
          Register
        </button>
      </form>
      <p className="mt-2">{message}</p>
    </div>
  );
}
