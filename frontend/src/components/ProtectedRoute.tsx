import React from "react";
import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
  children: JSX.Element;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const token = localStorage.getItem("jwtToken");

  if (!token) {
    alert("You must be logged in to access the chat");
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
