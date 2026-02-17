import React, { useState } from "react";
import ChatSidebar from "./components/ChatSidebar";
import ChatWindow from "./components/ChatWindow";

const App = () => {
  const [selectedSessionId, setSelectedSessionId] = useState<number | null>(null);

  return (
    <div style={{ display: "flex", height: "100vh" }}>
      <ChatSidebar
        selectedSessionId={selectedSessionId}
        onSelectSession={setSelectedSessionId}
      />

      <ChatWindow sessionId={selectedSessionId} />
    </div>
  );
};

export default App;
