import React, { useState } from 'react';
import MajorDetailModal from './06.5MajorDetailModal';

const majorDetails = {
  safety: {
    name: '보건환경안전학과',
    intro: '인간의 건강과 안전, 지속 가능한 환경을 융합과학적으로 연구하는 학과',
    careers: [
      '안전보건공단, 환경공단 등 공기업',
      '보건직, 환경직 공무원',
      '기업체의 산업보건 관리자, 환경안전 관리자 등'
    ],
    basics: ['일반화학', '공중보건학', '산업안전개론', '산업위생학']
  },
  bio: {
    name: '바이오생명공학과',
    intro: '생명의 존엄성과 생물 환경의 중요성을 이해하고 생명과학 발달에 기여하는 학과',
    careers: ['국공립기관 및 연구소', '대기업 연구소', '제약회사', '대학병원 연구소 등'],
    basics: ['생물학연구법', '기초생물학', '일반생물학', '일반화학']
  },
  food: {
    name: '식품조리학부',
    intro: '식생활 이론을 바탕으로 인류의 건강 증진 및 삶의 질 향상을 위한 전문가 양성',
    careers: ['영양사', '식품기사', '쉐프', '외식 창업', '메뉴 개발자 등'],
    basics: ['식품학', '외식경영론', '기초영양학', '조리원리']
  },
  ai: {
    name: 'AI융합학부',
    intro: 'AI, 빅데이터, 소프트웨어 기술 기반 창의적 문제 해결 전문가 양성',
    careers: ['소프트웨어 개발자', '데이터 과학자', '서비스 기획자', '공무원 등'],
    basics: ['인공지능의 이해', 'AI프로그래밍', '확률과 통계']
  }
};

export default function MajorList() {
  const [selected, setSelected] = useState(null);

return (
  <div className="bg-white rounded-2xl shadow-sm border border-gray-300 w-full h-full min-h-[465px] mt-6">
    {/* 헤더 */}
    <div className="px-6 py-4 border-b border-gray-200">
      <h2 className="text-xl font-bold text-center">〈 자율전공 탐색 〉</h2>
    </div>

    {/* 본문 */}
    <div className="p-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {Object.entries(majorDetails).map(([key, major]) => (
          <div
            key={key}
            onClick={() => setSelected(major)}
            className="cursor-pointer bg-white rounded-xl border border-gray-300 shadow-sm hover:shadow-md hover:-translate-y-0.5 transition p-5 h-[200px] flex flex-col justify-between"
          >
            <h3 className="text-base font-semibold">{major.name}</h3>
            <p className="text-sm text-gray-600 mt-2 line-clamp-3">{major.intro}</p>
          </div>
        ))}
      </div>
    </div>

    {/* 모달 */}
    {selected && (
      <MajorDetailModal
        major={selected}
        onClose={() => setSelected(null)}
        backdropClass="fixed inset-0 bg-white/30 backdrop-blur-sm flex items-center justify-center z-50"
      />
    )}
  </div>
);
}
