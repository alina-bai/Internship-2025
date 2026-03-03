type Props = {
  selectedSessionId: number | null;
  onSelectSession: (id: number) => void;
};

export default function ChatSidebar({ selectedSessionId, onSelectSession }: Props) {
  const sessions = [
    { id: 1, title: "General" },
    { id: 2, title: "Course Q&A" }
  ];

  return (
      <div style={{ width: 200, borderRight: "1px solid #ccc", padding: "1rem" }}>
        <h3>Chats</h3>
        {sessions.map(s => (
            <button
                key={s.id}
                onClick={() => onSelectSession(s.id)}
                style={{
                  display: "block",
                  marginBottom: "0.5rem",
                  backgroundColor: selectedSessionId === s.id ? "#ddd" : "#fff"
                }}
            >
              {s.title}
            </button>
        ))}
      </div>
  );
}