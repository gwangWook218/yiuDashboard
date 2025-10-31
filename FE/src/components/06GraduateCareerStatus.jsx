import React, { useState } from "react";

/** ───────────────────────── 데이터 정의 구역 ───────────────────────── */
const JOB_PORTALS = [
  { key: "worknet", name: "워크넷", url: "https://www.work.go.kr", color: "#2BA6DE" },
  { key: "jobkorea", name: "잡코리아", url: "https://www.jobkorea.co.kr", color: "#1F8ACB" },
  { key: "moel", name: "고용노동부", url: "https://www.moel.go.kr", color: "#009688" },
  { key: "jobplanet", name: "잡플래닛", url: "https://www.jobplanet.co.kr", color: "#00C362" },
  { key: "incruit", name: "인크루트", url: "https://www.incruit.com", color: "#FF6B00" },
  // --- 용인대학교 추가 링크 ---
  { key: "cde", name: "원격교육캠퍼스(CDE)", url: "https://cde.yongin.ac.kr/home/mainHome/Form/main", color: "#6A5ACD" },
  { key: "total", name: "종합정보서비스", url: "https://total.yongin.ac.kr/login.do", color: "#483D8B" },
  { key: "yes", name: "엘리트역량개발(YES)", url: "https://yes.yongin.ac.kr/", color: "#8A2BE2" },
  // --- 효과를 위해 항목을 복제합니다. ---
  { key: "worknet2", name: "워크넷", url: "https://www.work.go.kr", color: "#2BA6DE" },
  { key: "jobkorea2", name: "잡코리아", url: "https://www.jobkorea.co.kr", color: "#1F8ACB" },
  { key: "moel2", name: "고용노동부", url: "https://www.moel.go.kr", color: "#009688" },
  { key: "cde2", name: "원격교육캠퍼스(CDE)", url: "https://cde.yongin.ac.kr/home/mainHome/Form/main", color: "#6A5ACD" },
  { key: "total2", name: "종합정보서비스", url: "https://total.yongin.ac.kr/login.do", color: "#483D8B" },
  { key: "yes2", name: "엘리트역량개발(YES)", url: "https://yes.yongin.ac.kr/", color: "#8A2BE2" },
  { key: "jobplanet2", name: "잡플래닛", url: "https://www.jobplanet.co.kr", color: "#00C362" },
  { key: "incruit2", name: "인크루트", url: "https://www.incruit.com", color: "#FF6B00" },
];


/* 로고를 위한 원형 컴포넌트 */
function CircleLogo({ text, color }) {
  // 텍스트 길이에 따라 약어를 결정
  let display = text;
  if (text === "원격교육캠퍼스(CDE)") display = "CDE";
  else if (text === "종합정보서비스") display = "정보";
  else if (text === "엘리트역량개발(YES)") display = "YES";
  else if (text === "고용노동부") display = "고노";
  else if (text === "워크넷") display = "워크";
  else if (text === "잡플래닛") display = "잡플";
  else if (text === "인크루트") display = "인크";
  else if (text === "잡코리아") display = "잡코";
  else display = "GO";
  
  return (
    <div
      className="w-10 h-10 rounded-full flex items-center justify-center font-bold text-white shadow shrink-0 text-sm"
      style={{ backgroundColor: color }}
    >
      {display}
    </div>
  );
}

export default function GraduateCareerStatus() {

  // CSS 애니메이션을 React 컴포넌트 내부에 정의
  const style = `
    @keyframes marquee {
      0% { transform: translateX(0); }
      100% { transform: translateX(-50%); } /* 항목을 두 배로 복제했으므로 50% 이동하면 처음 상태로 돌아옴 */
    }

    .marquee-container {
      overflow: hidden;
      width: 100%;
      /* 마스킹을 조정하여 양 끝이 더 부드럽게 시작/끝나도록 합니다. */
      mask-image: linear-gradient(to right, 
        hsl(0 0% 0% / 0) 0%, 
        hsl(0 0% 0% / 1) 5%, 
        hsl(0 0% 0% / 1) 95%, 
        hsl(0 0% 0% / 0) 100%
      );
    }

    .marquee-content {
      display: flex;
      gap: 1.5rem; /* Tailwind 'gap-6'에 가깝게 조정 */
      animation: marquee 30s linear infinite; /* 30초 동안 선형 무한 반복 */
      width: max-content;
    }

    /* 마우스 오버 시 일시 정지 */
    .marquee-content:hover {
      animation-play-state: paused;
    }

    /* 개별 항목 스타일 */
    .marquee-item {
      min-width: 250px; /* 각 항목의 최소 너비 설정 */
    }
  `;


  return (
    <div className="bg-white rounded-2xl shadow-xl border border-gray-200 p-0 w-full mx-auto mt-6">
      
      <style>{style}</style>
      
      {/* 타이틀 섹션 복구 */}
      <div className="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
        <h1 className="text-xl font-bold text-slate-800">취업 및 교내 정보 바로가기</h1>
      </div>
      
      <div className="p-4"> 
        <div className="marquee-container rounded-xl border border-gray-300 shadow-inner p-3 bg-slate-50">
          <div className="marquee-content">
            {JOB_PORTALS.map(({ key, name, url, color }) => (
              <a
                key={key}
                href={url}
                target="_blank"
                rel="noopener noreferrer"
                className="marquee-item group inline-flex items-center gap-3 p-3 rounded-xl border border-gray-300 shadow-md transition bg-white"
                aria-label={`${name} 새 탭에서 열기`}
                title={`${name} 바로가기`}
              >
                <CircleLogo name={name} text={name} color={color} />
                <span className="text-base font-medium text-slate-800 group-hover:text-[#028EA7]">
                  {name}
                </span>
              </a>
            ))}
          </div>
        </div>
        
      </div>
      
    </div>
  );
}
