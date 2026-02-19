import axios from "axios";

// ✅ Создаём экземпляр axios с базовым URL вашего backend
const api = axios.create({
  baseURL: "http://localhost:8080/api", // замени на адрес сервера, если нужно
  headers: {
    "Content-Type": "application/json",
  },
});

// ✅ Перехватчик запроса: добавляем JWT-токен в заголовок Authorization
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("jwtToken"); // читаем токен из хранилища
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; // добавляем токен в запрос
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// ✅ Обработка ошибок (например, если токен устарел)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn("Unauthorized — please log in again");
      localStorage.removeItem("jwtToken");
      localStorage.removeItem("username");
      window.location.href = "/login"; // редирект на логин
    }
    return Promise.reject(error);
  }
);

export default api;
