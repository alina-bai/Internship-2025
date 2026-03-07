import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function AuthPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = () => {
        // здесь твой login API
        localStorage.setItem("jwtToken", "demo-token");
        navigate("/chat");
    };

    return (
        <div style={{ maxWidth: 400, margin: "50px auto" }}>
            <h1>Login / Register</h1>
            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={e => setEmail(e.target.value)}
                style={{ display: "block", width: "100%", marginBottom: 10, padding: 8 }}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                style={{ display: "block", width: "100%", marginBottom: 10, padding: 8 }}
            />
            <button onClick={handleLogin} style={{ padding: 8, width: "100%" }}>
                Login / Register
            </button>
        </div>
    );
}