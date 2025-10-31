import React from "react";

export default function StatCard({ title, value, icon }) {
  return (
    <div className="bg-white border border-gray-200 rounded-xl shadow-sm p-6 h-36 flex flex-col items-center justify-center hover:shadow-lg transition">
      <div className="mb-2">{icon}</div>
      <p className="text-lg font-semibold text-gray-600">{title}</p>
      <p className="text-2xl font-bold text-[#2F5664] mt-2">{value ?? "-"}</p>
    </div>
  );
}
