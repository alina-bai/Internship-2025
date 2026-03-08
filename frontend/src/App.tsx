// ВСТРОЕННОЕ (из библиотеки react-router-dom)
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

// НАШИ страницы
import AuthPage from "./pages/AuthPage";
import ChatPage from "./pages/ChatPage";
import CoursePage from "./pages/CoursePage";

// НАШ компонент защиты
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
    return (
        <BrowserRouter>
            <Routes>

                {/* Страница логина и регистрации */}
                <Route path="/" element={<AuthPage />} />

                {/* Защищённые страницы */}
                <Route element={<ProtectedRoute />}>

                    <Route path="/chat" element={<ChatPage />} />
                    <Route path="/courses" element={<CoursePage />} />

                </Route>

                {/* Если путь не найден */}
                <Route path="*" element={<Navigate to="/" />} />

            </Routes>
        </BrowserRouter>
    );
}

export default App;