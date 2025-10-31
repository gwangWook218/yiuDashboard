import React from 'react';
import { X } from 'lucide-react';

export default function MajorDetailModal({ major, onClose }) {
  return (
    <div className="fixed inset-0 bg-white/30 backdrop-blur-sm z-50 flex items-center justify-center">
      <div className="bg-white w-full max-w-4xl rounded-2xl shadow-lg relative p-8">
        {/* 닫기 버튼 */}
        <button onClick={onClose} className="absolute top-4 right-4 text-gray-600 hover:text-black">
          <X size={20} />
        </button>

        <h2 className="text-3xl font-bold text-center text-[#3B7F91] mb-4">{major.name}</h2>
        <hr className="mb-6 border-gray-300" />

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {/* 학과 소개 */}
          <div className="bg-blue-50 p-4 rounded-xl shadow">
            <h3 className="text-lg font-semibold text-blue-800 mb-2">학과 소개</h3>
            <p className="text-gray-700 text-sm">{major.intro}</p>
          </div>

          {/* 졸업 후 진로 */}
          <div className="bg-yellow-50 p-4 rounded-xl shadow">
            <h3 className="text-lg font-semibold text-yellow-800 mb-2">졸업 후 진로</h3>
            <ul className="text-gray-700 text-sm list-disc list-inside space-y-1">
              {major.careers.map((job, i) => (
                <li key={i}>{job}</li>
              ))}
            </ul>
          </div>

          {/* 기초 전공과목 */}
          <div className="bg-green-50 p-4 rounded-xl shadow">
            <h3 className="text-lg font-semibold text-[#3B7F91] mb-2">기초 전공과목</h3>
            <div className="flex flex-wrap gap-2">
              {major.basics.map((subject, i) => (
                <span key={i} className="bg-green-100 px-3 py-1 rounded-full text-sm">
                  {subject}
                </span>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
