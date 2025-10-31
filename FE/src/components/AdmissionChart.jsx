import React from "react";
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from "recharts";

const COLORS = ["#2F5664", "#4A90E2", "#F5A623"];
const admissionData = [
  { name: "정시", value: 27.4 },
  { name: "수시", value: 72.6 },
  { name: "특별전형", value: 10 },
];

export default function AdmissionChart() {
  return (
    <div className="flex flex-col items-center">
      <ResponsiveContainer width="100%" height={200}>
        <PieChart>
          <Pie data={admissionData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={70} label>
            {admissionData.map((entry, i) => (
              <Cell key={i} fill={COLORS[i % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip />
          <Legend verticalAlign="bottom" height={36} />
        </PieChart>
      </ResponsiveContainer>

      <a
        href="https://ipsi.yongin.ac.kr/intro.html"
        target="_blank"
        rel="noopener noreferrer"
        className="mt-8 px-6 py-2 bg-[#2F5664] text-white text-sm rounded-lg shadow hover:bg-[#24404d] transition"
      >
        입학처 바로가기
      </a>
    </div>
  );
}
