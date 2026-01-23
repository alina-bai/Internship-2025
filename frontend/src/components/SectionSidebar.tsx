useEffect(() => {
  fetch(`${API_BASE_URL}/courses/${courseId}/sections`, {
    headers: { Authorization: `Bearer ${token}` }
  })
    .then(res => res.json())
    .then(setSections);
}, [courseId]);
<button onClick={() => setSelectedSection(section)}>
  {section.title}
</button>

