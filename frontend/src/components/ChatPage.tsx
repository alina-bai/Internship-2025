import { useState } from "react";
import SectionSidebar from "../components/SectionSidebar";
import SectionContent from "../components/SectionContent";
import ChatSidebar from "../components/ChatSidebar";
import ChatWindow from "../components/ChatWindow";

type Section = {
    id: number;
    title: string;
    content: string;
};

export default function ChatPage() {
    const [selectedSection, setSelectedSection] = useState<Section | null>(null);
    const [selectedSessionId, setSelectedSessionId] = useState<number | null>(null);

    const courseId = 1;

    return (
        <div style={{ display: "flex", height: "100vh" }}>
            <SectionSidebar courseId={courseId} setSelectedSection={setSelectedSection} />

            <div style={{ flex: 1, display: "flex", flexDirection: "column" }}>
                <div style={{ flex: 1, overflowY: "auto", padding: "1rem" }}>
                    <SectionContent section={selectedSection} />
                </div>

                <div style={{ display: "flex", flex: 1, borderTop: "1px solid #ccc" }}>
                    <ChatSidebar
                        selectedSessionId={selectedSessionId}
                        onSelectSession={setSelectedSessionId}
                    />
                    {selectedSessionId !== null && <ChatWindow sessionId={selectedSessionId} />}
                </div>
            </div>
        </div>
    );
}