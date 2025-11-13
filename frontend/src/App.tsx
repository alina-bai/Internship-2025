import React, { useEffect, useState } from "react";
import axios from "axios";
import { BrowserRouter, Routes, Route, useNavigate, Navigate } from "react-router-dom";
import { Send, Loader2, LogOut } from 'lucide-react';

// --- Константы ---
const API_BASE_URL = "http://localhost:8080/api";

interface Message {
    role: "user" | "ai";
    text: string;
}

// --- 1. Компонент: Чат ---
const Chat = ({ userEmail, onLogout }) => {
    const [message, setMessage] = useState("");
    const [chat, setChat] = useState([]);
    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const chatEndRef = React.useRef(null);

    // Прокрутка вниз при новом сообщении
    useEffect(() => {
        chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [chat]);

    const handleSend = async (e) => {
        e.preventDefault();
        setErrorMessage('');

        if (!message.trim()) return;

        const userMessage = { role: "user", text: message };
        setChat((prev) => [...prev, userMessage]);
        setMessage("");
        setLoading(true);

        try {
            const token = localStorage.getItem("jwtToken");

            if (!token) {
                setErrorMessage("Ошибка аутентификации. Выйдите и войдите снова.");
                onLogout();
                return;
            }

            // Запрос на ваш Spring Boot бэкенд
            const response = await axios.post(
                `${API_BASE_URL}/chat`,
                // Используем 'prompt' для соответствия бэкенду
                { prompt: userMessage.text },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        "Content-Type": "application/json",
                    },
                }
            );

            // Обработка ответа от бэкенда. Ищем поле 'response', как определено в ChatResponseDto
            const replyText = response.data?.response;

            const aiMessage = {
                role: "ai",
                text: replyText
                    ? replyText
                    : "⚠️ Неизвестный ответ от AI: Сервер вернул успешный статус, но поле 'response' в JSON-ответе отсутствует. Проверьте ваш DTO и логи бэкенда.",
            };
            setChat((prev) => [...prev, aiMessage]);
        } catch (error) {
            console.error("Chat error:", error.response || error);

            if (error.response?.status === 401 || error.response?.status === 403) {
                setErrorMessage("Сессия истекла или токен недействителен. Пожалуйста, выполните повторный вход.");
                setTimeout(() => onLogout(), 2000);
            } else {
                // Это сообщение, которое вы сейчас видите
                setErrorMessage("Ошибка связи с сервером. Проверьте ваш API-ключ Gemini и логи бэкенда.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex flex-col min-h-screen bg-gray-100 dark:bg-gray-900 transition-colors duration-300">
            <header className="bg-blue-600 dark:bg-gray-800 text-white p-4 flex justify-between items-center shadow-md">
                {/* Отображаем email пользователя */}
                <h1 className="text-xl font-bold">AI Чат (пользователь: {userEmail})</h1>
                <button
                    onClick={onLogout}
                    className="flex items-center text-sm px-4 py-2 rounded-lg bg-red-500 hover:bg-red-600 transition-colors"
                    title="Выход"
                >
                    <LogOut className="mr-2 h-4 w-4" /> Выход
                </button>
            </header>

            <main className="flex-1 p-4 overflow-y-auto space-y-3 pb-24">
                {chat.length === 0 && (
                    <p className="text-gray-500 dark:text-gray-400 text-center pt-10">Начните общение с AI</p>
                )}

                {chat.map((msg, index) => (
                    <div
                        key={index}
                        className={`flex ${msg.role === "user" ? "justify-end" : "justify-start"}`}
                    >
                        <p
                            className={`max-w-3/4 p-3 rounded-xl shadow-lg ${
                                msg.role === "user"
                                    ? "bg-blue-500 text-white rounded-br-none"
                                    : "bg-white dark:bg-gray-700 text-gray-800 dark:text-white rounded-tl-none border border-gray-200 dark:border-gray-600"
                            }`}
                            style={{ maxWidth: '80%' }}
                        >
                            {msg.text}
                        </p>
                    </div>
                ))}
                {loading && (
                    <div className="flex justify-start">
                        <p className="bg-gray-200 dark:bg-gray-700 text-gray-600 dark:text-gray-400 p-3 rounded-xl shadow-lg flex items-center">
                            <Loader2 className="animate-spin mr-2 h-4 w-4" /> AI думает...
                        </p>
                    </div>
                )}
                <div ref={chatEndRef} />
                {errorMessage && (
                    <p className="text-red-500 text-center font-semibold mt-4 bg-red-100 dark:bg-red-900 p-3 rounded-lg border border-red-300 dark:border-red-700">
                        {errorMessage}
                    </p>
                )}
            </main>

            <div className="fixed bottom-0 left-0 right-0 p-4 bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 shadow-xl">
                <form
                    onSubmit={handleSend}
                    className="flex max-w-4xl mx-auto"
                >
                    <input
                        type="text"
                        placeholder="Введите ваше сообщение..."
                        value={message}
                        onChange={(e) => setMessage(e.target.value)}
                        className="flex-1 border border-gray-300 dark:border-gray-600 p-3 rounded-l-lg focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:text-white"
                        disabled={loading}
                    />
                    <button
                        type="submit"
                        className={`bg-blue-600 text-white px-6 py-3 rounded-r-lg font-semibold transition-colors flex items-center justify-center ${
                            loading ? "bg-blue-400 cursor-not-allowed" : "hover:bg-blue-700"
                        }`}
                        disabled={loading || !message.trim()}
                    >
                        <Send className="h-5 w-5" />
                    </button>
                </form>
            </div>
        </div>
    );
};


// --- 2. Компонент: Вход/Регистрация (AuthScreen) ---
const AuthScreen = ({ setToken, setUserEmail }) => {
    const navigate = useNavigate();

    // --- Состояния UI ---
    const [darkMode, setDarkMode] = useState(localStorage.getItem("theme") === "dark");
    const [isSignUp, setIsSignUp] = useState(false);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState(null);
    const [loading, setLoading] = useState(false);

    // --- Эффект для night mode ---
    useEffect(() => {
        document.body.classList.toggle("dark", darkMode);
        localStorage.setItem("theme", darkMode ? "dark" : "light");
    }, [darkMode]);

    // --- Отправка формы (Реальный AJAX) ---
    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage(null);
        setLoading(true);

        const endpoint = isSignUp ? "/auth/register" : "/auth/login";

        // --- Скорректированный Payload ---
        // Используем email в качестве username, чтобы удовлетворить требования DTO (username: @NotEmpty)
        // и логики входа (findByUsername)
        const payload = isSignUp
            ? { username: email, email, password }
            : { username: email, password };
        // --- Конец Payload ---

        try {
            const response = await axios.post(`${API_BASE_URL}${endpoint}`, payload);

            if (!isSignUp && response.data.token) {
                // ✅ ЛОГИН успешен
                localStorage.setItem("jwtToken", response.data.token);
                localStorage.setItem("userEmail", email); // Используем userEmail

                // ОБНОВЛЕНИЕ 1: Обновляем состояние в родительском компоненте App
                setToken(response.data.token);
                setUserEmail(email);

                setMessage("success");
                setTimeout(() => navigate("/chat"), 500);
            } else if (isSignUp) {
                // ✅ РЕГИСТРАЦИЯ успешна - теперь автоматически логинимся
                setMessage("success");

                // 1. Ждем 1 секунду, чтобы пользователь увидел сообщение об успехе
                await new Promise(resolve => setTimeout(resolve, 1000));

                // 2. Создаем payload для автоматического входа
                const loginPayload = { username: email, password };

                try {
                    // 3. Выполняем автоматический вход (постинг на /auth/login)
                    const loginResponse = await axios.post(`${API_BASE_URL}/auth/login`, loginPayload);

                    if (loginResponse.data.token) {
                        // 4. Автологин успешен, сохраняем токен и перенаправляем
                        localStorage.setItem("jwtToken", loginResponse.data.token);
                        localStorage.setItem("userEmail", email); // Используем userEmail

                        // ОБНОВЛЕНИЕ 2: Обновляем состояние в родительском компоненте App
                        setToken(loginResponse.data.token);
                        setUserEmail(email);

                        navigate("/chat"); // Немедленный переход
                    } else {
                        throw new Error("Login failed after registration (no token).");
                    }
                } catch (loginError) {
                    // Если автологин не удался (ошибка 401, 400 и т.д.)
                    console.error("Auto-login error after successful registration:", loginError);
                    // Переключаемся на экран входа, чтобы пользователь попробовал сам
                    setIsSignUp(false);
                    setMessage("error");
                }
            }
        } catch (error) {
            console.error("Auth error:", error.response || error);
            // 409 Conflict: Пользователь уже существует
            if (error.response?.status === 409) {
                 setMessage("error_exists");
            // 400 Bad Request: Неверный ввод (теперь это либо формат Email, либо длина Пароля < 6)
            } else if (error.response?.status === 400) {
                 setMessage("error_bad_request");
            // Другая ошибка
            } else {
                 setMessage("error");
            }
        } finally {
            setLoading(false);
        }
    };

    // --- Разметка ---
    return (
        <div className={`app ${darkMode ? "dark" : ""}`}>
            {/* 🔘 Переключатель темы */}
            <div
                className="theme-toggle"
                onClick={() => setDarkMode(!darkMode)}
                title="Toggle theme"
            >
                {darkMode ? "☀️" : "🌙"}
            </div>

            {/* 🧩 Основная форма */}
            <div className="form-container">
                <h2>
                    {isSignUp ? "Регистрация" : "Вход"}
                </h2>

                <form onSubmit={handleSubmit}>
                    {/* Поле Email */}
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />

                    {/* Поле Password: Минимум 6 символов, соответствует DTO */}
                    <input
                        type="password"
                        placeholder="Пароль (мин. 6 символов)" // Обновил placeholder
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        minLength={6}
                    />

                    <button
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? <Loader2 className="animate-spin inline mr-2" size={20} /> : (isSignUp ? "Зарегистрироваться" : "Войти")}
                    </button>

                    {message === "error" && (
                        <p className="error">
                            {isSignUp ? "Регистрация не удалась. Ошибка сервера (500) или сети." : "Неверный Email или пароль"}
                        </p>
                    )}
                    {message === "error_bad_request" && (
                        <p className="error">
                            Регистрация не удалась (400). Проверьте требования к Email (формат) и паролю (длина, формат).
                        </p>
                    )}
                    {message === "error_exists" && (
                        <p className="error">
                            Пользователь с таким Email уже существует. Пожалуйста, войдите или используйте другой Email.
                        </p>
                    )}
                    {message === "success" && (
                        // Сообщение отобразится 1 секунду, пока идет автологин
                        <p className="success">
                            {isSignUp ? "Аккаунт успешно создан! Автоматический вход..." : "Вход успешен! Перенаправление..."}
                        </p>
                    )}
                </form>

                {/* 🔄 Переключатель между режимами */}
                <p className="switch-mode">
                    {isSignUp ? (
                        <>
                            Уже есть аккаунт?{" "}
                            <span
                                onClick={() => {
                                    setIsSignUp(false);
                                    setMessage(null);
                                }}
                                className="switch-link"
                            >
                                Войти
                            </span>
                        </>
                    ) : (
                        <>
                            Нет аккаунта?{" "}
                            <span
                                onClick={() => {
                                    setIsSignUp(true);
                                    setMessage(null);
                                }}
                                className="switch-link"
                            >
                                Зарегистрироваться
                            </span>
                        </>
                    )}
                </p>
            </div>
        </div>
    );
};


// --- 3. Главный компонент Приложения с Маршрутизацией (MainApp) ---
const MainApp = () => {
    // Используем 'userEmail' для состояния и localStorage
    const [token, setToken] = useState(localStorage.getItem('jwtToken'));
    const [userEmail, setUserEmail] = useState(localStorage.getItem('userEmail'));

    useEffect(() => {
        if (token && !userEmail) {
            setUserEmail(localStorage.getItem('userEmail'));
        }
    }, [token, userEmail]);

    const handleLogout = () => {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('userEmail'); // Правильный ключ
        setToken(null);
        setUserEmail(null);
    };

    return (
        <Routes>
            {/* Главный маршрут: если есть токен, перенаправляем на чат, иначе на вход */}
            <Route
                path="/"
                element={token
                    ? <Navigate to="/chat" replace />
                    // Передаем setToken и setUserEmail в AuthScreen
                    : <AuthScreen setToken={setToken} setUserEmail={setUserEmail} />
                }
            />

            {/* Маршрут чата: защищен токеном */}
            <Route
                path="/chat"
                element={token && userEmail
                    ? <Chat userEmail={userEmail} onLogout={handleLogout} />
                    : <Navigate to="/" replace />
                }
            />
            {/* Fallback для неизвестных маршрутов */}
            <Route path="*" element={<Navigate to={token ? "/chat" : "/"} replace />} />
        </Routes>
    );
};

// Экспортируем корневой компонент с BrowserRouter
export default function AppRouter() {
    return (
        <BrowserRouter>
            <MainApp />
        </BrowserRouter>
    );
}
