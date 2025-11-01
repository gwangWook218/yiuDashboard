import React, { useMemo, useState, useEffect } from 'react';
import axios from 'axios';
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    Tooltip,
    Legend,
    BarElement,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, Tooltip, Legend, BarElement);

// [수정] 각 학교별 고유 색상 정의
const UNIVERSITY_COLORS = {
    yongin: '#028EA7',       // 용인대학교 (브랜드색)
    myongji: '#EFB500',      // 명지대학교 (대체색)
    kangnam: '#A6263A',      // 강남대학교 (임의 지정)
    // 필요에 따라 더 많은 학교 색상 추가
};

// [수정] 각 학교별 고유 색상의 소프트 버전 정의
const UNIVERSITY_SOFT_COLORS = {
    yongin: 'rgba(2, 142, 167, 0.6)',
    myongji: 'rgba(239, 181, 0, 0.6)',
    kangnam: 'rgba(166, 38, 58, 0.6)',
};


const YEARS = ['2022', '2023', '2024'];
const BASE_URL = (import.meta.env.VITE_API_URL || "").replace(/\/$/, "");
const API_ENDPOINTS = {
    uni: '/api/faculty/research/funding-per-faculty/compare',
};

function formatData(compareInApiData, compareOutApiData) {
    const formatted = {};
    const processData = (apiData, scopeKey) => {
        if (Array.isArray(apiData)) {
            apiData.forEach(item => {
                const year = String(item.year);
                const university = item.schlKrnNm;
                const value = item.value || 0;
                if (!university) return;
                if (!formatted[university]) {
                    formatted[university] = {};
                }
                if (!formatted[university][year]) {
                    formatted[university][year] = { '교내': 0, '교외': 0 };
                }
                formatted[university][year][scopeKey] = value;
            });
        }
    };
    processData(compareInApiData, '교내');
    processData(compareOutApiData, '교외');
    return formatted;
}

const formatDiff = (value) => {
    if (value === null || isNaN(value)) return '—';
    const roundedValue = Math.round(value * 100) / 100;
    const formatted = roundedValue.toFixed(2);
    return roundedValue >= 0 ? `+${formatted}` : formatted;
}

// ResearchCompareSection 컴포넌트 전체 코드
function ResearchCompareSection({ title, data, yearsWithData, currentYear }) {
    const [scope, setScope] = useState("교외");
    const universities = useMemo(() => {
        if (!data) return [];
        const universityOrder = ['용인대학교', '명지대학교', '강남대학교']; 
        return universityOrder.filter(name => data[name]);
    }, [data]);
    const displayLabels = universities.map(name => name.replace('대학교', ''));

    const chartData = useMemo(() => {
        if (!data || universities.length === 0) return { labels: [], datasets: [] };
        
        const dataValues = universities.map(name => data[name]?.[currentYear]?.[scope] ?? 0);
        
        const backgroundColors = universities.map(uName => {
            const colorKey = { '용인대학교': 'yongin', '명지대학교': 'myongji', '강남대학교': 'kangnam' }[uName];
            return UNIVERSITY_SOFT_COLORS[colorKey] || 'rgba(204, 204, 204, 0.6)';
        });

        const borderColors = universities.map(uName => {
            const colorKey = { '용인대학교': 'yongin', '명지대학교': 'myongji', '강남대학교': 'kangnam' }[uName];
            return UNIVERSITY_COLORS[colorKey] || '#cccccc';
        });

        return {
            labels: displayLabels,
            datasets: [{
                label: scope,
                data: dataValues,
                borderColor: borderColors,
                backgroundColor: backgroundColors,
                borderWidth: 2,
                borderRadius: 10,
                barPercentage: 0.8,
                categoryPercentage: 0.8,
            }]
        };
    }, [data, currentYear, scope, universities, displayLabels]);

    const options = useMemo(() => {
        const dataValues = chartData.datasets?.[0]?.data || [];
        const maxValue = Math.max(...dataValues, 0);
        const maxYValue = Math.max(100, Math.ceil(maxValue * 1.1 / 100) * 100);
        return {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false }, 
                tooltip: {
                    callbacks: {
                        label: (ctx) => `${ctx.label} ${ctx.dataset.label}: ${ctx.raw.toFixed(2)} 천원/인`,
                    },
                },
            },
            scales: {
                y: { beginAtZero: true, title: { display: true, text: '천원/인' }, min: 0, max: maxYValue },
                x: { grid: { display: false }, categoryPercentage: 0.8, barPercentage: 0.8 },
            },
        };
    }, [scope, chartData]);

    const tableData = useMemo(() => {
        if (!data || universities.length === 0) return [];
        const dataRows = [];
        universities.forEach(uName => {
            if (!data[uName]) return;
            const inside = { university: uName, scope: '교내', data: yearsWithData.map(y => data[uName][y]?.교내 ?? null) };
            const outside = { university: uName, scope: '교외', data: yearsWithData.map(y => data[uName][y]?.교외 ?? null) };
            dataRows.push(inside, outside);
        });
        return dataRows;
    }, [data, yearsWithData, universities]);

    return (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-6">
            
            {/* 표 부분: min-h, h-full, flex 보장 */}
            <div className="rounded-2xl border border-gray-200 p-3 shadow bg-white lg:col-span-1 min-h-[350px] h-full flex flex-col">
                <h3 className="text-base font-semibold mb-8">대학별 3개년 연구비 현황 (천원/인)</h3>
                <div className="overflow-x-auto">
                    <table className="min-w-full text-sm border-collapse">
                        <thead>
                            <tr className="text-gray-500 border-b border-gray-300 bg-slate-50">
                                <th className="py-3 text-left px-4 w-28">대학</th>
                                <th className="py-3 text-center w-20">구체범위</th>
                                {yearsWithData.map(y => <th key={y} className="py-3 text-center">{y}</th>)}
                            </tr>
                        </thead>
                        <tbody>
                            {tableData.map((row, i) => {
                                const isFirstRowForUniversity = i % 2 === 0;
                                const isYongin = row.university === '용인대학교';
                                return (
                                    <tr key={`${row.university}-${row.scope}`} className={` ${isFirstRowForUniversity ? 'bg-white' : 'bg-slate-50/40'} ${isYongin ? 'font-medium' : ''} border-b border-gray-300 `}>
                                        {isFirstRowForUniversity && (
                                            <td className={`py-1.5 px-4 font-semibold ${isYongin ? 'bg-cyan-50' : 'bg-slate-100'}`} rowSpan={2}>
                                                {row.university.replace('대학교', '')}
                                            </td>
                                        )}
                                        <td className="py-1.5 text-center">{row.scope}</td>
                                        {row.data.map((value, idx) => (
                                            <td key={idx} className="py-1.5 text-center">
                                                {value !== null ? value.toFixed(2) : '—'}
                                            </td>
                                        ))}
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                </div>
            </div>
            
            {/* 차트 부분: 박스 높이를 맞추기 위한 구조 */}
            <div className="lg:col-span-1 rounded-2xl border border-gray-200 p-3 shadow bg-white min-h-[350px] h-full flex flex-col">
                
                {/* [수정 완료] 제목과 버튼을 같은 줄에 배치하고 justify-between 적용 */}
                <div className="flex items-center justify-between mb-3 flex-wrap gap-2">
                    {/* 제목을 버튼보다 먼저 배치하여 왼쪽에 정렬 */}
                    <h3 className="text-base font-bold text-slate-800">
                        {currentYear} 대학별 {scope} 연구비 비교
                    </h3>
                    <div className="flex gap-2 border border-gray-200 rounded-xl shadow overflow-hidden">
                        {['교외', '교내'].map(s => (
                            <button
                                key={s}
                                onClick={() => setScope(s)}
                                className={`px-3 py-1.5 text-sm transition-all font-semibold ${
                                    scope === s ? 'bg-[#028EA7] text-white' : 'bg-white text-slate-600'
                                }`}
                            >
                                {s} 연구비
                            </button>
                        ))}
                    </div>
                </div>
                <div className="h-[280px]">
                    <Bar data={chartData} options={options} />
                </div>
                <p className="text-xs text-slate-500 px-1 mt-2">
                    * Y축 범위는 표시되는 데이터의 최댓값에 따라 동적으로 설정됩니다.
                </p>
            </div>
        </div>
    );
}

export default function FacultyResearchExpensePerFaculty() {
    const [year, setYear] = useState('2024');
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);
    const COMPARE_UNI_NAME = '명지대학교';

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            setError(false);
            try {
                const [compareInRes, compareOutRes] = await Promise.all([
                    axios.get(`${BASE_URL}${API_ENDPOINTS.uni}?scope=inside`),
                    axios.get(`${BASE_URL}${API_ENDPOINTS.uni}?scope=outside`),
                ]);
                const formattedData = formatData(compareInRes.data, compareOutRes.data);
                setData(formattedData);
                if (!formattedData['용인대학교']?.[year]) {
                    const availableYears = YEARS.filter(y => formattedData['용인대학교']?.[y]);
                    if (availableYears.length > 0) {
                        setYear(availableYears[availableYears.length - 1]);
                    }
                }
            } catch (error) {
                console.error("데이터 로드 중 오류 발생 (프론트엔드 대응):");
                if (axios.isAxiosError(error) && error.response) {
                    console.error(`요청 URL: ${error.config.url}, 상태 코드: ${error.response.status}`);
                    setError(true);
                } else {
                    console.error("예상치 못한 오류:", error);
                    setError(true);
                }
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    const yearsWithData = useMemo(() => {
        return YEARS;
    }, []);

    const {
        yonginIn,
        yonginOut,
        compareIn,
        compareOut,
        yonginTotal,
        compareTotal,
        gapTotalWithCompare,
        yoyOut,
        yoyIn
    } = useMemo(() => {
        if (!data) {
            return {
                yonginIn: null, yonginOut: null, compareIn: null, compareOut: null,
                yonginTotal: null, compareTotal: null, gapTotalWithCompare: null,
                yoyOut: null, yoyIn: null
            };
        }
        const yonginIn = data['용인대학교']?.[year]?.교내 ?? 0;
        const yonginOut = data['용인대학교']?.[year]?.교외 ?? 0;
        const compareIn = data[COMPARE_UNI_NAME]?.[year]?.교내 ?? 0;
        const compareOut = data[COMPARE_UNI_NAME]?.[year]?.교외 ?? 0;
        const compareTotal = compareIn + compareOut;
        const yonginTotal = yonginIn + yonginOut;
        const gapTotalWithCompare = yonginTotal - compareTotal;
        const prevYear = YEARS[YEARS.indexOf(year) - 1];
        const yoyOut = prevYear && data['용인대학교'][prevYear] ? yonginOut - (data['용인대학교'][prevYear]?.교외 ?? 0) : null;
        const yoyIn = prevYear && data['용인대학교'][prevYear] ? yonginIn - (data['용인대학교'][prevYear]?.교내 ?? 0) : null;

        return {
            yonginIn,
            yonginOut,
            compareIn,
            compareOut,
            yonginTotal,
            compareTotal,
            gapTotalWithCompare,
            yoyOut: yoyOut !== null ? yoyOut : 0,
            yoyIn: yoyIn !== null ? yoyIn : 0,
        };
    }, [data, year]);

    if (loading) {
        return (
            <div className="w-full h-full flex items-center justify-center p-6 bg-white rounded-2xl shadow mt-4 border border-gray-300">
                <p className="text-lg text-gray-500">데이터를 불러오는 중...</p>
            </div>
        );
    }
    if (error || !data) {
        return (
            <div className="w-full h-full flex flex-col items-center justify-center p-6 bg-red-50 rounded-2xl shadow mt-4 border border-red-300">
                <p className="text-lg font-semibold text-red-700 mb-2">데이터 로드 실패</p>
                <p className="text-sm text-red-600">서버에서 데이터 로드 중 오류가 발생했습니다. 백엔드 로그 또는 API 상태를 확인하세요.</p>
                <p className="text-xs text-red-500 mt-2">프론트엔드 코드에서는 더 이상 해결할 수 없습니다.</p>
            </div>
        );
    }

    return (
        <div className="w-full h-full mx-auto p-6 bg-white rounded-2xl shadow mt-4 border border-gray-300 font-['Inter']">
            <div className="flex items-center justify-between flex-wrap gap-2 mb-4">
                <h2 className="text-xl font-semibold text-gray-800">전임교원 1인당 연구비 (단위: 천원/인)</h2>
                <div className="inline-flex gap-2">
                    {yearsWithData.map((y) => (
                        <button
                            key={y}
                            onClick={() => setYear(y)}
                            className={`px-3 py-1.5 rounded-full border text-sm transition-all ${
                                year === y
                                    ? 'bg-[#028EA7] text-white border-[#028EA7] shadow'
                                    : 'border-gray-300 text-[#028EA7] hover:bg-[#e6f6f9]'
                            }`}
                        >
                            {y}
                        </button>
                    ))}
                </div>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mb-5">
                <div className="rounded-xl border border-gray-200 p-4 shadow bg-white">
                    <div className="text-sm font-semibold text-gray-500 truncate">용인대 교내 / 교외 연구비 — {year}</div>
                    <div className="text-2xl sm:text-3xl font-bold">
                        {yonginIn?.toFixed(2)} / {yonginOut?.toFixed(2)}
                        <span className="text-base text-gray-500 ml-1">천원/인</span>
                    </div>
                    <div className="flex flex-wrap gap-2 mt-2">
                        <div
                            className={`inline-flex items-center gap-1 text-xs rounded-full px-2 py-0.5 ${
                                yoyOut == null
                                    ? 'bg-slate-50 text-slate-500'
                                    : yoyOut >= 0
                                    ? 'bg-emerald-50 text-[#028EA7]'
                                    : 'bg-rose-50 text-rose-600'
                            }`}
                        >
                            {yoyOut == null ? '— 교외 전년 없음' : `${yoyOut >= 0 ? '▲' : '▼'} ${Math.abs(yoyOut).toFixed(2)} 천원(교외 전년대비)`}
                        </div>
                        <div
                            className={`inline-flex items-center gap-1 text-xs rounded-full px-2 py-0.5 ${
                                yoyIn == null
                                    ? 'bg-slate-50 text-slate-500'
                                    : yoyIn >= 0
                                    ? 'bg-emerald-50 text-[#028EA7]'
                                    : 'bg-rose-50 text-rose-600'
                            }`}
                        >
                            {yoyIn == null ? '— 교내 전년 없음' : `${yoyIn >= 0 ? '▲' : '▼'} ${Math.abs(yoyIn).toFixed(2)} 천원(교내 전년대비)`}
                        </div>
                    </div>
                </div>
                <div className="rounded-xl border border-gray-200 p-4 shadow bg-white">
                    <div className="text-sm font-semibold text-gray-500 truncate">{COMPARE_UNI_NAME.replace('대학교', '')} 교내 / 교외 연구비 — {year}</div>
                    <div className="text-2xl sm:text-3xl font-bold">
                        {compareIn?.toFixed(2)} / {compareOut?.toFixed(2)}
                        <span className="text-base text-gray-500 ml-1">천원/인</span>
                    </div>
                    <div className="mt-1 text-xs text-gray-500">
                        용인대 총합: {yonginTotal?.toFixed(2)}
                        <span className={`ml-2 ${yonginTotal - compareTotal >= 0 ? 'text-[#028EA7]' : 'text-rose-600'} font-semibold`}>
                            {yonginTotal - compareTotal >= 0 ? ' (우위)' : ' (열위)'}
                        </span>
                    </div>
                </div>
                <div className="rounded-xl border border-gray-200 p-4 shadow bg-white">
                    <div className="text-sm font-semibold text-gray-500 truncate">용인대 vs {COMPARE_UNI_NAME.replace('대학교', '')} 총합 격차 ({year})</div>
                    <div className={`text-3xl font-bold mt-1 ${gapTotalWithCompare >= 0 ? 'text-[#028EA7]' : 'text-rose-600'}`}>
                        {formatDiff(gapTotalWithCompare)}
                        <span className="text-base font-normal ml-1">천원/인</span>
                    </div>
                    <div className={`mt-1 text-xs font-medium`}>
                        용인대가 {COMPARE_UNI_NAME.replace('대학교', '')} 총합보다
                        <span className={gapTotalWithCompare >= 0 ? 'text-[#028EA7]' : 'text-rose-600'}>
                            {` ${gapTotalWithCompare >= 0 ? '높습니다' : '낮습니다'}`}
                        </span>
                    </div>
                </div>
            </div>

            {/* 표+차트 비교 섹션 */}
            <ResearchCompareSection
                title="전임교원 1인당 연구비"
                data={data}
                yearsWithData={yearsWithData}
                currentYear={year}
            />
            {/* 하단 구분선 */}
            <div className="mt-6 mx-auto w-[200px] border-t border-gray-200" />
        </div>
    );
}