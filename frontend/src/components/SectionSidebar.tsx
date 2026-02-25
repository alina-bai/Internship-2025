import { useEffect, useState } from "react";

type Section = {
  id: number;
  title: string;
  content: string;
};

type Props = {
  courseId: number;
  setSelectedSection: (section: Section) => void;
};

export default function SectionSidebar({ courseId, setSelectedSection }: Props) {
  const [sections, setSections] = useState<Section[]>([]);
  const token = localStorage.getItem("token");
  const API_BASE_URL = "http://localhost:8080/api";

  useEffect(() => {
    if (!token) return;

    fetch(`${API_BASE_URL}/courses/${courseId}/sections`, {
      headers: { Authorization: `Bearer ${token}` }
    })
        .then(res => res.json())
        .then(setSections)
        .catch(console.error);
  }, [courseId, token]);

  return (
      <div style={{ width: 200, borderRight: "1px solid #ccc", padding: "1rem" }}>
        <h3>Sections</h3>
        {sections.map(section => (
            <button
                key={section.id}
                onClick={() => setSelectedSection(section)}
                style={{ display: "block", marginBottom: "0.5rem" }}
            >
              {section.title}
            </button>
        ))}
      </div>
  );
}