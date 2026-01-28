import { useState } from "react";
import { useParams } from "react-router-dom";
import SectionSidebar from "../components/SectionSidebar";
import SectionContent from "../components/SectionContent";
import SectionChat from "../components/SectionChat";

export default function CoursePage() {
  const { courseId } = useParams();
  const [selectedSection, setSelectedSection] = useState(null);

  return (
    <div className="flex h-screen">
      {/* LEFT: sections list */}
      <SectionSidebar
        courseId={courseId}
        onSelectSection={setSelectedSection}
      />

      {/* CENTER: markdown content */}
      <div className="flex-1 p-4 overflow-y-auto">
        <SectionContent section={selectedSection} />
      </div>

      {/* RIGHT: AI assistant */}
      <div className="w-[400px] border-l">
        <SectionChat section={selectedSection} />
      </div>
    </div>
  );
}
