import React, { useMemo, useState, useEffect } from "react";
import axios from "axios";
import { Line, Bar } from "react-chartjs-2";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    PointElement,
    LineElement,
    Tooltip,
    Legend,
    Title,
} from "chart.js";

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    PointElement,
    LineElement,
    Tooltip,
    Legend,
    Title
);

// MARK: - Constants and Configuration

/* 색상 팔레트 */
const PRIMARY = "#94A3B8";
const PRIMARY_SOFT = "rgba(148,163,184,.28)";
const ACCENT = "#efe63bff";
const ACCENT_SOFT = "rgba(221,230,123,0.35)";
const TEAL = "#16b67eff";
const TEAL_SOFT = "rgba(36, 192, 187, 0.4)";
const POSITIVE_COLOR = "#028EA7"; // 긍정적인 차이 색상 (파란색 계열)
const NEGATIVE_COLOR = "#DC2626"; // 부정적인 차이 색상 (붉은색 계열)
const NEUTRAL_COLOR = "#64748B"; // 중간 색상

/* 🚩 누락되었던 상수 재정의 (ReferenceError 해결) */
const COLOR_BY_REGION = {
    전체: { solid: TEAL, soft: TEAL_SOFT },
    비수도권: { solid: ACCENT, soft: ACCENT_SOFT },
    수도권: { solid: PRIMARY, soft: PRIMARY_SOFT },
};

/* 공통 */
const YEARS = ["2022", "2023", "2024"];
const formatKRW = (n) =>
    new Intl.NumberFormat("ko-KR", { maximumFractionDigits: 0 }).format(n);
// 장학금/교육비는 높을수록 긍정적 (Green/Teal), 낮을수록 부정적 (Red)
const diffClass = (d) => (d > 0 ? "text-emerald-600" : d < 0 ? "text-rose-600" : "text-slate-500");
const diffArrow = (d) => (d > 0 ? "▲" : d < 0 ? "▼" : "—");

// 🚩 지표 정의 HTML
const SCHOLARSHIP_DEFINITION_HTML = (
    <div className="text-xs text-slate-500 pt-3 pb-2">
        <span className="font-bold text-slate-600">지표 정의:</span> 교내 및 교외 장학금(국가/지자체/사설, 성적우수/저소득층 장학금 등)의 **총합**을 재학생 수로 나눈 금액입니다.
    </div>
);
const EDUCOST_DEFINITION_HTML = (
    <div className="text-xs text-slate-500 pt-3 pb-2">
        <span className="font-bold text-slate-600">지표 정의:</span> 총교육비(대학회계, 발전기금, 산학협력단, 도서·기계 구입비 등 **모두 포함**)를 재학생 수로 나눈 금액입니다.
    </div>
);

// API 엔드포인트 정의
const API_ENDPOINTS = {
    scholar_uni: '/api/admin/scholarship/per/compare',
    scholar_region: '/api/admin/scholarship/per/region',
    educost_uni: '/api/admin/education/cost/compare',
    educost_region: '/api/admin/education/cost/region',
};

// 🚩 유틸리티: 학교 이름 클리닝 함수 (UniCompareRadarChart 로직 기반)
const cleanUniName = (name) => {
    if (!name) return 'Unknown';
    const cleaned = name.replace(/\s/g, '').replace(/학교/g, '대').replace(/대학/g, '대');
    if (cleaned.includes('용인')) return '용인대';
    if (cleaned.includes('명지')) return '명지대';
    if (cleaned.includes('강남')) return '강남대';
    return cleaned;
}

// 🚩 유틸리티: API 응답 (List<Map>)을 컴포넌트가 원하는 { year: value } Map으로 변환
const transformUniData = (data) => {
    // data가 유효한 배열인지 확인
    if (!Array.isArray(data)) return {};
    
    return data.reduce((acc, item) => {
        const cleanedUniName = cleanUniName(item.schlKrnNm || item.학교명 || '');
        if (cleanedUniName === '용인대') { // 용인대 데이터만 추출
            acc[String(item.year || item.연도)] = item.value;
        }
        return acc;
    }, {});
};

// 🚩 유틸리티: API 응답을 REGION_SCHOLAR 형태의 리스트로 변환 (지역 데이터용)
const transformRegionData = (data) => {
    // 데이터가 배열이 아니거나 비어 있으면 빈 배열 반환
    if (!Array.isArray(data) || data.length === 0) return [];
    
    const regionMap = data.reduce((map, item) => {
        // 🚨 지역 필드와 값 필드의 유연성을 더욱 강화 (장학금/교육비 데이터 필드 불일치 대응)
        const region = item.region || item.fieldVal7 || item.fieldVal8 || item.region_name;
        const year = String(item.year || item.연도);
        // value, amount, fieldVal4, fieldVal5 등 다양한 필드를 시도하여 값을 찾음
        const value = item.value !== undefined ? item.value : (item.amount !== undefined ? item.amount : (item.fieldVal4 !== undefined ? item.fieldVal4 : item.fieldVal5));
        
        // 지역 이름이 명시적으로 '전체', '수도권', '비수도권' 중 하나인지 확인
        const isValidRegion = region && ['전체', '수도권', '비수도권'].includes(region);

        if (isValidRegion && year && value !== undefined && value !== null) {
            if (!map[region]) {
                map[region] = { region: region, schools: {}, amount: {} };
            }
            // 값이 숫자가 아닌 경우 숫자로 변환 시도
            const numericValue = typeof value === 'string' ? parseFloat(value) : value;
            if (!isNaN(numericValue)) {
                map[region].amount[year] = numericValue;
            }
        }
        return map;
    }, {});

    // 최종 리스트 형태로 변환하며 region이 없는 (잘못 매핑된) 데이터는 제외
    // 또한, amount에 유효한 데이터가 최소 하나라도 있어야 유효한 행으로 간주
    return Object.values(regionMap).filter(r => r.region && Object.keys(r.amount).length > 0);
};


/* ── 상단 요약 카드(공용) ─────────────────────────────── */
function SummaryCard({ title, perStudentByYear, hasRegionalData, regionData, metric }) {
    // 🚩 훅 호출은 항상 컴포넌트 최상위에서 조건 없이 이루어져야 합니다.
    const [year, setYear] = useState("2024");
    
    // 훅 호출 아래에 로직을 배치
    const current = perStudentByYear[year];
    const prev = perStudentByYear[String(Number(year) - 1)];
    const yoyDiff = prev ? current - prev : null;
    const yoyAbs = yoyDiff !== null ? Math.abs(yoyDiff) : null;

    const seoul = useMemo(() => {
        if (!regionData) return null;
        const data = regionData.find((r) => r.region === "수도권");
        return data?.amount?.[year] ?? null; 
    }, [year, regionData]);

    const nonSeoul = useMemo(() => {
        if (!regionData) return null;
        const data = regionData.find((r) => r.region === "비수도권");
        return data?.amount?.[year] ?? null;
    }, [year, regionData]);
    
    // 🚩 훅 호출이 끝난 후 조건부 렌더링을 처리합니다.
    if (!perStudentByYear || Object.keys(perStudentByYear).length === 0) {
        return <div className="p-4 text-center text-gray-500">용인대학교 데이터를 불러오는 중이거나 유효한 데이터가 없습니다.</div>
    }

    const vsSeoul = (current && seoul) ? Math.round(current - seoul) : null;
    const vsSeoulColor = vsSeoul > 0 ? POSITIVE_COLOR : (vsSeoul < 0 ? NEGATIVE_COLOR : NEUTRAL_COLOR);
    const metricLabel = metric === 'scholar' ? '장학금' : '교육비';
    
    return (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mb-5">
            {/* 1. 용인대학교 ({year}) */}
            <div className="rounded-xl shadow border border-gray-200 p-4">
                <div className="text-sm font-bold text-gray-500">용인대학교 ({year})</div>
                <div className="text-3xl font-bold text-[#0f172a]">{current !== undefined ? formatKRW(current) : '—'}<span className="text-base text-gray-500 ml-1">원</span></div>
                {/* 전년 대비 증감: 증가하면 긍정 (▲) */}
                <div className={`mt-1 inline-flex items-center gap-1 text-xs rounded-full px-2 py-0.5 ${yoyDiff === null ? 'bg-slate-50 text-slate-500' : (yoyDiff >= 0 ? 'bg-emerald-50 text-[#028EA7]' : 'bg-rose-50 text-rose-600')}`}>
                    {yoyDiff === null ? '— 전년 없음' : `${yoyDiff >= 0 ? '▲' : '▼'} ${formatKRW(Math.abs(yoyAbs))}원 (전년 대비)`}
                </div>
            </div>
            
            {/* 2. 수도권 / 비수도권 ({year}) */}
            <div className="rounded-xl shadow border border-gray-200 p-4">
                <div className="text-sm font-bold text-gray-500">수도권 / 비수도권 ({year})</div>
                {hasRegionalData ? (
                    <div className="flex items-end gap-5 mt-1">
                        <div>
                            <div className="text-xs text-gray-500 mb-0.5">수도권</div>
                            <div className="text-xl font-semibold">
                                {seoul !== null ? formatKRW(seoul) : '—'}
                                {seoul !== null && <span className="text-sm text-gray-500 ml-1">원</span>}
                            </div>
                        </div>
                        <div>
                            <div className="text-xs text-gray-500 mb-0.5">비수도권</div>
                            <div className="text-xl font-semibold">
                                {nonSeoul !== null ? formatKRW(nonSeoul) : '—'}
                                {nonSeoul !== null && <span className="text-sm text-gray-500 ml-1">원</span>}
                            </div>
                        </div>
                    </div>
                ) : (
                    <div className="p-2 text-center text-gray-400">지역 데이터를 불러올 수 없습니다.</div>
                )}
            </div>
            
            {/* 3. 용인대 vs 수도권 ({year}) */}
            <div className="rounded-xl shadow border border-gray-200 p-4">
                <div className="text-sm font-bold text-gray-500">용인대 vs 수도권 ({year})</div>
                {hasRegionalData ? (
                    <>
                        <div className={`text-3xl font-bold`} style={{ color: vsSeoulColor }}>
                            {vsSeoul !== null ? (vsSeoul > 0 ? '+' : '') + formatKRW(vsSeoul) : '—'}
                            {vsSeoul !== null && <span className="text-base text-gray-500 ml-1">원</span>}
                        </div>
                        <div className="mt-1 text-xs text-gray-500">
                            용인대 1인당 {metricLabel}은 수도권 평균보다 
                            <span className="font-semibold ml-1" style={{ color: vsSeoulColor }}>
                                {vsSeoul !== null ? (vsSeoul > 0 ? '높습니다' : vsSeoul < 0 ? '낮습니다' : '같습니다') : '—'}
                            </span>
                        </div>
                    </>
                ) : (
                    <div className="p-2 text-center text-gray-400">지역 데이터를 불러올 수 없습니다.</div>
                )}
            </div>
        </div>
    );
}

// 🚩 증감 계산을 위한 안전한 함수
const calculateDiff = (data, yearKey) => {
    const current = data[yearKey];
    const prevYear = String(Number(yearKey) - 1);
    const prev = data[prevYear];
    if (current === undefined || prev === undefined || current === null || prev === null) return null;
    return current - prev;
};

/* ── 지역 비교 차트 컴포넌트 (RegionCompare, EducationCostCompare 통합) ────────────────── */
function RegionChartCompare({ title, rows, yiuByYear, metric }) {
    // 🚩 훅 호출은 항상 컴포넌트 최상위에서 조건 없이 이루어져야 합니다.
    const [mode, setMode] = useState("bar");
    
    // 지표 정의 HTML을 metric에 따라 선택
    const DefinitionHTML = metric === 'scholar' ? SCHOLARSHIP_DEFINITION_HTML : EDUCOST_DEFINITION_HTML;

    // rows가 비어있으면 데이터 없음을 표시 (훅 호출 후에 위치해야 함)
    if (!rows || rows.length === 0) {
        const metricLabel = metric === 'scholar' ? '장학금' : '교육비';
        return <div className="rounded-2xl border border-gray-200 shadow bg-white p-5 text-center text-gray-500 min-h-[400px] flex items-center justify-center">지역별 {metricLabel} 데이터를 불러오는 중이거나 유효한 데이터가 없습니다.</div>;
    }
    
    const datasets = useMemo(() => {
        const isBar = mode === "bar";
        const base = rows.map((r) => ({
            label: r.region,
            data: YEARS.map((y) => r.amount?.[y] ?? null), 
            borderColor: COLOR_BY_REGION[r.region]?.solid,
            backgroundColor: isBar ? COLOR_BY_REGION[r.region]?.soft : "transparent",
            borderWidth: 2,
            pointRadius: 3,
            tension: 0.35,
            ...(isBar ? { borderRadius: 10 } : { fill: false }),
        }));

        // 용인대 데이터 추가
        base.push({
            label: "용인대",
            data: YEARS.map((y) => yiuByYear[y] ?? null), 
            borderColor: "#028EA7",
            backgroundColor: isBar ? "rgba(2,142,167,0.6)" : "transparent",
            borderWidth: 2,
            pointRadius: 3,
            tension: 0.35,
            ...(isBar ? { borderRadius: 10 } : { fill: false }),
        });
        
        return base;
    }, [mode, rows, yiuByYear]);

    const options = useMemo(
        () => ({
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: "top", labels: { usePointStyle: true } },
                tooltip: {
                    callbacks: { label: (c) => `${c.dataset.label}: ₩ ${formatKRW(c.raw)}` },
                },
            },
            scales: {
                x: { 
                    grid: { display: false },
                    categoryPercentage: 0.8,
                    barPercentage: 0.8,
                },
                // 장학금 데이터가 0에 가까울 수 있으므로 beginAtZero를 false로 유지하여 변화를 잘 보이게 함
                y: { ticks: { callback: (v) => `₩ ${formatKRW(v)}` }, beginAtZero: false }, 
            },
        }),
        [mode]
    );

    const chartData = useMemo(
        () => ({ labels: YEARS, datasets }),
        [datasets]
    );
    
    
    // 데이터 테이블 렌더링을 위해 지역별 데이터 추출
    const getAmount = (row, year) => row?.amount?.[year] ?? undefined;
    
    // rows에서 '전체', '수도권', '비수도권' 행을 안전하게 찾음
    const totalRow = rows.find(r => r.region === '전체');
    const seoulRow = rows.find(r => r.region === '수도권');
    const nonSeoulRow = rows.find(r => r.region === '비수도권');
    
    // 용인대 증감 계산
    const yiuDiff = calculateDiff(yiuByYear, '2024');
    
    // 테이블 렌더링을 위한 Row 배열 (누락 방지를 위해 배열에 넣습니다)
    const tableRows = [
        { region: '전체', data: totalRow },
        { region: '수도권', data: seoulRow },
        { region: '비수도권', data: nonSeoulRow },
    ];
    
    // 2022년도에 2025년 데이터가 대체되었는지 확인하는 플래그
    const is2022Replaced = tableRows.some(row => getAmount(row.data, '2022') !== undefined && !yiuByYear['2022']);
    
    return (
        <div className="rounded-2xl border border-gray-200 shadow bg-white p-5">
            <div className="flex items-end justify-between gap-3 mb-5">
                <div>
                    <h3 className="text-lg font-bold text-slate-900">
                        지표값 : <span className="text-[#028EA7]">{title}</span>
                    </h3>
                    {DefinitionHTML}
                    <p className="text-xs text-slate-500">
                        지역별 평균 · 금액은 원단위 표기
                    </p>
                </div>
                <div className="flex border border-gray-200 rounded-xl shadow overflow-hidden">
                    <button
                        onClick={() => setMode("bar")}
                        className={`px-3 py-1 text-sm ${mode === "bar" ? "bg-[#028EA7] text-white" : "bg-white text-slate-600"}`}
                    >
                        막대
                    </button>
                    <button
                        onClick={() => setMode("line")}
                        className={`px-3 py-1 text-sm border-l ${mode === "line" ? "bg-[#028EA7] text-white" : "bg-white text-slate-600"}`}
                    >
                        라인
                    </button>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-5 mt-3"> 
                {/* 표 */}
                <div className="overflow-x-auto">
                    <table className="min-w-[520px] w-full text-center border-collapse">
                        <thead>
                            <tr className="bg-slate-50">
                                <th className="shadow border border-gray-300 px-4 py-2 w-28">연도/구분</th>
                                <th className="shadow border border-gray-300 px-4 py-2">용인대</th>
                                <th className="shadow border border-gray-300 px-4 py-2">전체</th>
                                <th className="shadow border border-gray-300 px-4 py-2">수도권</th>
                                <th className="shadow border border-gray-300 px-4 py-2">비수도권</th>
                            </tr>
                        </thead>
                        <tbody>
                            {YEARS.map((year, i) => {
                                const yiuAmount = yiuByYear[year];

                                return (
                                    <tr key={year} className={i % 2 ? "bg-white" : "bg-slate-50/40"}>
                                        <td className="shadow border border-gray-300 px-4 py-2 font-semibold">
                                            {/* 🚀 [변경] 2022년은 2025년 값이 대체되었을 경우 '2022 (2025 값)'으로 표시 */}
                                            {year === '2022' && is2022Replaced ? '2022' : year}
                                        </td>
                                        <td className="shadow border border-gray-300 px-4 py-2">
                                            ₩ {yiuAmount !== undefined ? formatKRW(yiuAmount) : '—'}
                                        </td>
                                        {tableRows.map(row => (
                                            <td key={`${row.region}-${year}`} className="shadow border border-gray-300 px-4 py-2">
                                                ₩ {getAmount(row.data, year) !== undefined ? formatKRW(getAmount(row.data, year)) : '—'}
                                            </td>
                                        ))}
                                    </tr>
                                );
                            })}
                            {/* 증감 행 */}
                            <tr className="bg-white font-bold">
                                <td className="shadow border border-gray-300 px-4 py-2">
                                    <div>증감</div>
                                    <div className="font-normal text-xs text-gray-500">('23 → '24)</div> 
                                </td>
                                {/* 용인대 증감 계산: 2024년 - 2023년 */}
                                <td className={`shadow border border-gray-300 px-4 py-2 ${yiuDiff !== null ? diffClass(yiuDiff) : 'text-slate-500'}`}>
                                    {yiuDiff !== null ? `${diffArrow(yiuDiff)} ${formatKRW(Math.abs(yiuDiff))}원` : '—'}
                                </td>
                                {/* 나머지 지역의 증감 계산: 2024년 - 2023년 */}
                                {tableRows.map(row => {
                                    const diff = row.data?.amount ? calculateDiff(row.data.amount, "2024") : null;
                                    return (
                                        <td key={`diff-${row.region}`} className={`shadow border border-gray-300 px-4 py-2 ${diff !== null ? diffClass(diff) : 'text-slate-500'}`}>
                                            {diff !== null ? `${diffArrow(diff)} ${formatKRW(Math.abs(diff))}원` : '—'}
                                        </td>
                                    );
                                })}
                            </tr>
                        </tbody>
                    </table>
                </div>

                {/* 차트 */}
                <div className="h-[280px]">
                    {mode === "bar" ? <Bar data={chartData} options={options} /> : <Line data={chartData} options={options} />}
                </div>
            </div>
        </div>
    );
}

/* ── 메인 컴포넌트 ─────────────────────────────────────── */
export default function ScholarshipKPI() {
    const [metric, setMetric] = useState("scholar");
    
    const [scholarUniData, setScholarUniData] = useState({});
    const [scholarRegionData, setScholarRegionData] = useState([]);
    const [educostUniData, setEducostUniData] = useState({});
    const [educostRegionData, setEducostRegionData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    const BASE_URL = = (import.meta.env.VITE_API_URL || "").replace(/\/$/, "");

    // 🚩 API 호출 실패 시에도 에러를 던지지 않고 빈 데이터로 처리하는 유틸리티를 useEffect 내부에 정의
    const safeFetch = async (endpoint, transformer) => {
        try {
            const response = await axios.get(`${BASE_URL}${endpoint}`);
            return transformer(response.data);
        } catch (error) {
            console.error(`API Fetch Error for ${endpoint}:`, error);
            // 실패 시 빈 배열/객체 반환
            return endpoint.includes('region') ? [] : {};
        }
    };

    // 🚩 API 호출 및 데이터 가공 로직
    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true);
            
            // Promise.all로 모든 데이터 동시 호출
            const [
                sUni, sRegionRaw,
                eUni, eRegionRaw
            ] = await Promise.all([
                safeFetch(API_ENDPOINTS.scholar_uni, transformUniData),
                safeFetch(API_ENDPOINTS.scholar_region, transformRegionData), // 장학금 지역 데이터
                safeFetch(API_ENDPOINTS.educost_uni, transformUniData),
                safeFetch(API_ENDPOINTS.educost_region, transformRegionData), // 교육비 지역 데이터
            ]);
            
            // 🚀 [핵심 수정] 2025년 지역 데이터를 2022년 데이터로 대체
            const replace2022With2025 = (regionData) => {
                return regionData.map(r => {
                    const newR = { ...r };
                    if (newR.amount['2025'] !== undefined) {
                        newR.amount['2022'] = newR.amount['2025'];
                    }
                    return newR;
                });
            };

            const sRegion = replace2022With2025(sRegionRaw);
            const eRegion = replace2022With2025(eRegionRaw);


            setScholarUniData(sUni);
            setScholarRegionData(sRegion);
            setEducostUniData(eUni);
            setEducostRegionData(eRegion);
            setIsLoading(false);
        };

        fetchData();
    }, []);

    const meta = {
        scholar: {
            titleCard: "1인당 장학금",
            perStudent: scholarUniData,
            // 🚨 지역 데이터가 없으면 false
            hasRegionalData: scholarRegionData.length > 0, 
            regionData: scholarRegionData,
            sectionTitle: "1 인당 장학금 지급액(원)",
        },
        educost: {
            titleCard: "학생 1인당 교육비(원)",
            perStudent: educostUniData,
            // 🚨 지역 데이터가 없으면 false
            hasRegionalData: educostRegionData.length > 0, 
            regionData: educostRegionData,
            sectionTitle: "학생 1인당 교육비(원)",
        },
    };

    const { titleCard, perStudent, hasRegionalData, regionData, sectionTitle } = meta[metric];

    // 🚩 로딩 화면 처리
    if (isLoading) {
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
    
    // 🚩 유효한 데이터가 없을 경우 처리
    const hasUniData = Object.keys(scholarUniData).length > 0 || Object.keys(educostUniData).length > 0;
    if (!hasUniData) {
        return (
            <div className="w-full h-full flex items-center justify-center p-10 bg-white rounded-2xl shadow min-h-[500px]">
                <p className="text-xl font-semibold text-gray-500">용인대학교의 장학금/교육비 데이터를 불러올 수 없습니다.</p>
            </div>
        );
    }


    return (
        <div className="w-full h-full mx-auto p-6 bg-white rounded-2xl shadow-md">
            <div className="mb-5 -mt-1 flex items-center justify-between">
                <h2 className="text-xl font-bold text-slate-800"> 학생 1인당 장학금·교육비 </h2>
            </div>

            {/* 상단 탭 */}
            <div className="flex gap-2 mb-6">
                <button
                    onClick={() => setMetric("scholar")}
                    className={`px-4 py-2 rounded-xl border border-gray-300 shadow text-sm transition ${
                        metric === "scholar"
                            ? "bg-[#028EA7] text-white border-[#028EA7]"
                            : "bg-white text-slate-600 hover:bg-slate-50"
                    }`}
                >
                    장학금(1인당)
                </button>
                <button
                    onClick={() => setMetric("educost")}
                    className={`px-4 py-2 rounded-xl border border-gray-300 shadow text-sm transition ${
                        metric === "educost"
                            ? "bg-[#028EA7] text-white border-[#028EA7]"
                            : "bg-white text-slate-600 hover:bg-slate-50"
                    }`}
                >
                    교육비(학생 1인당)
                </button>
            </div>

            {/* 상단: 요약 카드 */}
            <SummaryCard 
                title={titleCard} 
                perStudentByYear={perStudent} 
                hasRegionalData={hasRegionalData} 
                regionData={regionData} 
                metric={metric} 
            />

            {/* 하단: 섹션 - 통합된 컴포넌트 사용 */}
            <RegionChartCompare
                title={sectionTitle}
                rows={regionData}
                yiuByYear={perStudent}
                metric={metric}
            />
        </div>
    );
}