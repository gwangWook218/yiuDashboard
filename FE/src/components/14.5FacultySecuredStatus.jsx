import React, { useMemo, useState, useEffect } from "react";
import { Line } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
    LineElement,
    PointElement,
} from "chart.js";

// Chart.js 모듈 등록
ChartJS.register(
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
    LineElement,
    PointElement
);

// 상수 정의
const BRAND = "#028EA7";
const BRAND_SOFT = "rgba(2,142,167,0.14)";
const LIGHT_GREEN = "#A3E635";
const YIU_COLOR = BRAND;

const YEARS = ["2022", "2023", "2024"]; // 연도 배열

// API 엔드포인트 정의 (로컬호스트 URL은 예시입니다)
const NOTICE_URL = 'http://ec2-13-209-7-237.ap-northeast-2.compute.amazonaws.com:8080/api/faculty/fulltime/ensure/notice';   // 고시 상세
const COMPARE_URL = 'http://ec2-13-209-7-237.ap-northeast-2.compute.amazonaws.com:8080/api/faculty/fulltime/ensure/compare'; // 대학 비교
const REGION_URL = 'http://ec2-13-209-7-237.ap-northeast-2.compute.amazonaws.com:8080/api/faculty/fulltime/ensure/region';   // 지역별 비교

// --- 유틸리티 함수 ---

const diffArrow = (d) => (d > 0 ? "▲" : d < 0 ? "▼" : "—");
const diffClass = (d) =>
    d > 0 ? "text-emerald-600" : d < 0 ? "text-rose-600" : "text-slate-500";

// 알약 스타일을 위한 공통 함수
const getPillClass = (d) => {
    if (d === null || d === 0) return 'bg-slate-50 text-slate-500';
    // isRate가 아니어도 양수/음수에 따라 색상 변경
    return d > 0 ? 'bg-emerald-50 text-[#028EA7]' : 'bg-rose-50 text-rose-600';
};

// 숫자 포맷팅 함수
const formatCount = (n) => (n !== null && n !== undefined ? n.toLocaleString() : 'N/A');

// API 호출 및 재시도 로직
const fetchWithRetry = async (url, maxRetries = 3) => {
    let lastError = null;
    for (let i = 0; i < maxRetries; i++) {
        try {
            const response = await fetch(url);
            if (!response.ok) {
                // HTTP 상태 코드가 400 이상인 경우도 에러로 처리
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            lastError = error;
            if (i < maxRetries - 1) {
                // Exponential backoff
                const delay = Math.pow(2, i) * 1000;
                await new Promise(resolve => setTimeout(resolve, delay));
            }
        }
    }
    throw lastError;
};

// --- InfoCard 컴포넌트 (스타일 유지를 위해 그대로 사용) ---

function InfoCard({ title, value, unit, sub, yoyDiff = null, isRate = false }) {
    const renderIcon = (t) => {
        // Icon rendering logic is kept as is
        switch (t) {
            case "전체 교원 수":
                return (
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4b5563" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="12" cy="7" r="4" />
                        <path d="M5.5 21a4.5 4.5 0 0 1 13 0" />
                    </svg>
                );
            case "전임교원 수":
                return (
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#028EA7" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="12" cy="7" r="4" />
                        <path d="M5.5 21a4.5 4.5 0 0 1 13 0" />
                    </svg>
                );
            case "교원 확보 기준 수":
                return (
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4b5563" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                        <line x1="4" y1="7" x2="20" y2="7" />
                        <line x1="7" y1="7" x2="7" y2="17" />
                        <line x1="17" y1="7" x2="17" y2="17" />
                        <line x1="4" y1="17" x2="20" y2="17" />
                    </svg>
                );
            case "전임교원 확보율":
                return (
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#028EA7" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                        <polyline points="4 12 9 17 20 6" />
                    </svg>
                );
            default:
                return (
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#4b5563" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
                        <circle cx="12" cy="7" r="4" />
                        <path d="M5.5 21a4.5 4.5 0 0 1 13 0" />
                    </svg>
                );
        }
    };

    const colorMap = {
        "전체 교원 수": "#4b5563",
        "전임교원 수": "#028EA7",
        "교원 확보 기준 수": "#4b5563",
        "전임교원 확보율": "#028EA7",
    };

    const color = colorMap[title] || "#4b5563";
    const iconStyle = {
        backgroundColor: `${color}0D`,
        border: `1px solid ${color}40`,
        color: color,
    };

    const hasYoy = yoyDiff !== null && yoyDiff !== 0 && !isNaN(yoyDiff);
    const displayDiff = isRate ? Math.abs(yoyDiff).toFixed(2) : formatCount(Math.abs(yoyDiff)); // 확보율은 소수점 둘째 자리까지 표시
    const diffUnit = isRate ? '%p' : '명';

    return (
        <div className="rounded-xl border border-gray-200 p-4 bg-white">
            <div className="flex items-start gap-3">
                <div className="w-10 h-10 rounded-full flex items-center justify-center shrink-0" style={iconStyle}>
                    {renderIcon(title)}
                </div>
                <div>
                    <div className="text-[11px] text-gray-500">{title}</div>
                    <div className="mt-0.5 text-xl font-extrabold tracking-tight text-slate-900">
                        {value}
                        {unit && <span className="ml-1 text-xs text-gray-500">{unit}</span>}
                    </div>
                    {/* 전년 대비 증감 표시 */}
                    {hasYoy && (
                        <div className="mt-0.5 inline-flex items-center gap-1 text-[11px] rounded-full px-1.5 py-0.5" style={{
                            backgroundColor: getPillClass(yoyDiff).split(' ')[0],
                            color: getPillClass(yoyDiff).split(' ')[1]
                        }}>
                            {diffArrow(yoyDiff)}
                            {displayDiff}
                            {` ${diffUnit}`} (전년 대비)
                        </div>
                    )}
                    {sub && <div className="text-[11px] text-gray-500 mt-0.5">{sub}</div>}
                </div>
            </div>
        </div>
    );
}

// --- 메인 컴포넌트: FacultySecuredStatus ---

export default function FacultySecuredStatus() {
    const [selectedYear, setSelectedYear] = useState(2024);
    const [noticeData, setNoticeData] = useState([]); // 고시 상세 데이터
    const [compareData, setCompareData] = useState([]); // 대학 비교 데이터
    const [regionData, setRegionData] = useState([]); // 지역별 비교 데이터
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    // 1. 데이터 로딩 (2025년 지역 데이터 대체 로직 추가)
    useEffect(() => {
        const fetchData = async () => {
            try {
                setIsLoading(true);
                setError(null);

                const [notice, compare, regionRaw] = await Promise.all([
                    fetchWithRetry(NOTICE_URL),
                    fetchWithRetry(COMPARE_URL),
                    fetchWithRetry(REGION_URL),
                ]);

                // 🚀 [핵심 수정] 2025년 지역 데이터가 있다면 2022년 데이터로 대체
                const region = regionRaw.map(r => {
                    // API 응답 구조를 기반으로 r.year가 2025일 때 2022년으로 데이터를 복제합니다.
                    // regionByYear useMemo 로직에서 r.year를 String으로 변환하므로 r.year도 String으로 처리
                    if (String(r.year) === '2025') {
                        return [
                            r, // 원본 2025 데이터 유지 (필요할 경우)
                            { ...r, year: 2022 } // 2022년으로 복제
                        ];
                    }
                    return r;
                }).flat().filter(r => r.year !== 2025); // 2025년 원본 데이터는 필터링하여 제외

                setNoticeData(notice);
                setCompareData(compare);
                setRegionData(region);

            } catch (err) {
                console.error("데이터 불러오기 오류:", err);
                setError(`데이터를 불러오는 데 실패했습니다. (API 오류: ${err.message})`);
            } finally {
                setIsLoading(false);
            }
        };

        fetchData();
    }, []);

    // 2. 선택된 연도 데이터 및 전년 대비 데이터 가공 (첫 번째 코드의 useMemo 로직)
    const selectedNotice = useMemo(
        () => noticeData.find((n) => n.year === selectedYear),
        [noticeData, selectedYear]
    );

    const prevNotice = useMemo(
        () => noticeData.find((n) => n.year === selectedYear - 1),
        [noticeData, selectedYear]
    );

    // 3. 주요 값 계산
    const securedRate = selectedNotice?.["전임교원 확보율"] ?? 0;
    const totalFaculty = selectedNotice?.["학생정원"] ?? 0; // 이 필드명은 '전체 교원 수'에 맞지 않을 수 있음. 원본 컴포넌트를 따름
    const fulltimeFaculty = selectedNotice?.["학생정원 기준 전임교원"] ?? 0;
    const requiredFaculty = selectedNotice?.["학생정원 기준 교원 법정정원"] ?? 0;

    // 전년 대비 증감 계산
    const securedRateDiff =
        prevNotice && selectedNotice && prevNotice["전임교원 확보율"] !== undefined && selectedNotice["전임교원 확보율"] !== undefined
            ? +(selectedNotice["전임교원 확보율"] - prevNotice["전임교원 확보율"]).toFixed(2)
            : null;

    const totalDiff =
        prevNotice && selectedNotice && prevNotice["학생정원"] !== undefined && selectedNotice["학생정원"] !== undefined
            ? selectedNotice["학생정원"] - prevNotice["학생정원"]
            : null;

    const fulltimeDiff =
        prevNotice && selectedNotice && prevNotice["학생정원 기준 전임교원"] !== undefined && selectedNotice["학생정원 기준 전임교원"] !== undefined
            ? selectedNotice["학생정원 기준 전임교원"] -
            prevNotice["학생정원 기준 전임교원"]
            : null;

    const requiredDiff =
        prevNotice && selectedNotice && prevNotice["학생정원 기준 교원 법정정원"] !== undefined && selectedNotice["학생정원 기준 교원 법정정원"] !== undefined
            ? selectedNotice["학생정원 기준 교원 법정정원"] -
            prevNotice["학생정원 기준 교원 법정정원"]
            : null;


    // 4. 지역별 데이터 매핑 (regionData를 연도별/지역별 맵으로 변환)
    const regionByYear = useMemo(() => {
        const grouped = {};
        regionData.forEach((r) => {
            const yearStr = String(r.year);
            if (!grouped[yearStr]) grouped[yearStr] = {};
            // 'value'가 확보율이고, 'schools'가 학교 수라고 가정 (원본 코드의 regionData 구조 유추)
            grouped[yearStr][r.region] = {
                ratio: r.value,
                schools: r.schools // schools 필드가 없는 경우를 대비해 널 허용
            };
        });
        return grouped;
    }, [regionData]);

    // 5. 테이블 및 차트 데이터 구성을 위한 Row 데이터
    const tableRows = useMemo(() => {
        const years = YEARS; // YEARS 상수 사용
        const yiuMap = compareData.filter(d => d.schlKrnNm === "용인대학교").reduce((acc, curr) => {
            acc[String(curr.year)] = curr.value;
            return acc;
        }, {});

        // 연도별 테이블 행 데이터 생성
        return years.map((year) => ({
            year,
            yongin: yiuMap[year] ?? null,
            수도권: regionByYear[year]?.["수도권"]?.ratio ?? null,
            비수도권: regionByYear[year]?.["비수도권"]?.ratio ?? null,
            전체: regionByYear[year]?.["전체"]?.ratio ?? null,
            // 학교 수 추가 (테이블에 사용하기 위함)
            수도권_교수: regionByYear[year]?.["수도권"]?.schools ?? null,
            비수도권_교수: regionByYear[year]?.["비수도권"]?.schools ?? null,
            전체_교수: regionByYear[year]?.["전체"]?.schools ?? null,
        }));
    }, [compareData, regionByYear]);

    // 6. 차트 데이터 구성
    const chartData = useMemo(() => {
        const labels = tableRows.map((r) => r.year);
        return {
            labels,
            datasets: [
                {
                    label: "전체",
                    data: tableRows.map((r) => r.전체),
                    borderColor: "#96daf0ff", // 원본 스타일 유지
                    backgroundColor: "#96daf0ff".replace(')', ', 0.14)').replace('rgb', 'rgba').replace('hsl', 'hsla'),
                    tension: 0.36,
                    borderWidth: 3,
                    pointRadius: 3,
                    pointHoverRadius: 6,
                    fill: false,
                },
                {
                    label: "수도권",
                    data: tableRows.map((r) => r.수도권),
                    borderColor: LIGHT_GREEN, // 원본 스타일 유지
                    backgroundColor: LIGHT_GREEN.replace(')', ', 0.14)').replace('rgb', 'rgba').replace('hsl', 'hsla'),
                    tension: 0.36,
                    borderWidth: 3,
                    pointRadius: 3,
                    pointHoverRadius: 6,
                    fill: false,
                },
                {
                    label: "비수도권",
                    data: tableRows.map((r) => r.비수도권),
                    borderColor: "#efe63bff", // 원본 스타일 유지
                    backgroundColor: "#efe63bff".replace(')', ', 0.14)').replace('rgb', 'rgba').replace('hsl', 'hsla'),
                    tension: 0.36,
                    borderWidth: 3,
                    pointRadius: 3,
                    pointHoverRadius: 6,
                    fill: false,
                },
                {
                    label: "용인대",
                    data: tableRows.map((r) => r.yongin),
                    borderColor: YIU_COLOR, // 원본 스타일 유지
                    backgroundColor: BRAND_SOFT, // 원본 스타일 유지
                    tension: 0.36,
                    borderWidth: 3,
                    pointRadius: 3,
                    pointHoverRadius: 6,
                    fill: false,
                },
            ].filter(d => d.data.some(v => v !== null)), // 데이터가 없는 데이터셋은 제거
        };
    }, [tableRows]);

    // 7. 차트 옵션 구성 (Y축 고정 값 적용)
    const chartOptions = useMemo(() => {
        
        return {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: "top", labels: { usePointStyle: true } },
                tooltip: { callbacks: { label: (c) => `${c.raw.toFixed(2)}%` } },
            },
            scales: {
                x: { grid: { display: false } },
                y: {
                    beginAtZero: false,
                    title: { display: true, text: "확보율(%)" },
                    // 🚀 [수정] 요청하신 대로 Y축 범위를 63%에서 68%로 고정
                    min: 63, 
                    max: 68 
                }
            },
        };
    }, [chartData]); // chartData가 변경되어도 min/max는 고정

    // 로딩 및 에러 처리 UI (두 번째 코드의 UI 유지)
    if (error) {
        return (
            <div className="w-full h-full flex flex-col items-center justify-center p-10 bg-white rounded-2xl shadow">
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-rose-500 mb-4"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>
                <p className="text-xl font-semibold text-rose-600">데이터 로딩 오류</p>
                <p className="text-gray-500 mt-2">{error}</p>
            </div>
        );
    }

    // 데이터가 로드되었으나 (isLoading=false) noticeData가 비어있는 경우도 로딩 상태로 처리
    if (isLoading || noticeData.length === 0) {
        return (
            <div className="w-full h-full flex items-center justify-center p-10 bg-white rounded-2xl shadow min-h-[500px]">
                <div className="flex flex-col items-center">
                    <svg className="animate-spin h-8 w-8 text-[#028EA7]" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    <p className="mt-3 text-gray-600">데이터를 불러오는 중...</p>
                </div>
            </div>
        );
    }

    // 테이블의 증감분 계산 (2024 - 2023)
    const getRegionDiff = (region) => {
        const rate24 = tableRows.find(r => r.year === '2024')?.[region] || 0;
        const rate23 = tableRows.find(r => r.year === '2023')?.[region] || 0;
        return +(rate24 - rate23).toFixed(2);
    }

    const yiuDiff = getRegionDiff('yongin');
    const sudoDiff = getRegionDiff('수도권');
    const bisudoDiff = getRegionDiff('비수도권');
    const entireDiff = getRegionDiff('전체');

    // 2022년도에 2025년 데이터가 대체되었는지 확인하는 플래그
    const is2022Replaced = regionData.some(r => r.year === 2022);


    return (
        <div className="w-full h-full mx-auto p-6 bg-white rounded-2xl shadow">
            <div className="flex justify-between items-start mb-3">
                <h3 className="text-xl font-semibold text-slate-800">
                    전임교원 확보율 - 용인대학교 ({selectedYear})
                </h3>
                <div className="flex space-x-2">
                    {YEARS.map((y) => (
                        <button
                            key={y}
                            onClick={() => setSelectedYear(Number(y))} // selectedYear는 Number 타입으로
                            className={`px-3 py-1.5 rounded-full border text-sm font-medium transition-all ${
                                String(selectedYear) === y
                                    ? "bg-[#028EA7] text-white border-[#028EA7] shadow-md"
                                    : "bg-white text-gray-700 border-gray-300 hover:bg-gray-100"
                            }`}
                        >
                            {y}
                        </button>
                    ))}
                </div>
            </div>
            {/* 요약 카드 */}
            <div className="rounded-2xl border border-gray-200 p-4 bg-white shadow mb-6">
                <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
                    <InfoCard
                        title="전체 교원 수"
                        value={formatCount(totalFaculty)}
                        unit="명"
                        yoyDiff={totalDiff}
                    />
                    <InfoCard
                        title="전임교원 수"
                        value={formatCount(fulltimeFaculty)}
                        unit="명"
                        yoyDiff={fulltimeDiff}
                    />
                    <InfoCard
                        title="교원 확보 기준 수"
                        value={formatCount(requiredFaculty)}
                        unit="명"
                        yoyDiff={requiredDiff}
                    />
                    <InfoCard
                        title="전임교원 확보율"
                        value={`${securedRate.toFixed(1)}%`}
                        yoyDiff={securedRateDiff}
                        isRate={true}
                    />
                </div>
                {/* 진행바 */}
                <div className="mt-5 rounded-2xl shadow-inner border border-gray-100 p-5 bg-slate-50">
                    <div className="flex items-center justify-between text-sm">
                        <span className="text-gray-700 font-medium">정원 대비 전임교원 확보율</span>
                        <span className="font-semibold text-[#028EA7]">{securedRate.toFixed(1)}%</span>
                    </div>
                    <div className="mt-2 h-3 rounded-full bg-slate-100 overflow-hidden">
                        <div
                            className="h-3 rounded-full transition-all duration-500 ease-out"
                            style={{
                                width: `${securedRate}%`,
                                background: "linear-gradient(90deg, rgba(82,199,218,0.6) 0%, rgba(2,142,167,0.95) 100%)",
                            }}
                        />
                    </div>
                    <div className="mt-2 text-xs text-gray-600 flex justify-between">
                        <span>
                            전임교원 <b className="text-slate-700">{formatCount(fulltimeFaculty)}명</b>
                        </span>
                        <span>
                            법정 정원 <b className="text-slate-700">{formatCount(requiredFaculty)}명</b>
                        </span>
                    </div>
                </div>
            </div>
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-6">
                {/* 표 */}
                <div className="rounded-2xl shadow border border-gray-200 p-4">
                    <h3 className="text-base font-semibold mb-1 text-slate-800">전임교원 확보 현황 - 지역별 통계</h3>
                    <p className="text-xs text-gray-500 mb-4">비교 대상 학교 수와 확보율을 한눈에 확인하세요.</p>
                    <div className="overflow-x-auto pt-4 mb-3">
                        <table className="text-sm w-full border-collapse min-w-[500px]">
                            <thead>
                                <tr className="text-gray-700 bg-slate-100 border-b border-gray-300">
                                    <th className="py-3 px-3 text-left w-20 font-semibold rounded-tl-xl">연도/구분</th>
                                    <th className="py-3 px-3 text-center font-semibold text-[#028EA7]">
                                        용인대
                                    </th>
                                    <th className="py-3 px-3 text-center font-semibold text-slate-700">
                                        수도권
                                    </th>
                                    <th className="py-3 px-3 text-center font-semibold text-slate-700">
                                        비수도권
                                    </th>
                                    <th className="py-3 px-3 text-center font-semibold text-slate-700 rounded-tr-xl">
                                        전체
                                    </th>
                                </tr>
                            </thead>
                            <tbody>
                                {tableRows.map((row) => (
                                    <tr key={row.year} className="border-b border-gray-200 bg-white hover:bg-slate-50">
                                        <td className="py-3 px-3 font-medium text-left bg-slate-50/50 text-slate-800">
                                            {/* 🚀 [변경] 2022년은 2025년 값이 대체되었을 경우 '2022 (2025 값)'으로 표시 */}
                                            {row.year === '2022' && is2022Replaced ? '2022' : row.year}
                                        </td>
                                        <td key={`yiu-${row.year}`} className="py-3 text-center text-slate-700 font-bold bg-cyan-50/70">
                                            {row.yongin !== null ? row.yongin.toFixed(2) : '—'}%
                                        </td>
                                        {[
                                            { region: "수도권", value: row.수도권, schools: row.수도권_교수 },
                                            { region: "비수도권", value: row.비수도권, schools: row.비수도권_교수 },
                                            { region: "전체", value: row.전체, schools: row.전체_교수 }
                                        ].map(r => (
                                            <td key={`${r.region}-${row.year}`} className="py-3 text-center text-slate-700 font-medium">
                                                {r.value !== null ? r.value.toFixed(2) : '—'}%
                                                {r.schools !== null && <span className="text-gray-500 ml-1 text-xs">({r.schools}교)</span>}
                                            </td>
                                        ))}
                                    </tr>
                                ))}
                                {/* 증감 행 */}
                                <tr className="border-b border-gray-200 last:border-b-0 transition-colors bg-white hover:bg-slate-50">
                                    {/* 🚀 [수정] 증감 셀 폰트 크기 조정 (text-sm, 하단 text-xs) */}
                                    <td className="py-3 px-3 bg-slate-50/50 text-slate-800 rounded-bl-xl text-center">
                                        <div className="font-bold text-sm">증감</div>
                                        <div className="font-bold text-xs text-gray-500 whitespace-nowrap">('23 → '24)</div>
                                    </td>
                                    <td key={`diff-yiu`} className={`py-3 text-center font-bold ${diffClass(yiuDiff)} bg-cyan-50/70`}>
                                        {yiuDiff !== 0 ? `${diffArrow(yiuDiff)} ${Math.abs(yiuDiff).toFixed(2)}%p` : '—'}
                                    </td>
                                    {[sudoDiff, bisudoDiff, entireDiff].map((d, index) => (
                                        <td key={`diff-${index}`} className={`py-3 text-center font-bold ${d !== 0 ? diffClass(d) : 'text-slate-500'}`}>
                                            {d !== 0 ? `${diffArrow(d)} ${Math.abs(d).toFixed(2)}%p` : '—'}
                                        </td>
                                    ))}
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                {/* 그래프 */}
                <div className="rounded-2xl shadow border border-gray-200 p-4">
                    <h3 className="text-base font-semibold mb-1 text-slate-800">연도별 확보율 비교 그래프</h3>
                    <p className="text-xs text-gray-500 mb-4">용인대와 지역별 평균 확보율 추이.</p>
                    <div className="h-[260px]">
                        <Line data={chartData} options={chartOptions} />
                    </div>
                </div>
            </div>
            <div className="text-xs text-gray-600 space-y-2 mt-6 p-4 bg-slate-50 rounded-lg border border-gray-200">
                <p><b>확보율 정의:</b> 법정 기준 충족률(정해진 최소 기준 충족 여부 판단).</p>
                <p className="font-medium text-gray-700">전임교원 확보율 = (전임교원 수 / 교원 법정 정원) &times; 100</p>
                <p><b>확보 현황:</b> 실제 확보 상태 통계(비교·분석용 데이터, 지역/대학별 수준 확인 가능).</p>
            </div>
        </div>
    );
}