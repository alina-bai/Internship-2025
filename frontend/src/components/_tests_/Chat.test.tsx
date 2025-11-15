import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import axios from "axios";
import Chat from "../Chat"; // путь может отличаться, если у тебя другая структура

jest.mock("axios");
const mockedAxios = axios as jest.Mocked<typeof axios>;

describe("Chat Component", () => {
  beforeEach(() => {
    localStorage.setItem("jwtToken", "fake-jwt-token");
  });

  afterEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });

  test("renders input and button correctly", () => {
    render(<Chat />);
    expect(screen.getByPlaceholderText("Type your message...")).toBeInTheDocument();
    expect(screen.getByRole("button", { name: /send/i })).toBeInTheDocument();
  });

  test("sends message and displays AI response", async () => {
    mockedAxios.post.mockResolvedValueOnce({
      data: { reply: "Hello, human!" },
    });

    render(<Chat />);

    const input = screen.getByPlaceholderText("Type your message...");
    const button = screen.getByRole("button", { name: /send/i });

    fireEvent.change(input, { target: { value: "Hi AI" } });
    fireEvent.click(button);

    await waitFor(() => {
      expect(mockedAxios.post).toHaveBeenCalledWith(
        "http://localhost:8080/api/chat",
        { message: "Hi AI" },
        expect.objectContaining({
          headers: expect.objectContaining({
            Authorization: "Bearer fake-jwt-token",
          }),
        })
      );

      expect(screen.getByText("Hi AI")).toBeInTheDocument();
      expect(screen.getByText("Hello, human!")).toBeInTheDocument();
    });
  });
});
