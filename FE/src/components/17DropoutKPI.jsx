import React, { useEffect, useMemo, useState } from "react";
import axios from "axios";
import { Bar, Doughnut } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale, LinearScale, BarElement, PointElement,
  Tooltip, Legend, Title, ArcElement
} from "chart.js";

ChartJS.register(
  CategoryScale, LinearScale, BarElement, PointElement,
  Tooltip, Legend, Title, ArcElement
);

// 브랜드/보조 색상
const BRAND = "#028EA7";
const BRAND_SOFT = "rgba(2,142,167,0.6)";
const ALT = "#efe63bff";
const ALT_SOFT = "rgba(221,230,123,0.35)";
const DOUGHNUT_RISK_COLOR = "#D85A5A";
const DOUGHNUT_RISK_SOFT = "rgba(216, 90, 90, 0.7)";
const DOUGHNUT_NORMAL_COLOR = "#89C4C5";
const DOUGHNUT_NORMAL_SOFT = "rgba(137, 196, 197, 0.7)";

function makeDeptDoughnutData(v) {
  const totalCount = v.total.count;
  const riskPct = totalCount > 0 ? (v.risk.count / totalCount) * 100 : 0;
  const normalPct = totalCount > 0 ? (v.normal.count / totalCount) * 100 : 0;
  return {
    labels: ["잠재위험학생", "일반학생"],
    datasets: [
      {
        label: "학과 내 비율",
        data: [v.riskRate, v.normalRate],
        backgroundColor: [DOUGHNUT_RISK_SOFT, DOUGHNUT_NORMAL_SOFT],
        borderColor: [DOUGHNUT_RISK_COLOR, DOUGHNUT_NORMAL_COLOR],
        borderWidth: 2,
        hoverOffset: 4
      },
    ],
  };
}

const FACTOR_COGNITIVE = [
  { label: "대학선택만족", 2023: 64.94, 2024: 60.91 },
  { label: "전공진로확신", 2023: 63.85, 2024: 45.55 },
  { label: "대학에대한기대", 2023: 58.63, 2024: 45.86 }, // 약간 보정
  { label: "대학생활전념의지", 2023: 52.60, 2024: 36.80 }, // 약간 보정
  { label: "대학선택만족도", 2023: 62.47, 2024: 67.10 },
  { label: "대학환경만족", 2023: 63.40, 2024: 66.20 },
  { label: "대학체계만족", 2023: 67.80, 2024: 66.90 },
  { label: "대학충성도", 2023: 52.80, 2024: 58.50 },
  { label: "전공확신", 2023: 53.40, 2024: 52.30 },
  { label: "진로확신", 2023: 50.20, 2024: 48.20 },
  { label: "교수신뢰", 2023: 60.60, 2024: 57.80 },
  { label: "학업충실도", 2023: 45.80, 2024: 45.50 },
];

const FACTOR_AFFECT = [
  { label: "정서안정성", 2023: 53.82, 2024: 45.05 },
  { label: "주변으로부터의지지", 2023: 61.22, 2024: 59.56 },
  { label: "진로적합성", 2023: 47.84, 2024: 47.20 },
  { label: "학습효능감", 2023: 47.69, 2024: 46.06 },
  { label: "시험불안", 2023: 50.50, 2024: 51.24 },
  { label: "학업부담감", 2023: 49.20, 2024: 50.47 },
];

const FACTOR_LEARNING = [
  { label: "학습비전", 2023: 44.83, 2024: 43.52 },
  { label: "학습관리", 2023: 46.95, 2024: 45.30 },
  { label: "행동전략", 2023: 47.05, 2024: 44.09 },
  { label: "인지전략", 2023: 47.34, 2024: 44.83 },
  { label: "촉진전략", 2023: 40.47, 2024: 45.02 },
];

const FACTOR_ADAPT = [
  { label: "경제적여건", 2023: 53.34, 2024: 55.85 },
  { label: "경제적지원", 2023: 62.24, 2024: 62.24 },
  { label: "대학적응자신감", 2023: 53.79, 2024: 54.47 },
  { label: "행사참여도", 2023: 46.90, 2024: 49.99 },
  { label: "인간관계친화성", 2023: 47.51, 2024: 46.72 },
];

// 막대 차트 데이터 (컬러/테두리/둥글기 적용)
function makeFactorBar(rows2023, rows2024) {
  const labels = [...new Set([...rows2023.map(d => d.label), ...rows2024.map(d => d.label)])];
  const findVal = (arr, label) => arr.find(d => d.label === label)?.value || 0;
  return {
    labels,
    datasets: [
      {
        label: "2023",
        data: labels.map(l => findVal(rows2023, l)),
        backgroundColor: BRAND_SOFT,
        borderColor: BRAND,
        borderWidth: 2,
        borderRadius: 10,
        barPercentage: 0.7,
        categoryPercentage: 0.6
      },
      {
        label: "2024",
        data: labels.map(l => findVal(rows2024, l)),
        backgroundColor: ALT_SOFT,
        borderColor: ALT,
        borderWidth: 2,
        borderRadius: 10,
        barPercentage: 0.7,
        categoryPercentage: 0.6
      }
    ]
  };
}

const fmt = (n, digits = 1) => Number(n).toLocaleString(undefined, { maximumFractionDigits: digits });
const ratioLabel = (count, total) => {
  const pct = total > 0 ? ((count / total) * 100).toFixed(1) : "0.0";
  return `${pct}% (${count}명)`;
};

export default function DropoutKPI() {
  const [year, setYear] = useState("2024");
  const [deptId, setDeptId] = useState(22);
  const [deptList, setDeptList] = useState([]);
  const [deptDetail, setDeptDetail] = useState(null);
  const [factorTab, setFactorTab] = useState("인지");

  const [factor2023, setFactor2023] = useState([]);
  const [factor2024, setFactor2024] = useState([]);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/admin/dropout/ai?year=${year}`)
      .then((res) => {
        // "소계"는 제외
        const filtered = res.data.filter((d) => d.departments !== "소계");
        setDeptList(filtered);
      })
      .catch((err) => console.error(err));
  }, [year]);

  useEffect(() => {
    axios
      .get(`http://localhost:8080/api/admin/dropout/detail?deptId=${deptId}&year=${year}`)
      .then((res) => setDeptDetail(res.data))
      .catch((err) => console.error(err));
  }, [deptId, year]);

  useEffect(() => {
      const loadFactor = async () => {
        try {
          const [res2023, res2024] = await Promise.all([
            axios.get(`http://localhost:8080/api/admin/dropout/factor?year=2023&type=${factorTab}`),
            axios.get(`http://localhost:8080/api/admin/dropout/factor?year=2024&type=${factorTab}`),
          ]);
          // API → 그래프용 형식
          const mapData = (arr) =>
            arr.data.map((d) => ({
              label: d.factorName,
              value: d.value,
            }));
          setFactor2023(mapData(res2023));
          setFactor2024(mapData(res2024));
        } catch (err) {
          console.error(err);
        }
      };
      loadFactor();
    }, [factorTab]);

  const factorBarData = useMemo(
    () => makeFactorBar(factor2023, factor2024),
    [factor2023, factor2024]
  );

  const factorBarOpts = useMemo(
    () => ({
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { position: "top", labels: { usePointStyle: true } },
        tooltip: { callbacks: { label: (c) => `${c.dataset.label}: ${fmt(c.raw,1)}` } },
      },
      scales: {
        x: { stacked: false, grid: { display: false } },
        y: { beginAtZero: false, suggestedMin: 30, suggestedMax: 80 },
      },
    }),
    []
  );

  const factorTabs = [
    { key: "인지", title: "인지" },
    { key: "정서", title: "정서" },
    { key: "학습", title: "학습" },
    { key: "적응", title: "적응" },
  ];

  return (
    <div className="w-full h-full mx-auto p-6 bg-white rounded-2xl shadow-md">
      {/* 타이틀 */}
      <div className="mb-7 flex items-center justify-between">
        <h2 className="text-[20px] font-extrabold tracking-tight text-slate-900">중도탈락 현황 (AI융합대학)</h2>
        <div className="inline-flex gap-2">
            {["2023", "2024"].map((y) => (
              <button
                key={y}
                onClick={() => setYear(y)}
                className={`px-3 py-1.5 rounded-full border text-sm transition-all ${
                  year === y
                    ? "bg-[#028EA7] text-white border-[#028EA7] shadow"
                    : "border-[#028EA7] text-[#028EA7] hover:bg-[#e6f6f9]"
                }`}
              >
                {y}
              </button>
            ))}
          </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-5 mb-6">
        {/* 표 */}
        <div className="rounded-2xl border border-gray-200 p-4 shadow bg-white">
          <h3 className="text-base font-bold">
            지표값 : <span className="text-[#028EA7]">잠재위험/일반/전체</span>
            <span className="text-xs text-slate-500 ml-1">({year})</span>
          </h3>
          <div className="overflow-x-auto ">
            <table className="min-w-[620px] w-full text-center border-collapse mt-4">
              <thead>
                <tr className="bg-slate-50 border-b border-gray-300">
                  <th className="px-4 py-3 w-40 text-left border-r border-gray-300">학부(과)</th>
                  <th className="px-4 py-3 border-r border-gray-300">잠재위험학생</th>
                  <th className="px-4 py-3 border-r border-gray-300">일반학생</th>
                  <th className="px-4 py-3">전체</th>
                </tr>
              </thead>
              <tbody>
                {deptList.map((d, i) => {
                  const isSelected = d.departments === deptDetail?.departments;
                  const rowClass = i % 2 ? "bg-white" : "bg-slate-50/40";
                  const deptCellStyle = `
                    cursor-pointer hover:underline
                    bg-sky-50 text-sky-700
                    ${isSelected ? "bg-blue-100" : ""}
                  `;
                  const maxRisk = Math.max(...deptList.map(d => d.atRiskStudents || 0));

                  return (
                    <tr key={d.departments} className={rowClass} onClick={() => setDeptId(22 + i)}>
                      <td className={`px-4 py-2 border shadow text-left font-semibold border-r border-gray-200 ${deptCellStyle}`}>
                          {d.departments}
                      </td>
                      <td className={`px-4 py-2 border shadow border-r border-gray-200 font-semibold
                      ${d.atRiskStudents === maxRisk
                          ? "bg-red-100 text-[#D85A5A]" : "text-[#D85A5A]"}`}>
                        {d.atRiskStudents} ({d.riskRate}%)
                      </td>
                      <td className="px-4 py-2 border shadow border-r border-gray-200 text-[#028EA7] font-semibold">
                        {d.nonRiskStudents} ({d.normalRate}%)
                      </td>
                      <td className="px-4 py-2 border shadow border-r border-gray-200">
                        {d.total} ({d.totalRate}%)
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
        {/* 도넛+수치 */}
        <div className="rounded-2xl border border-gray-200 p-4 shadow bg-white">
          {deptDetail ? (
            <>
              <div className="font-semibold text-lg text-slate-800 mb-2">
                {deptDetail.departments} 상세 ({year})
              </div>
              <div className="grid grid-cols-3 gap-3 mb-3">
                <div className="rounded-xl border border-gray-200 p-3 shadow text-center bg-white">
                  <div className="text-xs text-slate-500">잠재위험학생</div>
                  <div className="text-2xl font-extrabold text-[#D85A5A]">
                    {ratioLabel(deptDetail.atRiskStudents, deptDetail.total)}
                  </div>
                </div>
                <div className="rounded-xl border border-gray-200 p-3 shadow text-center bg-white">
                  <div className="text-xs text-slate-500">일반학생</div>
                  <div className="text-2xl font-extrabold text-[#028EA7]">
                    {ratioLabel(deptDetail.nonRiskStudents, deptDetail.total)}
                  </div>
                </div>
                <div className="rounded-xl border border-gray-200 p-3 shadow text-center bg-white">
                  <div className="text-xs text-slate-500">전체 학생</div>
                  <div className="text-2xl font-extrabold text-[#334155]">
                    {deptDetail.total}명
                  </div>
                </div>
              </div>
              <div className="h-[260px] flex items-center justify-center">
                <Doughnut
                  data={makeDeptDoughnutData(deptDetail)}
                  options={{
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                      legend: { position: "right", labels: { usePointStyle: true } },
                      tooltip: {
                        callbacks: {
                          label: ({ label, parsed }) => {
                            const pct = parsed.toFixed(1);
                            const count = Math.round((parsed / 100) * deptDetail.total);
                            return `${label}: ${pct}% (${count}명)`;
                          }
                        }
                      }
                    }
                  }}
                />
              </div>
            </>
          ) : (
            <p className="text-center text-slate-500">학과 정보를 불러오는 중...</p>
          )}
        </div>
      </div>
      {/* 막대그래프 (요인분석, 탭) */}
      <div className="rounded-2xl border border-gray-200 p-4 shadow bg-white mb-5">
        <div className="flex items-center justify-between mb-3">
          <h3 className="text-xl font-bold">재학생 중도탈락 예방지표 분석 - 잠재위험학생</h3>
          <div className="inline-flex gap-2">
            {factorTabs.map(({key, title}) => (
              <button
                key={key}
                onClick={() => setFactorTab(key)}
                className={`px-3 py-1.5 rounded-full border text-sm transition-all ${
                  factorTab === key
                    ? "bg-[#028EA7] text-white border-[#028EA7] shadow"
                    : "border-[#028EA7] text-[#028EA7] hover:bg-[#e6f6f9]"
                }`}
              >
                {title}
              </button>
            ))}
          </div>
        </div>
        <div className="h-[380px] mb-3">
          {factor2023.length > 0 || factor2024.length > 0 ? (
          <Bar data={factorBarData} options={factorBarOpts} />
          ) : (
            <p className="text-center text-slate-500 mt-20">
              데이터를 불러오는 중입니다...
            </p>
          )}
        </div>
        <div className="text-xs text-slate-600 space-y-1 p-2 border-t border-gray-200 mt-2">
          <p>
            지표 해석: 점수가 높을수록 해당 요인에 대한 적응 수준이 안정적임을 의미합니다. (만점 기준 100점)
          </p>
        </div>
      </div>
    </div>
  );
}
