// import React from "react"; // не нужно в React 17+
import ReactMarkdown from "react-markdown";

type Section = {
    id: number;
    title: string;
    content: string;
};

type Props = {
    section: Section | null;
};

export default function SectionContent({ section }: Props) {
    if (!section) return <div>Выберите раздел</div>;

    return (
        <div>
            <h2>{section.title}</h2>
            <ReactMarkdown>{section.content}</ReactMarkdown>
        </div>
    );
}