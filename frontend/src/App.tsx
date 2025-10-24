import React, { useEffect, useState } from "react";
import "./index.css";

function App() {
  const [darkMode, setDarkMode] = useState(localStorage.getItem("theme") === "dark");
  const [isSignUp, setIsSignUp] = useState(false); // 👈 режим формы
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState<null | "error" | "success">(null);

  useEffect(() => {
    document.body.classList.toggle("dark", darkMode);
    localStorage.setItem("theme", darkMode ? "dark" : "light");
  }, [darkMode]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (isSignUp) {
      // имитация регистрации
      if (email && password.length >= 4) {
        setMessage("success");
      } else {
        setMessage("error");
      }
    } else {
      // имитация логина
      if (email === "test@example.com" && password === "1234") {
        setMessage("success");
      } else {
        setMessage("error");
      }
    }
  };

  return (
    <div className="app">
      <div
        className="theme-toggle"
        onClick={() => setDarkMode(!darkMode)}
        title="Toggle theme"
      >
        {darkMode ? "☀️" : "🌙"}
      </div>

      <div className="form-container">
        <h2>{isSignUp ? "Sign Up" : "Sign In"}</h2>

        <form onSubmit={handleSubmit}>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button type="submit">{isSignUp ? "Register" : "Login"}</button>

          {message === "error" && (
            <p className="error">
              {isSignUp
                ? "Please enter a valid email and password (min 4 chars)"
                : "Invalid email or password"}
            </p>
          )}
          {message === "success" && (
            <p className="success">
              {isSignUp
                ? "Account created successfully!"
                : "Login successful!"}
            </p>
          )}
        </form>

        {/* 👇 Переключатель режимов */}
        <p className="switch-mode">
          {isSignUp ? (
            <>
              Already have an account?{" "}
              <span onClick={() => { setIsSignUp(false); setMessage(null); }}>Sign In</span>
            </>
          ) : (
            <>
              Don’t have an account?{" "}
              <span onClick={() => { setIsSignUp(true); setMessage(null); }}>Sign Up</span>
            </>
          )}
        </p>
      </div>
    </div>
  );
}

export default App;
