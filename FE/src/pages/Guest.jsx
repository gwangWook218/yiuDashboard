import React, { useState, useEffect } from "react"; 
// IoIosSearch를 io 라이브러리에서 가져오도록 명확히 분리
import { FaUserGraduate, FaGlobe, FaUserTie } from "react-icons/fa"; 
import { IoMdRibbon, IoIosSearch } from "react-icons/io"; 

import GuestTopNav from "../components/GuestTopNav";
import Footer from "../components/Footer";
import KakaoMap from "../components/KakaoMap";
import facilities from "../data/facilities";
import HeroHeader from "../components/HeroHeader";
import AdmissionChart from "../components/AdmissionChart";
import { UniversityOrgChartCard } from "../components/OrgTree";
import ParkingGuide from "../components/ParkingGuide";
import FacilityPanel from "../components/FacilityPanel";
import BasicStatModal from "../components/09.5BasicStatModal"; 
import { getPublic } from "../lib/publicApi"; 

// =========================================================
// 💡 PublicStats.jsx 에서 가져온 통계 정의 및 로직
// =========================================================
const YEAR_DEFAULT = 2024; // 기준 연도 설정

const STAT_DEFS = [
  // 재학생 수 (students) - 클릭 가능
  { key: "students", title: "재학생 수", path: "/api/public/main/students", params: { year: YEAR_DEFAULT }, icon: <FaUserGraduate size={24} className="text-[#2F5664]" />, isClickable: true, onClickType: 'students' },
  // 졸업생 취업률 (employment) - 클릭 가능
  { key: "employment", title: "졸업생 취업률", path: "/api/public/main/students/graduate", params: { year: YEAR_DEFAULT }, icon: <IoMdRibbon size={24} className="text-[#2F5664]" />, isClickable: true, onClickType: 'recruitment' }, 
  // 임직원 수 (staff) - 클릭 불가
  { key: "staff", title: "임직원 수", path: "/api/public/main/staff", params: {}, icon: <FaUserTie size={24} className="text-[#2F5664]" />, isClickable: false },
  // 외국인 유학생 수 (foreign) - 클릭 불가
  { key: "foreign", title: "외국인 유학생 수", path: "/api/public/main/students/foreign", params: { year: YEAR_DEFAULT }, icon: <FaUserGraduate size={24} className="text-[#2F5664]" />, isClickable: false },
  // 1인당 장학금 (scholarship) - 클릭 불가
  { key: "scholarship", title: "1인당 장학금", path: "/api/public/main/scholarship", params: { year: YEAR_DEFAULT }, icon: <FaGlobe size={24} className="text-[#2F5664]" />, isClickable: false }, 
  // 교원 수 (faculty) - 클릭 불가
  { key: "faculty", title: "교원 수", path: "/api/public/main/faculty", params: { year: YEAR_DEFAULT }, icon: <FaUserTie size={24} className="text-[#2F5664]" />, isClickable: false },
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

// 데이터 포맷팅 함수 (PublicStats.jsx에서 가져옴)
function formatValue(key, data) {
  if (!data) return "—";

  // ✅ 배열로 오는 경우
  if (Array.isArray(data) && data.length > 0) {
    const first = data[0];
    const n = toNum(first.value ?? first.count ?? first.faculty_count ?? first.staff_count ?? first.amount ?? first.rate);
    if (n === null) return "—";

    if (key === "faculty" || key === "students" || key === "foreign" || key === "staff") {
      return `${n.toLocaleString()}명`;
    }
    if (key === "scholarship") {
      // 1인당 장학금은 만원 단위로 표시하거나, 원 단위로 표시 후 반올림
      return `${Math.round(n / 10000).toLocaleString()}만원`; 
    }
    if (key === "employment") {
      return `${n.toFixed(1)}%`;
    }
  }

  // ✅ 객체로 오는 경우 (배열 처리와 유사)
   if (typeof data === "object") {
    const n = toNum(data.value ?? data.count ?? data.faculty_count ?? data.staff_count ?? data.amount ?? data.rate);
    if (n === null) return "—";

    if (key === "faculty" || key === "students" || key === "foreign" || key === "staff") {
      return `${n.toLocaleString()}명`;
    }
    if (key === "scholarship") {
      return `${Math.round(n / 10000).toLocaleString()}만원`; 
    }
    if (key === "employment") {
      return `${n.toFixed(1)}%`;
    }
  }

  return "—";
}
// =========================================================

export default function Guest() {
  const DEFAULT_NAME = "학생회관(인성관)";
  const defaultFacility = facilities.find(f => f.name === DEFAULT_NAME) ?? null;
  const [selected, setSelected] = useState(defaultFacility);
  
  // 모달 상태 관리
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(null); 
  
  // 💡 API 연동 상태
  const [statsData, setStatsData] = useState({}); // API 응답 원본 데이터
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  const handleStatClick = (type) => {
    setModalType(type);
    setShowModal(true);
  };

  // 💡 API 연동 로직
  useEffect(() => {
    let cancel = false;
    setIsLoading(true);
    (async () => {
      try {
        const settled = await Promise.allSettled(
          STAT_DEFS.map(async (d) => [d.key, await getPublic(d.path, d.params)])
        );
        const result = {};
        for (const s of settled) {
          if (s.status === "fulfilled") {
            const [key, value] = s.value;
            result[key] = value;
          }
        }
        if (!cancel) {
          setStatsData(result);
          setIsLoading(false);
        }
      } catch (e) {
        if (!cancel) {
          setError(e.message ?? String(e));
          setIsLoading(false);
        }
      }
    })();
    return () => { cancel = true; };
  }, []);

  // 💡 표시될 최종 통계 데이터 (로딩 상태 처리)
  const statsToDisplay = STAT_DEFS.map(def => {
    const formattedValue = formatValue(def.key, statsData[def.key]);
    return {
      ...def,
      // 💡 수정된 부분: Duplicate Key 오류 수정
      value: isLoading ? '...' : formattedValue
    }
  });
  
  // 로딩 및 에러 처리 UI
  const StatsBody = () => {
      if (error) return <div className="text-red-600 p-4">통계 불러오기 실패: {error}</div>;
      
      return (
        <div className="grid grid-cols-2 md:grid-cols-3 gap-6">
          {statsToDisplay.map((s, i) => (
            <div
              key={i}
              title={s.isClickable ? `${s.title} 상세 분석 보기` : undefined}
              className={`bg-white rounded-xl shadow-sm p-4 text-center border border-gray-200 
                  flex flex-col justify-center items-center h-[140px] min-h-[120px] 
                  ${s.isClickable ? 'cursor-pointer hover:shadow-lg transition' : ''}`}
              onClick={s.isClickable ? () => handleStatClick(s.onClickType) : undefined}
            >
              <div className="flex flex-col justify-center items-center w-full">
                {/* 아이콘 및 제목 */}
                <div className="flex items-center justify-center space-x-2 mb-1">
                  {s.icon}
                  <h3 className="text-sm font-semibold text-gray-700">{s.title}</h3>
                </div>
                {/* 값 */}
                <p className="text-3xl font-bold text-[#2F5664]">{s.value}</p>
                
                {/* 팝업 안내 문구 추가 */}
                {s.isClickable && (
                  <p className="flex items-center text-xs text-[#028EA7] font-medium mt-1">
                    <IoIosSearch size={14} className="mr-1" />
                    상세 분석 보기
                  </p>
                )}
              </div>
            </div>
          ))}
        </div>
      );
  };


  return (
    <div className="bg-gray-100 min-h-screen">
      <GuestTopNav />
      <div className="h-14" />

      <main className="flex-1 p-6 space-y-8 w-full max-w-none">
        {/* Hero */}
        <HeroHeader />

        {/* KPI + 파이차트 */}
        <section id="admission" className="grid grid-cols-1 lg:grid-cols-[2fr_1fr] gap-6 items-stretch">
          <div className="bg-white rounded-2xl shadow-sm p-6 border border-gray-200 h-full">
            <h2 className="text-lg font-bold mb-4">학교 통계 ({YEAR_DEFAULT}년 현재)</h2>
            {/* 💡 API 연동된 통계 데이터 렌더링 */}
            <StatsBody />
          </div>

          <div className="bg-white rounded-2xl shadow-sm p-6 border border-gray-200 h-full flex flex-col">
            <h2 className="text-lg font-bold mb-4">2025년도 입시 정보</h2>
            <div className="flex-1 flex flex-col">
              <AdmissionChart />
            </div>
          </div>
        </section>

        {/* 조직도 */}
        <section id="org">
          <UniversityOrgChartCard />
        </section>

        {/* 주차 */}
        <section id="parking" className="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
          <h2 className="text-xl font-bold mb-4">주차안내</h2>
          <ParkingGuide />
        </section>

        {/* 지도 + 시설 패널 */}
        <section id="map" className="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
          <h2 className="text-xl font-bold mb-4">캠퍼스맵</h2>
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2">
              <KakaoMap
                center={{ lat: 37.227302, lng: 127.168191 }}
                level={3}
                facilities={facilities}
                height={500}
                onSelect={setSelected}
              />
            </div>
            <FacilityPanel selected={selected} />
          </div>
        </section>
      </main>

      <Footer />
      
      {/* 모달 컴포넌트 렌더링 */}
      <BasicStatModal
        show={showModal}
        onClose={() => setShowModal(false)}
        statType={modalType}
      />
    </div>
  );
}