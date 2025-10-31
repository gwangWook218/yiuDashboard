import React, { useEffect, useState } from "react";
import { FaUserGraduate, FaChalkboardTeacher, FaGlobe } from "react-icons/fa";
import StatCard from "./StatCard";
import { getPublic } from "../lib/publicApi";

const YEAR_DEFAULT = 2024;

const defs = [
  { key: "students", title: "재학생 수", path: "/api/public/main/students", params: { year: YEAR_DEFAULT }, icon: <FaUserGraduate size={24} className="text-[#2F5664]" /> },
  // ✅ 교원 수 API 연동으로 변경
  { key: "faculty", title: "교원 수", path: "/api/public/main/faculty", params: { year: YEAR_DEFAULT }, icon: <FaChalkboardTeacher size={24} className="text-[#2F5664]" /> },
  { key: "staff", title: "임직원 수", path: "/api/public/main/staff", params: {}, icon: <FaGlobe size={24} className="text-[#2F5664]" /> },
  { key: "foreign", title: "외국인 유학생 수", path: "/api/public/main/students/foreign", params: { year: YEAR_DEFAULT }, icon: <FaUserGraduate size={24} className="text-[#2F5664]" /> },
  { key: "scholarship", title: "1인당 장학금", path: "/api/public/main/scholarship", params: { year: YEAR_DEFAULT }, icon: <FaGlobe size={24} className="text-[#2F5664]" /> },
  { key: "employment", title: "졸업생 취업률", path: "/api/public/main/students/graduate", params: { year: YEAR_DEFAULT }, icon: <FaChalkboardTeacher size={24} className="text-[#2F5664]" /> },
];

// 문자열 → 숫자 변환
const toNum = (v) => {
  if (typeof v === "number") return v;
  if (typeof v === "string") {
    const n = Number(v.replace(/[, ]/g, ""));
    return Number.isFinite(n) ? n : null;
  }
  return null;
};

function formatValue(key, data, def) {
  if (def?.value) return def.value;
  if (!data) return "—";

  // ✅ 배열로 오는 경우
  if (Array.isArray(data) && data.length > 0) {
    const first = data[0];

    if (key === "faculty") {
      const n = toNum(first.value ?? first.faculty_count ?? first.count);
      if (n !== null) return `${n.toLocaleString()}명`;
    }
    if (key === "students" || key === "foreign" || key === "staff") {
      const n = toNum(first.value ?? first.count ?? first.staff_count);
      if (n !== null) return `${n.toLocaleString()}명`;
    }
    if (key === "scholarship") {
      const n = toNum(first.amount ?? first.value);
      if (n !== null) return `${Math.round(n).toLocaleString()}원`;
    }
    if (key === "employment") {
      const n = toNum(first.rate ?? first.value);
      if (n !== null) return `${n.toFixed(1)}%`;
    }
  }

  // ✅ 객체로 오는 경우
  if (typeof data === "object") {
    if (key === "faculty") {
      const n = toNum(data.faculty_count ?? data.value ?? data.count);
      if (n !== null) return `${n.toLocaleString()}명`;
    }
    if (key === "scholarship") {
      const n = toNum(data.amount ?? data.value);
      if (n !== null) return `${Math.round(n).toLocaleString()}원`;
    }
    if (key === "employment") {
      const n = toNum(data.rate ?? data.value);
      if (n !== null) return `${n.toFixed(1)}%`;
    }
    if ("staff_count" in data) {
      const n = toNum(data.staff_count);
      if (n !== null) return `${n.toLocaleString()}명`;
    }
    if ("count" in data || "value" in data) {
      const n = toNum(data.count ?? data.value);
      if (n !== null) return `${n.toLocaleString()}명`;
    }
  }

  return "—";
}

export default function PublicStats() {
  const [stats, setStats] = useState({});
  const [error, setError] = useState("");

  useEffect(() => {
    let cancel = false;
    (async () => {
      try {
        const normalDefs = defs.filter((d) => !d.value);
        const settled = await Promise.allSettled(
          normalDefs.map(async (d) => [d.key, await getPublic(d.path, d.params)])
        );
        const result = {};
        for (const s of settled) {
          if (s.status === "fulfilled") {
            const [key, value] = s.value;
            result[key] = value;
          }
        }
        console.log("📊 [DEBUG] stats:", result);
        if (!cancel) setStats(result);
      } catch (e) {
        if (!cancel) setError(e.message ?? String(e));
      }
    })();
    return () => { cancel = true; };
  }, []);

  if (error) return <div className="text-red-600">불러오기 실패: {error}</div>;

  return (
    <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
      {defs.map((def) => (
        <StatCard
          key={def.key}
          title={def.title}
          icon={def.icon}
          value={formatValue(def.key, stats[def.key], def)}
        />
      ))}
    </div>
  );
}
