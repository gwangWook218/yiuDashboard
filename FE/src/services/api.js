const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const fetchStudentCount = async (year) => {
  const res = await fetch(`${BASE_URL}/public/students/${year}`);
  const xml = await res.text();
  const parser = new DOMParser();
  const data = parser.parseFromString(xml, "text/xml");
  const count = data.querySelector("indctVal1")?.textContent;
  return Number(count);
};

export const fetchGraduateRate = async (year) => {
  const res = await fetch(`${BASE_URL}/public/students/graduateRate/${year}`);
  const xml = await res.text();
  const parser = new DOMParser();
  const data = parser.parseFromString(xml, "text/xml");
  return {
    schoolRate: parseFloat(data.querySelector("indctVal4")?.textContent),
    nationalAvg: parseFloat(data.querySelector("indctAvg")?.textContent),
  };
};

export const fetchForeignStudents = async (year) => {
  const res = await fetch(`${BASE_URL}/public/students/foreign/${year}`);
  const xml = await res.text();
  const parser = new DOMParser();
  const data = parser.parseFromString(xml, "text/xml");
  const count = data.querySelector("indctVal1")?.textContent;
  return Number(count);
};